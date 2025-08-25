package com.chatkitty.stompx.request

data class StompXDisconnectRequest(
  val onSuccess: () -> Unit,
  val onError: () -> Unit
)
