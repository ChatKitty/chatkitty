package com.chatkitty.ui.models

import app.dvkyun.flexhybridapp.FlexType

data class ChatUIConfiguration(
    val widgetId: String,
    val username: String,
    val locale: String? = null,
    val container: ChatUiContainer? = null,
    val theme: Theme? = null,
    val profile: UserProfile? = null,
    val connection: ApiConnection? = null
)

data class ApiConnection(val apiKey: String)

enum class Theme(private val value: String) {
    LIGHT("light"),

    DARK("dark");

    override fun toString() = value
}

data class ChatUiContainer(
    var id: String? = null,
    var height: String? = null,
    var width: String? = null
): FlexType

data class UserProfile(
    val displayName: String,
    val displayPicture: String
): FlexType

class ChatUIComponents(
    val onMounted: ((ChatComponentContext) -> Unit)? = null,
    val onHeaderSelected: (() -> Unit)? = null,
    val onMenuActionSelected: (() -> Unit)? = null
)