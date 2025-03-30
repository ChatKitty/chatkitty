package com.chatkitty.ui.stompx.model

data class RtmEvent<R>(
  val type: String,
  val resource: R?
)
