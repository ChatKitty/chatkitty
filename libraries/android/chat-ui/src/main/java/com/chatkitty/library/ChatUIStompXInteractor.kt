package com.chatkitty.library

import com.chatkitty.api.StompXBridge
import com.chatkitty.models.ChatUIMessage
import com.chatkitty.models.ConnectPayload
import com.chatkitty.stompx.StompX
import com.chatkitty.stompx.model.ChatKittyGrant
import com.chatkitty.stompx.model.FlexStompXEventType
import com.chatkitty.stompx.model.StompXEventHandler
import com.chatkitty.stompx.model.StompXListenForEventPayload
import com.chatkitty.stompx.model.StompXPerformActionPayload
import com.chatkitty.stompx.model.StompXRelayPayload
import com.chatkitty.stompx.model.StompXResource
import com.chatkitty.stompx.model.StompXSubscribePayload
import com.chatkitty.stompx.request.StompXListenForEventRequest
import com.chatkitty.stompx.request.StompXPerformActionRequest
import com.chatkitty.stompx.request.StompXRelayResourceRequest
import com.chatkitty.stompx.request.StompXSubscribeToTopicRequest
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

internal class ChatUIStompXInteractor(
    private val stompX: StompX,
    private val stompXBridge: StompXBridge
) {
    private var subscriptionMap: MutableMap<String, () -> Unit> = mutableMapOf()
    fun onReceiveMessage(event: ChatUIMessage) {
        when (event.type) {
            "stompx:connect" -> {
                var writeGrant: String? = null
                var readGrant: String? = null

                stompX.relayResource(
                    request = StompXRelayResourceRequest(
                        destination = "/application/v1/user.relay",
                        type = Any::class,
                        onSuccess = { user ->
                            stompX.relayResource(
                                request = StompXRelayResourceRequest(
                                    destination = "/application/v1/user.write_file_access_grant.relay",
                                    type = ChatKittyGrant::class,
                                    onSuccess = { write ->
                                        writeGrant = write.grant
                                        stompX.relayResource(request = StompXRelayResourceRequest(
                                            destination = "/application/v1/user.read_file_access_grant.relay",
                                            type = ChatKittyGrant::class,
                                            onSuccess = { read ->
                                                readGrant = read.grant
                                                stompXBridge.onMessage(
                                                    id = null,
                                                    type = FlexStompXEventType.CONNECT_SUCCESS,
                                                    payload = ConnectPayload(
                                                        user,
                                                        writeGrant,
                                                        readGrant
                                                    )
                                                )
                                            },
                                            onError = {
                                                stompXBridge.onMessage(
                                                    id = null,
                                                    type = FlexStompXEventType.CONNECT_FAILURE,
                                                    payload = StompXResource(resource = null)
                                                )
                                            }
                                        ))
                                    },
                                    onError = {
                                        stompXBridge.onMessage(
                                            id = null,
                                            type = FlexStompXEventType.CONNECT_FAILURE,
                                            payload = StompXResource(resource = null)
                                        )
                                    }
                                )
                            )
                        }, onError = {
                            stompXBridge.onMessage(
                                id = null,
                                type = FlexStompXEventType.CONNECT_FAILURE,
                                payload = StompXResource(resource = null)
                            )
                        }
                    )
                )
            }
            "stompx:resource.relay" -> {
                event.payload?.let { payload ->
                    val relayPayload = synthesize<StompXRelayPayload>(payload)
                    stompX.relayResource(request = StompXRelayResourceRequest(
                        destination = relayPayload.destination,
                        type = Any::class,
                        parameters = relayPayload.parameters?.mapValues { if (it.value) "true" else "false" } ?: emptyMap(),
                        onSuccess = { model ->
                            stompXBridge.onMessage(id = event.id,
                                type = FlexStompXEventType.RELAY_SUCCESS,
                                payload = StompXResource(resource = model)
                            )
                        },
                        onError = {
                            stompXBridge.onMessage(id = event.id,
                                type = FlexStompXEventType.RELAY_ERROR,
                                payload = StompXResource(resource = null)
                            )
                        }
                    ))
                }
            }
            "stompx:topic.subscribe" -> {
                event.payload?.let { payload ->
                    val subscribePayload = synthesize<StompXSubscribePayload>(payload)
                    val subscription = stompX.subscribeToTopic(
                        request = StompXSubscribeToTopicRequest(
                            topic = subscribePayload.topic,
                            onSuccess = {
                                stompXBridge.onMessage(
                                    id = event.id,
                                    type = FlexStompXEventType.TOPIC_SUBSCRIBED,
                                    payload = StompXResource(resource = null)
                                )
                            }
                        )
                    )
                    subscriptionMap[event.id ?: ""] = subscription
                }
            }
            "stompx:event.listen" -> {
                event.payload?.let { payload ->
                    val listenForEventPayload = synthesize<StompXListenForEventPayload>(payload)
                    val subscription = stompX.listenForEvent(
                        request = StompXListenForEventRequest(
                            topic = listenForEventPayload.topic,
                            type = Any::class,
                            handler = StompXEventHandler(event = listenForEventPayload.event, onSuccess = { model ->
                                stompXBridge.onMessage(
                                    id = event.id,
                                    type = FlexStompXEventType.EVENT_PUBLISHED,
                                    payload = StompXResource(resource = model)
                                )
                            })
                        )
                    )

                    subscriptionMap[event.id ?: ""] = subscription
                }
            }
            "stompx:action.perform" -> {
                event.payload?.let { payload ->
                    val performPayload = synthesize<StompXPerformActionPayload>(payload)
                    val request =
                        StompXPerformActionRequest(destination = performPayload.destination,
                            type = Any::class,
                            body = performPayload.body,
                            onSent = {
                                stompXBridge.onMessage(
                                    id = event.id,
                                    type = FlexStompXEventType.ACTION_SENT,
                                    payload = StompXResource(resource = null)
                                )
                            },
                            onSuccess = { resource: Any ->
                                stompXBridge.onMessage(
                                    id = event.id,
                                    type = FlexStompXEventType.ACTION_SUCCESS,
                                    payload = StompXResource(resource = resource)
                                )
                            },
                            onError = {
                                stompXBridge.onMessage(id = event.id,
                                    type = FlexStompXEventType.ACTION_ERROR,
                                    payload = StompXResource(resource = null)
                                )
                            }
                        )
                    stompX.sendAction(request)
                }
            }
            "stompx:topic.unsubscribe" -> {
                event.id?.let { id ->
                    subscriptionMap[id]?.invoke()
                }
            }
            "stompx:event.unsubscribe" -> {
                event.id?.let { id ->
                    subscriptionMap[id]?.invoke()
                }
            }
            "stompx:disconnect" -> {
                subscriptionMap.forEach { (_, value) ->
                    value()
                }
                subscriptionMap.clear()
            }
        }
    }

    inline fun <reified T> synthesize(from: Any): T {
        val objectMapper: ObjectMapper = jacksonObjectMapper()
        return objectMapper.convertValue(from, T::class.java)
    }
}