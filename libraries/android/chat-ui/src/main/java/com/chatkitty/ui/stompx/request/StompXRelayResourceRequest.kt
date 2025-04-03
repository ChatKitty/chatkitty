package com.chatkitty.ui.stompx.request

import com.chatkitty.ui.stompx.model.StompXError
import kotlin.reflect.KClass

internal data class StompXRelayResourceRequest<R : Any>(
  val destination: String,
  val type: KClass<R>,
  val embeddedType: KClass<*>? = null,
  val parameters: Map<String, Any?>? = null,
  val onSuccess: (R) -> Unit,
  val onError: ((StompXError) -> Unit)? = null
)
