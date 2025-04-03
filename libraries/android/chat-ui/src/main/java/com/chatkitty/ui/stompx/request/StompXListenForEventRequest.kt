package com.chatkitty.ui.stompx.request

import com.chatkitty.ui.stompx.model.StompXEventHandler
import kotlin.reflect.KClass

internal data class StompXListenForEventRequest<E : Any>(
  val topic: String,
  val type: KClass<E>,
  val handler: StompXEventHandler<E>
)
