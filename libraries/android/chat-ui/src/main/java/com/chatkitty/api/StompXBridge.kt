package com.chatkitty.api

import com.chatkitty.stompx.model.FlexStompXEventType

interface StompXBridge {
    // Assuming T can be any type that is @Serializable
    fun <T: Any> onMessage(id: String?, type: FlexStompXEventType, payload: T)

    fun onMessage(id: String?, type: FlexStompXEventType)
}
