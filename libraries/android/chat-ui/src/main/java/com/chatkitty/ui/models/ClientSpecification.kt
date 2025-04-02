package com.chatkitty.ui.models

import app.dvkyun.flexhybridand.FlexType

data class ClientSpecification(
    val version: String,
    val environment: String,
    val connection: String
) : FlexType {
    constructor(connection: String) : this(
        version = "1.0.0",
        environment = "production",
        connection = connection
    )
}