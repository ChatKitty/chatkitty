package com.chatkitty.models

import app.dvkyun.flexhybridapp.FlexType

data class ConnectPayload(
    val user: Any,
    val write: String?,
    val read: String?
) : FlexType