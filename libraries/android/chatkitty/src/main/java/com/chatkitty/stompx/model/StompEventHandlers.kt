package com.chatkitty.stompx.model

import kotlin.reflect.KClass

internal data class StompEventHandlers<E : Any>(
  val type: KClass<E>,
  val handlers: MutableSet<StompXEventHandler<E>> = mutableSetOf()
) : MutableCollection<StompXEventHandler<E>> by handlers
