package app.dvkyun.flexhybridapp.forjava

import app.dvkyun.flexhybridapp.FlexEvent
import app.dvkyun.flexhybridapp.FlexWebView

interface FlexListenerForJava {
    fun onEvent(view: FlexWebView, type: FlexEvent, url: String, funcName: String, msg: String?)
}