package com.chatkitty.stompx.request

import com.chatkitty.stompx.model.StompXEventHandler
import kotlin.reflect.KClass

internal data class StompXListenForEventRequest<E : Any>(
  val topic: String,
  val type: KClass<E>,
  val handler: StompXEventHandler<E>
)
