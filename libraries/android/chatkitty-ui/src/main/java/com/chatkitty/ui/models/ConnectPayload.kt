package com.chatkitty.ui.models

import app.dvkyun.flexhybridand.FlexType

data class ConnectPayload(
    val user: Any,
    val write: String?,
    val read: String?
) : FlexType