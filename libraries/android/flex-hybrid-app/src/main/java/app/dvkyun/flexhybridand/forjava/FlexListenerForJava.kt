package app.dvkyun.flexhybridand.forjava

import app.dvkyun.flexhybridand.FlexEvent
import app.dvkyun.flexhybridand.FlexWebView

interface FlexListenerForJava {
    fun onEvent(view: FlexWebView, type: FlexEvent, url: String, funcName: String, msg: String?)
}