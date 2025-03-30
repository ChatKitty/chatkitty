package com.chatkitty.ui.stompx.request

import com.chatkitty.ui.stompx.model.StompXError

data class StompXConnectRequest(
  val apiKey: String,
  val authParams: Map<String, Any>?,
  val onConnected: () -> Unit,
  val onError: (StompXError) -> Unit
)
