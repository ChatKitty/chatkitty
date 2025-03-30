package com.chatkitty.ui.stompx.model

import app.dvkyun.flexhybridand.FlexType
import org.json.JSONObject

data class FlexStompXMessage<T>(
    val id: String?,
    val type: String,
    val payload: T,
    val payloadBase64: String?
) : FlexType

data class FlexStompXEmptyMessage(
    val id: String?,
    val type: String
) : JSONObject()

enum class FlexStompXEventType(val value: String) {
    CONNECT_SUCCESS("stompx:connect.success"),
    CONNECT_FAILURE("stompx:connect.error"),
    RELAY_SUCCESS("stompx:relay.success"),
    RELAY_ERROR("stompx:relay.error"),
    EVENT_PUBLISHED("stompx:event.published"),
    TOPIC_SUBSCRIBED("stompx:topic.subscribed"),
    ACTION_SENT("stompx:action.sent"),
    ACTION_SUCCESS("stompx:action.success"),
    ACTION_ERROR("stompx:action.error"),
    STREAM_SUCCESS("stompx:stream.success"),
    STREAM_ERROR("stompx:stream.error"),
    STREAM_PROGRESS_STARTED("stompx:stream.progress.started"),
    STREAM_PROGRESS_PUBLISHED("stompx:stream.progress.published"),
    STREAM_PROGRESS_COMPLETED("stompx:stream.progress.completed"),
    STREAM_PROGRESS_FAILED("stompx:stream.progress.failed"),
    STREAM_PROGRESS_CANCELLED("stompx:stream.progress.cancelled");

    companion object {
        // Function to get the enum type by string value
        fun fromValue(value: String): FlexStompXEventType? = entries.find { it.value == value }
    }
}
