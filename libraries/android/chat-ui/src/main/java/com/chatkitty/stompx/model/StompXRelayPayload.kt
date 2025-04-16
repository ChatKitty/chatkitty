package com.chatkitty.stompx.model
data class StompXRelayPayload(
    val parameters: Map<String, Boolean>? = null,
    val destination: String
)
