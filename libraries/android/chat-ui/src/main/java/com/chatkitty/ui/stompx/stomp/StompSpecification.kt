/*
 * Copyright (C) Howdi, Inc - All Rights Reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * This source is proprietary and confidential.
 */
package com.chatkitty.ui.stompx.stomp

import com.chatkitty.ui.websocket.model.stomp.AcceptVersionHeader
import com.chatkitty.ui.websocket.model.stomp.AckHeader
import com.chatkitty.ui.websocket.model.stomp.ContentLengthHeader
import com.chatkitty.ui.websocket.model.stomp.ContentTypeHeader
import com.chatkitty.ui.websocket.model.stomp.CustomHeader
import com.chatkitty.ui.websocket.model.stomp.DestinationHeader
import com.chatkitty.ui.websocket.model.stomp.HeartBeatHeader
import com.chatkitty.ui.websocket.model.stomp.HostHeader
import com.chatkitty.ui.websocket.model.stomp.IdHeader
import com.chatkitty.ui.websocket.model.stomp.ReceiptHeader
import com.chatkitty.ui.websocket.model.stomp.StompClientCommand
import com.chatkitty.ui.websocket.model.stomp.StompClientFrame
import com.chatkitty.ui.websocket.model.stomp.StompMessage
import com.chatkitty.ui.websocket.model.stomp.StompSubscription
import com.chatkitty.ui.websocket.model.stomp.StompXUserAgentHeader
import com.chatkitty.ui.websocket.model.stomp.StompXUserHeader
import java.util.UUID

class StompSpecification(
  private val heartBeatInterval: Long
) {
  fun connect(host: String,
              username: String): StompClientFrame {
    val stompHeaders = listOf(
      AcceptVersionHeader("1.1,1.2"),
      HostHeader(host),
      HeartBeatHeader("$heartBeatInterval,$heartBeatInterval"),
      StompXUserHeader(username),
      StompXUserAgentHeader("ChatKitty-Android/1.0.0")
    )

    return StompClientFrame(StompClientCommand.CONNECT, stompHeaders)
  }

  fun subscribe(subscription: StompSubscription, headers: Map<String, String> = emptyMap()): StompClientFrame {
    val stompHeaders = with(subscription) {
      mutableListOf(
        IdHeader(id),
        DestinationHeader(destination),
        ReceiptHeader(receipt),
        AckHeader("client-individual")
      )
    }

    headers.forEach { (name, value) ->
      stompHeaders += CustomHeader(name, value)
    }

    return StompClientFrame(StompClientCommand.SUBSCRIBE, stompHeaders)
  }

  fun sendJsonMessage(message: StompMessage, headers: Map<String, String> = emptyMap()): StompClientFrame {
    val stompHeaders = with(message) {
      mutableListOf(
        DestinationHeader(destination),
        ContentLengthHeader(this.payload.size),
        ReceiptHeader(receipt),
        ContentTypeHeader("application/json;charset=UTF-8")
      )
    }

    headers.forEach { (name, value) ->
      stompHeaders += CustomHeader(name, value)
    }

    return StompClientFrame(StompClientCommand.SEND, stompHeaders, String(message.payload))
  }

  fun unsubscribe(subscription: StompSubscription): StompClientFrame {
    val stompHeaders = listOf(
      IdHeader(subscription.id)
    )

    return StompClientFrame(StompClientCommand.UNSUBSCRIBE, stompHeaders)
  }

  fun ack(messageId: String): StompClientFrame {
    val stompHeaders = listOf(
      IdHeader(messageId)
    )

    return StompClientFrame(StompClientCommand.ACK, stompHeaders)
  }

  fun disconnect(receipt: String): StompClientFrame {
    val stompHeaders = listOf(
      ReceiptHeader(receipt)
    )

    return StompClientFrame(StompClientCommand.DISCONNECT, stompHeaders)
  }

  fun generateSubscriptionId(): String =
    "subscription-id-${UUID.randomUUID()}"

  fun generateHeartBeat(): String = "\n\n"
}
