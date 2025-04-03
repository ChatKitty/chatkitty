package com.chatkitty.ui.stompx.request

data class StompXDisconnectRequest(
  val onSuccess: () -> Unit,
  val onError: () -> Unit
)
