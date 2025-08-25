package com.chatkitty.stompx.model

data class StompXPerformActionPayload(
    val destination: String,
    val body: Any? = null
)