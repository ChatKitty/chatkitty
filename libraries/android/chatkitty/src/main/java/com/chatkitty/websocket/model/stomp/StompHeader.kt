/*
 * Copyright (C) Howdi, Inc - All Rights Reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * This source is proprietary and confidential.
 */
package com.chatkitty.websocket.model.stomp

sealed class StompHeader(
  open val name: String
)

val StompHeader.value: String
  get() = when (this) {
    is ContentLengthHeader -> length.toString()
    is ContentTypeHeader -> type
    is ReceiptHeader -> receipt
    is AcceptVersionHeader -> version
    is HostHeader -> host
    is LoginHeader -> login
    is PasscodeHeader -> passcode
    is HeartBeatHeader -> value
    is VersionHeader -> version
    is SessionHeader -> session
    is ServerHeader -> server
    is DestinationHeader -> destination
    is TransactionHeader -> transaction
    is IdHeader -> id
    is AckHeader -> ack
    is MessageIdHeader -> id
    is SubscriptionHeader -> id
    is ReceiptIdHeader -> id
    is CustomHeader -> value
    is StompXUserHeader -> value
    is StompXUserAgentHeader -> value
  }

fun stompHeader(name: String, value: String): StompHeader =
  when (name) {
    "content-length" -> ContentLengthHeader(value.toInt())
    "content-type" -> ContentTypeHeader(value)
    "receipt" -> ReceiptHeader(value)
    "accept-version" -> AcceptVersionHeader(value)
    "host" -> HostHeader(value)
    "login" -> LoginHeader(value)
    "passcode" -> PasscodeHeader(value)
    "heart-beat" -> HeartBeatHeader(value)
    "version" -> VersionHeader(value)
    "session" -> SessionHeader(value)
    "server" -> ServerHeader(value)
    "destination" -> DestinationHeader(value)
    "transaction" -> TransactionHeader(value)
    "id" -> IdHeader(value)
    "ack" -> AckHeader(value)
    "message-id" -> MessageIdHeader(value)
    "subscription" -> SubscriptionHeader(value)
    "receipt-id" -> ReceiptIdHeader(value)
    else -> CustomHeader(name, value)
  }

data class ContentLengthHeader(
  val length: Int
) : StompHeader("content-length")

data class ContentTypeHeader(
  val type: String
) : StompHeader("content-type")

data class ReceiptHeader(
  val receipt: String
) : StompHeader("receipt")

data class AcceptVersionHeader(
  val version: String
) : StompHeader("accept-version")

data class HostHeader(
  val host: String
) : StompHeader("host")

data class LoginHeader(
  val login: String
) : StompHeader("login")

data class PasscodeHeader(
  val passcode: String
) : StompHeader("passcode")

data class HeartBeatHeader(
  val value: String
) : StompHeader("heart-beat")

data class StompXUserHeader(
  val value: String
) : StompHeader("StompX-User")

data class VersionHeader(
  val version: String
) : StompHeader("version")

data class StompXUserAgentHeader(
  val value: String
) : StompHeader("StompX-User-Agent")
data class SessionHeader(
  val session: String
) : StompHeader("session")

data class ServerHeader(
  val server: String
) : StompHeader("server")

data class DestinationHeader(
  val destination: String
) : StompHeader("destination")

data class TransactionHeader(
  val transaction: String
) : StompHeader("transaction")

data class IdHeader(
  val id: String
) : StompHeader("id")

data class AckHeader(
  val ack: String
) : StompHeader("ack")

data class MessageIdHeader(
  val id: String
) : StompHeader("message-id")

data class SubscriptionHeader(
  val id: String
) : StompHeader("subscription")

data class ReceiptIdHeader(
  val id: String
) : StompHeader("receipt-id")

data class CustomHeader(
  override val name: String,
  val value: String
) : StompHeader(name)
