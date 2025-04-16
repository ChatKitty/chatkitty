/*
 * Copyright (C) Howdi, Inc - All Rights Reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * This source is proprietary and confidential.
 */
package com.chatkitty.websocket.model.stomp

enum class StompClientCommand {
  SEND, SUBSCRIBE, UNSUBSCRIBE, BEGIN, COMMIT, ABORT, ACK, NACK, DISCONNECT, CONNECT, STOMP
}

enum class StompServerCommand {
  CONNECTED, MESSAGE, RECEIPT, ERROR
}
