package com.chatkitty.ui.stompx.model

data class StompXPerformActionPayload(
    val destination: String,
    val body: Any? = null
)