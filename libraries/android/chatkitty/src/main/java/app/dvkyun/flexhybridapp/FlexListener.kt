package app.dvkyun.flexhybridapp

import java.util.*

class FlexListener(internal val listener: (view: FlexWebView, type: FlexEvent, url: String, funcName: String, msg: String?) -> Unit) {
    internal val id: UUID = UUID.randomUUID()
}