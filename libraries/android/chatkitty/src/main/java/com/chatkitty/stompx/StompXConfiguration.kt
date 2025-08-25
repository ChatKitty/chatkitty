package com.chatkitty.stompx

data class StompXConfiguration(
  val host: String,
  val username: String,
  val isSecure: Boolean,
  val isDebug: Boolean
)
