package com.chatkitty.ui.stompx.request

import com.chatkitty.ui.stompx.model.StompXError
import kotlin.reflect.KClass

data class StompXPerformActionRequest<R: Any>(
  val destination: String,
  val body: Any?,
  val type: KClass<R>,
  val embeddedType: KClass<*>? = null,
  val onSent: (() -> Unit)? = null,
  val onSuccess: ((R) -> Unit)? = null,
  val onError: ((StompXError) -> Unit)? = null
)
