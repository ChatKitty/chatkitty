package com.chatkitty.ui.stompx

import com.chatkitty.ui.stompx.request.*

internal interface StompX {
  fun connect(request: StompXConnectRequest)
  fun <R : Any> relayResource(request: StompXRelayResourceRequest<R>)
  fun <T : Any> subscribeToTopic(request: StompXSubscribeToTopicRequest<T>): () -> Unit
  fun <E : Any> listenForEvent(request: StompXListenForEventRequest<E>): () -> Unit
  fun <R: Any> sendAction(request: StompXPerformActionRequest<R>)
  fun disconnect(request: StompXDisconnectRequest)
}
