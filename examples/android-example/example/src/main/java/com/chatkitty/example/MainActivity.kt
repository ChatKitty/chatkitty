package com.chatkitty.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.chatkitty.example.ui.theme.ChatUiTheme
import com.chatkitty.models.ApiConnection
import com.chatkitty.models.ChatUIComponents
import com.chatkitty.models.ChatUIConfiguration
import com.chatkitty.models.Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatUiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatUIViewCompose()
                }
            }
        }
    }
}
@Composable
fun ChatUIViewCompose() {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            // Initialize your ChatUIView
            ChatUIView(ctx)
                .apply {
                setChatUIConfig(
                    ChatUIConfiguration(
                        widgetId = "UWiEkKvdAaUJ1xut",
                        username = "2989c53a-d0c5-4222-af8d-fbf7b0c74ec6",
                        theme = Theme.LIGHT,
                        connection = ApiConnection("afaac908-1db3-4b5c-a7ae-c040b9684403")),
                    ChatUIComponents(onMounted = {
                        println("onMounted: $context")
                    }, onHeaderSelected = {
                        println("onHeaderSelected")
                    }, onMenuActionSelected = {
                        println("onMenuActionSelected")
                    })
                )
            }
        },
        update = { view ->
            // Update your view if needed
        }
    )
}