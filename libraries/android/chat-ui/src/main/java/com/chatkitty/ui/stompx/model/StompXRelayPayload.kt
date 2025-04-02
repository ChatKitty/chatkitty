package com.chatkitty.ui.stompx.model
data class StompXRelayPayload(
    val parameters: Map<String, Boolean>? = null,
    val destination: String
)
