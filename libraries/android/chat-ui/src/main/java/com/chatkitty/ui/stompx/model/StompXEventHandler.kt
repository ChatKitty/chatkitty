package com.chatkitty.ui.stompx.model

internal data class StompXEventHandler<E : Any>(
  val event: String,
  val onSuccess: (event: E) -> Unit
)
