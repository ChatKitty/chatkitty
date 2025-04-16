package com.chatkitty.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import app.dvkyun.flexhybridapp.FlexWebView
import com.chatkitty.R
import com.chatkitty.api.ChatUIBridge
import com.chatkitty.api.StompXBridge
import com.chatkitty.library.ChatUIStompXInteractor
import com.chatkitty.library.FlexChatUIBridge
import com.chatkitty.library.FlexStompXBridge
import com.chatkitty.models.ChatUIComponents
import com.chatkitty.models.ChatUIConfiguration
import com.chatkitty.models.ClientSpecification
import com.chatkitty.models.InitializeOptions
import com.chatkitty.stompx.StompX
import com.chatkitty.stompx.StompXConfiguration
import com.chatkitty.stompx.StompXImpl
import com.chatkitty.stompx.request.StompXConnectRequest

class ChatUIView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private val flexWebView: FlexWebView
    private val flexChatUIBridge: ChatUIBridge
    private val stompXBridge: StompXBridge
    private var chatUIConfig: ChatUIConfiguration? = null
    private var chatUIStompXInteractor: ChatUIStompXInteractor? = null
    private var stompX: StompX? = null

    fun setChatUIConfig(config: ChatUIConfiguration,
                        components: ChatUIComponents? = null) {
        this.chatUIConfig = config
        val connection = if (config.connection != null) {
            "shared"
        } else {
            "standalone"
        }
        val options = InitializeOptions(
            username = config.username,
            theme = config.theme.toString(),
            clientSpecification = ClientSpecification(connection = connection)
        )
        flexChatUIBridge.onChatUiConnected {
            flexChatUIBridge.initialize(options)
        }

        flexChatUIBridge.onChatMounted {
            components?.onMounted?.invoke(it)
        }

        flexChatUIBridge.onChatHeaderSelected {
            components?.onHeaderSelected?.invoke()
        }

        flexChatUIBridge.onChatMenuActionSelected {
            components?.onMenuActionSelected?.invoke()
        }

        flexChatUIBridge.onPostMessage { message ->
            chatUIStompXInteractor?.onReceiveMessage(message)
        }

        flexWebView.baseUrl = "https://ui.chatkitty.com/chat"
        flexWebView.loadUrl("https://ui.chatkitty.com/chat?widget_id=" + config.widgetId)

        stompX = StompXImpl(StompXConfiguration(
            host = "api.chatkitty.com",
            username = config.username,
            isSecure = true,
            isDebug = true,
        ))

        stompX?.let {
            chatUIStompXInteractor = ChatUIStompXInteractor(it, stompXBridge)
        }

        if (config.connection != null) {
            stompX?.connect(
                StompXConnectRequest(
                    apiKey = config.connection.apiKey,
                    authParams = null,
                    onConnected = {
                        println("Connected")
                    },
                    onError = {
                        println("Error")
                    })
            )
        }
    }

    init {
        orientation = VERTICAL
        LayoutInflater.from(context).inflate(R.layout.chat_ui_view, this, true)
        flexWebView = findViewById(R.id.flexWebView)
        flexChatUIBridge = FlexChatUIBridge(flexWebView)
        stompXBridge = FlexStompXBridge(flexWebView)
    }
}