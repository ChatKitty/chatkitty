package com.chatkitty.ui.api

import com.chatkitty.ui.models.ChatComponentContext
import com.chatkitty.ui.models.ChatUIMessage
import com.chatkitty.ui.models.InitializeOptions

/**
export interface Flex {
onChatUiConnected: () => Promise<void>
onChatMounted: (context: ChatComponentContext) => Promise<void>
onChatHeaderSelected: (channel: Channel) => Promise<void>
onChatMenuActionSelected: (action: MenuAction) => Promise<void>
onChatNotificationReceived: (notification: Notification) => Promise<void>
}
 */
interface ChatUIBridge {

    fun initialize(options: InitializeOptions)

    fun onChatUiConnected(onChatUiConnected: () -> Unit)

    fun onChatMounted(onChatMounted: (ChatComponentContext) -> Unit)

    fun onChatHeaderSelected(onChatHeaderSelected: () -> Unit)

    fun onChatMenuActionSelected(onChatMenuActionSelected: () -> Unit)

    fun onChatNotificationReceived(onChatNotificationReceived: () -> Unit)

    fun onPostMessage(onPostMessage: (ChatUIMessage) -> Unit)
}
