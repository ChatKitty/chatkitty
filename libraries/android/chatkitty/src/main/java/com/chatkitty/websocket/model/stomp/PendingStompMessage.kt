/*
 * Copyright (C) Howdi, Inc - All Rights Reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * This source is proprietary and confidential.
 */
package com.chatkitty.websocket.model.stomp

import io.reactivex.CompletableEmitter
import io.reactivex.SingleEmitter
import java.util.concurrent.atomic.AtomicInteger

sealed class PendingStompMessage : Comparable<Long> {
  private val sentTime: Long = System.currentTimeMillis()

  val retryCount: AtomicInteger = AtomicInteger()

  abstract val message: StompMessage

  override fun compareTo(other: Long): Int =
    sentTime.compareTo(other)
}

data class CompletablePendingStompMessage(
  val emitter: CompletableEmitter,
  override val message: StompMessage
) : PendingStompMessage()

data class SinglePendingStompMessage(
  val emitter: SingleEmitter<String>,
  override val message: StompMessage
) : PendingStompMessage()
