package com.chatkitty.ui.stompx

import com.chatkitty.ui.stompx.model.RtmEvent
import com.chatkitty.ui.stompx.model.StompEventHandlers
import com.chatkitty.ui.stompx.request.*
import com.chatkitty.ui.stompx.stomp.StompSpecification
import com.chatkitty.ui.stompx.transport.makeOkHttpClient
import com.chatkitty.ui.websocket.logging.loggerFactory
import com.chatkitty.ui.websocket.model.WebSocketClientState
import com.chatkitty.ui.websocket.model.WebSocketConnectionLostException
import com.chatkitty.ui.websocket.model.stomp.*
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.reactivex.Notification
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import okhttp3.*
import okhttp3.internal.closeQuietly
import java.util.*
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.pow
import kotlin.reflect.KClass

internal class StompXImpl(
  private val configuration: StompXConfiguration
) : StompX {
  companion object {
    private const val MAX_WAIT_INTERVAL = 1 * 60 * 1000L

    private const val HEART_BEAT_INTERVAL = 5000L

    private const val PENDING_MESSAGE_THRESHOLD = 60 * 1000L

    private const val WEB_SOCKET_NORMAL_CLOSURE_CODE = 1000
    private const val WEB_SOCKET_CLIENT_STOPPED_CODE = 4000
    private const val WEB_SOCKET_FAILED_HEALTH_CHECK_CODE = 4999
    private val watchForReceiptSubject = PublishSubject.create<String>()

    private val objectMapper = ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .registerKotlinModule()

    private val client = makeOkHttpClient(
      isDebug = false,
      retryIOExceptions = true,
      wrapIOExceptions = false,
      userAgent = "ChatKitty-Android",
      cacheDirectory = "/tmp/chatkitty"
    )

    val disposeBag = CompositeDisposable()
  }

  private val logger by loggerFactory()

  private inner class StompClientWebSocketListener : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
      when (state) {
        WebSocketClientState.STARTED -> { webSocket.sendFrame(specification.connect(
          configuration.host,
          configuration.username))
        }

        WebSocketClientState.STOPPED -> {
          webSocket.close(WEB_SOCKET_CLIENT_STOPPED_CODE, "StompWebSocketClient is stopped")
        }
      }
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
      when (state) {
        WebSocketClientState.STARTED -> {
          try {
            if (text.isBlank()) {
              lastReceivedHeartBeatTime = System.currentTimeMillis()

              webSocket.sendPendingFrames()
              webSocket.sendPendingMessages()
            } else {
              val frame = text.asStompServerFrame()

              logger.info(
                """
                  <<<
                  ${frame.text}
                  <<<
                """.trimIndent()
              )

              when (frame.command) {
                StompServerCommand.CONNECTED -> {
                  socket = webSocket

                  subscriptions.forEach { subscribe(it) }

                  onConnectedSubject.onNext(Unit)

                  if (lastRetryTime != null) {
                    messages.onNext(Notification.createOnError(WebSocketConnectionLostException()))
                  }

                  resetRetries()
                }

                StompServerCommand.MESSAGE -> {
                  val relayRequest = pendingRelayRequests.remove(frame["subscription"])
                  if (relayRequest != null) {
                    val type = relayRequest.embeddedType
                      ?.let {
                        val innerType =
                          objectMapper.typeFactory.constructParametricType(
                            relayRequest.type.java,
                            it.java
                          )

                        objectMapper.typeFactory.constructParametricType(
                          RtmEvent::class.java,
                          innerType
                        )
                      } ?: objectMapper.typeFactory.constructParametricType(
                      RtmEvent::class.java,
                      relayRequest.type.java
                    )

                    val rtmEvent: RtmEvent<*> = objectMapper.readValue(frame.body, type)

                    @Suppress("UNCHECKED_CAST")
                    val onSuccess = relayRequest.onSuccess as (Any?) -> Unit

                    onSuccess(rtmEvent.resource)
                  } else {
                    webSocket.sendFrame(specification.ack(frame["ack"] ?: return), queueable = true)
                  }

                  frame["receipt-id"]?.let {
                    val performActionRequest = pendingActionRequests.remove(it)

                    if (performActionRequest != null) {
                      val type = performActionRequest.embeddedType
                        ?.let {
                          val innerType =
                            objectMapper.typeFactory.constructParametricType(
                              performActionRequest.type.java,
                              it.java
                            )

                          objectMapper.typeFactory.constructParametricType(
                            RtmEvent::class.java,
                            innerType
                          )
                        } ?: objectMapper.typeFactory.constructParametricType(
                        RtmEvent::class.java,
                        performActionRequest.type.java
                      )

                      val rtmEvent: RtmEvent<*> = objectMapper.readValue(frame.body, type)

                      @Suppress("UNCHECKED_CAST")
                      val onSuccess = performActionRequest.onSuccess as (Any?) -> Unit

                      onSuccess(rtmEvent.resource)
                    }
                  }

                  eventHandlers[frame["destination"]]
                    ?.let { handlers ->
                      val type =
                        objectMapper.typeFactory.constructParametricType(
                          RtmEvent::class.java,
                          handlers.type.java
                        )

                      val rtmEvent: RtmEvent<*> = objectMapper.readValue(frame.body, type)

                      handlers
                        .filter { it.event == rtmEvent.type }
                        .forEach {
                          @Suppress("UNCHECKED_CAST")
                          val onSuccess = it.onSuccess as (Any?) -> Unit

                          onSuccess(rtmEvent.resource)
                        }
                    }
                }

                StompServerCommand.RECEIPT -> {
                  val receipt = frame["receipt-id"]
                  watchForReceiptSubject.onNext(receipt ?: "")
                  when (val receipt = frame["receipt-id"]) {
                    disconnectReceipt ->
                      webSocket.close(
                        WEB_SOCKET_NORMAL_CLOSURE_CODE,
                        reason = "Disconnected WebSocket client"
                      )

                    in pendingMessages ->
                      pendingMessages.remove(receipt)?.let {
                        when (it) {
                          is CompletablePendingStompMessage ->
                            it.emitter.onComplete()

                          is SinglePendingStompMessage ->
                            it.emitter.onSuccess(it.message.receipt)
                        }
                      }
                  }
                }

                StompServerCommand.ERROR -> {
                  logger.warn("Received error frame $frame")
                }
              }
            }
          } catch (e: Exception) {

            logger.warn("Encountered error.", e)
          }
        }

        WebSocketClientState.STOPPED -> {
          webSocket.close(WEB_SOCKET_CLIENT_STOPPED_CODE, "StompWebSocketClient is stopped")
        }
      }
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
      when (state) {
        WebSocketClientState.STARTED -> {
          logger.warn("Connection closed when socket client started. With code $code: $reason.")

          retryConnection()
        }

        WebSocketClientState.STOPPED -> {
          messages.onComplete()
        }
      }
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
      response?.closeQuietly()

      when (state) {
        WebSocketClientState.STARTED -> {
          logger.warn("Failed to connect socket client.", t)

          retryConnection()
        }

        WebSocketClientState.STOPPED -> Unit
      }
    }
  }

  private val specification = StompSpecification(HEART_BEAT_INTERVAL)

  private lateinit var request: Request

  private val listener = StompClientWebSocketListener()

  private val connectionExecutor = Executors.newSingleThreadScheduledExecutor {
    Thread(it, "chatkitty_stomp")
  }

  private val queue: Queue<StompClientFrame> = ArrayBlockingQueue(500)

  private val subscriptions =
    Collections.newSetFromMap(ConcurrentHashMap<StompSubscription, Boolean>())

  private val pendingMessages: MutableMap<String, PendingStompMessage> = ConcurrentHashMap()

  private val eventHandlers: MutableMap<String, StompEventHandlers<*>> =
    ConcurrentHashMap()

  private val onConnectedSubject: PublishSubject<Unit> = PublishSubject.create()

  private val retries = AtomicInteger()

  private val AtomicInteger.waitTime: Long
    get() {
      val tries = toInt()

      return if (tries == 0)
        0
      else
        (2.0.pow(tries.coerceAtMost(10)).toLong() * 500L)
          .coerceAtMost(MAX_WAIT_INTERVAL)
    }

  @Volatile
  private var state = WebSocketClientState.STOPPED

  @Volatile
  private var heartBeatCheckFuture: ScheduledFuture<*>? = null

  @Volatile
  private var retryFuture: ScheduledFuture<*>? = null

  @Volatile
  private var lastReceivedHeartBeatTime: Long? = null

  @Volatile
  private var lastRetryTime: Long? = null

  @Volatile
  private lateinit var socket: WebSocket

  private lateinit var messages: PublishSubject<Notification<StompServerFrame>>

  private val pendingRelayRequests: MutableMap<String, StompXRelayResourceRequest<*>> =
    ConcurrentHashMap()

  private val pendingActionRequests: MutableMap<String, StompXPerformActionRequest<*>> =
    ConcurrentHashMap()

  private lateinit var disconnectReceipt: String

  override fun connect(request: StompXConnectRequest) {
    if (state == WebSocketClientState.STARTED) return

    this.request = with(configuration) {
      val builder = Request.Builder()
        .url("${if (isSecure) "wss" else "ws"}://$host/stompx/websocket?api-key=${request.apiKey}")

      builder.build()
    }

    logger.info("Starting service...")

    state = WebSocketClientState.STARTED

    messages = PublishSubject.create()

    disconnectReceipt = receipt()

    resetHeartBeat()

    resetRetries()

    onConnectedSubject.take(1).subscribe {
      request.onConnected()
    }

    connectWebSocket()

    heartBeatCheckFuture = connectionExecutor.scheduleAtFixedRate({
      when (state) {
        WebSocketClientState.STARTED -> {
          val lastHeartBeatTime = lastReceivedHeartBeatTime
            ?: return@scheduleAtFixedRate

          if (System.currentTimeMillis() - lastHeartBeatTime < HEART_BEAT_INTERVAL * 3) {
            socket.sendHeartBeat()
          } else {
            resetHeartBeat()

            val socketWasOpen =
              socket.close(WEB_SOCKET_FAILED_HEALTH_CHECK_CODE, reason = "Failed heart beat check")

            if (!socketWasOpen) {
              retryConnection()
            }
          }
        }

        WebSocketClientState.STOPPED -> {
          socket.close(WEB_SOCKET_CLIENT_STOPPED_CODE, "StompWebSocketClient is stopped")
        }
      }
    }, 0, HEART_BEAT_INTERVAL, TimeUnit.MILLISECONDS)
  }

  override fun <R : Any> relayResource(request: StompXRelayResourceRequest<R>): Unit =
    with(request) {
      val id = subscriptionId()

      pendingRelayRequests[id] = request

      socket.sendFrame(
        specification.subscribe(
          StompSubscription(
            id = id,
            destination = destination,
            receipt = receipt()
          )
        )
      )
    }

  override fun <T : Any> subscribeToTopic(request: StompXSubscribeToTopicRequest<T>): () -> Unit =
    with(request) {
      val receiptId = receipt()
      val subscription = StompSubscription(
        id = subscriptionId(),
        destination = topic,
        receipt = receiptId
      )

      disposeBag.add(
        watchForReceiptSubject
          .take(1)
          .filter { it == receiptId }
          .subscribeBy(
            onNext = {
              request.onSuccess?.invoke()
            }
          )
      )

      subscribe(subscription);

      {
        unsubscribe(subscription)
      }
    }

  override fun <E : Any> listenForEvent(request: StompXListenForEventRequest<E>): () -> Unit =
    with(request) {
      @Suppress("UNCHECKED_CAST")
      val handlers = ensureStompEventHandlerForTopic(topic, request.type)

      handlers += handler

      eventHandlers[topic] = handlers

      {
        handlers -= handler
      }
    }

  override fun <R: Any> sendAction(request: StompXPerformActionRequest<R>): Unit =
    with(request) {
      val receipt = receipt()

      pendingActionRequests[receipt] = request

      val jsonData = objectMapper.writeValueAsBytes(request.body)

      disposeBag.add(
        watchForReceiptSubject
          .take(1)
          .filter { it == receipt }
          .subscribeBy(
            onNext = {
              request.onSent?.invoke()
            }
          )
      )

      socket.sendFrame(
        specification.sendJsonMessage(
          StompMessage(
            destination = destination,
            receipt = receipt,
            payload = jsonData
          )
        )
      )
    }

  override fun disconnect(request: StompXDisconnectRequest): Unit =
    with(request) {
      val receipt = receipt()

      disposeBag.add(
        watchForReceiptSubject
          .take(1)
          .filter { it == receipt }
          .subscribeBy(
            onNext = {
              request.onSuccess.invoke()
            }
          )
      )

      socket.sendFrame(specification.disconnect(receipt))
    }

  private fun receipt(): String =
    "receipt-${UUID.randomUUID()}"

  private fun subscriptionId(): String =
    "subscription-id-${UUID.randomUUID()}"

  private fun WebSocket.sendHeartBeat() {
    send(specification.generateHeartBeat())
  }

  private fun resetHeartBeat() {
    lastReceivedHeartBeatTime = null
  }

  private fun connectWebSocket() {
    socket = client.newWebSocket(request, listener)
  }

  private fun retryConnection(): Unit =
    synchronized(this) {
      if (retryFuture != null) return

      val waitTime = retries.waitTime

      logger.info("Retrying connection: waiting for ${waitTime}ms. Attempt ${retries.get() + 1}.")

      resetHeartBeat()

      retryFuture = connectionExecutor.schedule({
        lastRetryTime = System.currentTimeMillis()
        retryFuture = null

        connectWebSocket()
      }, waitTime, TimeUnit.MILLISECONDS)

      retries.incrementAndGet()
    }

  private fun resetRetries() {
    retries.set(0)

    lastRetryTime = null
  }

  private fun subscribe(subscription: StompSubscription) {
    subscriptions += subscription

    socket.sendFrame(specification.subscribe(subscription))
  }

  private fun unsubscribe(subscription: StompSubscription) {
    subscriptions -= subscription

    socket.sendFrame(specification.unsubscribe(subscription))
  }

  private fun WebSocket.sendFrame(frame: StompClientFrame, queueable: Boolean = false): Boolean {
    logger.info(
      """
        >>>
        ${frame.text}
        >>>
      """.trimIndent()
    )

    if (!send(frame.text)) {
      if (queueable) {
        queue.offer(frame)
      }

      return false
    }

    return true
  }

  private fun WebSocket.sendPendingFrames() {
    synchronized(queue) {
      while (!queue.isEmpty()) {
        sendFrame(queue.remove())
      }
    }
  }

  private fun WebSocket.sendPendingMessages() {
    val now = System.currentTimeMillis()

    val pendingIterator = pendingMessages.entries.iterator()

    while (pendingIterator.hasNext()) {
      val (_, pending) = pendingIterator.next()

      with(pending.message) {
        if (pending < now - HEART_BEAT_INTERVAL * 3) {
          val sent = sendFrame(specification.sendJsonMessage(pending.message))

          if (!sent && pending < now - PENDING_MESSAGE_THRESHOLD) {
            pendingIterator.remove()

            val exception = MessageNotSentException(receipt, payload)

            when (pending) {
              is CompletablePendingStompMessage ->
                pending.emitter.onError(exception)

              is SinglePendingStompMessage ->
                pending.emitter.onError(exception)
            }
          }

          pending.retryCount.incrementAndGet()
        }
      }
    }
  }

  private fun <E : Any> ensureStompEventHandlerForTopic(
    topic: String,
    eventType: KClass<E>
  ): StompEventHandlers<E> {
    val existingHandlers = eventHandlers[topic]

    @Suppress("UNCHECKED_CAST")
    (return if (existingHandlers != null) {
      existingHandlers as StompEventHandlers<E>
    } else {
      val newHandlers = StompEventHandlers(eventType)
      eventHandlers[topic] = newHandlers as StompEventHandlers<*>
      newHandlers
    })
  }
}
