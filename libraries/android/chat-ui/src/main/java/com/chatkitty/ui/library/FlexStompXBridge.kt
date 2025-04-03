package com.chatkitty.ui.library

import app.dvkyun.flexhybridand.FlexWebView
import com.chatkitty.ui.api.StompXBridge
import com.chatkitty.ui.stompx.model.FlexStompXEmptyMessage
import com.chatkitty.ui.stompx.model.FlexStompXEventType
import com.chatkitty.ui.stompx.model.FlexStompXMessage
import com.fasterxml.jackson.databind.ObjectMapper
import android.util.Base64
import android.util.Log

class FlexStompXBridge(private val flexWebView: FlexWebView) : StompXBridge {
    private val mapper = ObjectMapper()

    override fun <T : Any> onMessage(id: String?, type: FlexStompXEventType, payload: T) {
        flexWebView.evalFlexFunc("onMessage", messageToBase64(FlexStompXMessage(id, type.value, payload)))
    }

    override fun onMessage(id: String?, type: FlexStompXEventType) {
        flexWebView.evalFlexFunc("onMessage", FlexStompXEmptyMessage(id, type.value))
    }

    private fun messageToBase64(message: FlexStompXMessage<*>): String {
        // Convert the JSON object to a string
        val jsonString = mapper.writeValueAsString(message)

        // Convert the JSON string to bytes using UTF-8 encoding
        val jsonData = jsonString.toByteArray(Charsets.UTF_8)

        // Encode the byte array to Base64
        return Base64.encodeToString(jsonData, Base64.DEFAULT)
    }
}
