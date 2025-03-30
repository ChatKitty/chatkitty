/*
 * Copyright (C) Howdi, Inc - All Rights Reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * This source is proprietary and confidential.
 */
package com.chatkitty.ui.websocket.model.stomp

data class StompMessage(
  val destination: String,
  val receipt: String,
  val payload: ByteArray
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as StompMessage

    if (destination != other.destination) return false
    if (!payload.contentEquals(other.payload)) return false
    if (receipt != other.receipt) return false

    return true
  }

  override fun hashCode(): Int {
    var result = destination.hashCode()
    result = 31 * result + payload.contentHashCode()
    result = 31 * result + receipt.hashCode()
    return result
  }
}
