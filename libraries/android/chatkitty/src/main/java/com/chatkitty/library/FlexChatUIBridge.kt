package com.chatkitty.library

import app.dvkyun.flexhybridapp.FlexWebView
import com.chatkitty.api.ChatUIBridge
import com.chatkitty.models.ChatComponentContext
import com.chatkitty.models.ChatUIMessage
import com.chatkitty.models.InitializeOptions

class FlexChatUIBridge(private val flexWebView: FlexWebView) : ChatUIBridge {

    // ChatUIBridge implementation

    override fun initialize(options: InitializeOptions) {
        flexWebView.evalFlexFunc("initialize", options)
    }

    override fun onChatUiConnected(onChatUiConnected: () -> Unit) {
        flexWebView.setInterface("onChatUiConnected") { _ ->
            onChatUiConnected()
        }
    }

    override fun onChatMounted(onChatMounted: (ChatComponentContext) -> Unit) {
        flexWebView.typeInterface("onChatMounted", ChatComponentContext::class) { context ->
            onChatMounted(context)
        }
    }

    override fun onChatHeaderSelected(onChatHeaderSelected: () -> Unit) {
        flexWebView.typeInterface("onChatHeaderSelected", io.swagger.client.models.Channel::class) { context ->
            onChatHeaderSelected()
        }
    }

    override fun onChatMenuActionSelected(onChatMenuActionSelected: () -> Unit) {
        flexWebView.setInterface("onChatMenuActionSelected") { args ->
            onChatMenuActionSelected()
        }
    }

    override fun onChatNotificationReceived(onChatNotificationReceived: () -> Unit) {
        flexWebView.setInterface("onChatNotificationReceived") { args ->
            onChatNotificationReceived()
        }
    }

    override fun onPostMessage(onPostMessage: (ChatUIMessage) -> Unit) {
        flexWebView.typeInterface("postMessage", ChatUIMessage::class) { args ->
            onPostMessage(args)
        }
    }
}
