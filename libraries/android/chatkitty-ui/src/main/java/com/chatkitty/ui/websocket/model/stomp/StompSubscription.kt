/*
 * Copyright (C) Howdi, Inc - All Rights Reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * This source is proprietary and confidential.
 */
package com.chatkitty.ui.websocket.model.stomp

data class StompSubscription(
  val id: String,
  val destination: String,
  val receipt: String
)
