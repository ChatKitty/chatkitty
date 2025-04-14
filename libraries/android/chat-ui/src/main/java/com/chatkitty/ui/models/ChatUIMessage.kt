package com.chatkitty.ui.models

import app.dvkyun.flexhybridapp.FlexType
import org.json.JSONObject

data class ChatUIMessage (
    val type: String,
    val id: String?,
    val payload: Any?
) : FlexType
