package com.chatkitty.ui.models

import app.dvkyun.flexhybridand.FlexType

data class InitializeOptions(val username: String,
                             val theme: String? = null,
                             val environment: String? = null,
                             val clientSpecification: ClientSpecification? = null): FlexType
