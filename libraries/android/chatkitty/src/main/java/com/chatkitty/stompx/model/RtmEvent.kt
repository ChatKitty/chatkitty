package com.chatkitty.stompx.model

data class RtmEvent<R>(
  val type: String,
  val resource: R?
)
