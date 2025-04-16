package com.chatkitty.stompx.model

data class StompXListenForEventPayload(val topic: String, val event: String)