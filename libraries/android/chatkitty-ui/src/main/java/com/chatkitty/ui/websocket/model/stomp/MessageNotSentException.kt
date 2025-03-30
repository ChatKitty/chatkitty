package com.chatkitty.ui.websocket.model.stomp

class MessageNotSentException(
  val receipt: String,
  val payload: Any
) : Exception()
