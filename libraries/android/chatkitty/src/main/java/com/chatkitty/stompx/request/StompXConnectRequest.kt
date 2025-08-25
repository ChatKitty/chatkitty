package com.chatkitty.stompx.request

import com.chatkitty.stompx.model.StompXError

data class StompXConnectRequest(
  val apiKey: String,
  val authParams: Map<String, Any>?,
  val onConnected: () -> Unit,
  val onError: (StompXError) -> Unit
)
