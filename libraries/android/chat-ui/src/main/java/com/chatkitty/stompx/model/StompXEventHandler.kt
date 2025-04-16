package com.chatkitty.stompx.model

internal data class StompXEventHandler<E : Any>(
  val event: String,
  val onSuccess: (event: E) -> Unit
)
