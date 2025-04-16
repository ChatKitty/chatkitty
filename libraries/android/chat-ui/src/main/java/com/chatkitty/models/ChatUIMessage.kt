package com.chatkitty.models

import app.dvkyun.flexhybridapp.FlexType

data class ChatUIMessage (
    val type: String,
    val id: String?,
    val payload: Any?
) : FlexType
