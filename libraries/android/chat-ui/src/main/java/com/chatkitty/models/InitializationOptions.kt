package com.chatkitty.models

import app.dvkyun.flexhybridapp.FlexType

data class InitializeOptions(val username: String,
                             val theme: String? = null,
                             val environment: String? = null,
                             val clientSpecification: ClientSpecification? = null): FlexType
