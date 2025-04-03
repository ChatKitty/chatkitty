package com.chatkitty.ui.stompx.request

data class StompXSubscribeToTopicRequest<T : Any>(
  val topic: String,
  val onSuccess: (() -> Unit)? = null
)
