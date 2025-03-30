package app.dvkyun.flexhybridand

import org.json.JSONArray
import org.json.JSONObject

class FlexAction internal constructor(name: String, webView: FlexWebView) {

    private var funName: String = name
    private var flexWebView: FlexWebView = webView

    var isFinished = false
        private set

    var onFinished: (() -> Unit)? = null

    private fun pReturn(response: Any?) {
        if (isFinished) {
            FlexUtil.INFO(FlexException.ERROR9)
            return
        }
        if (response is BrowserException) {
            val reason = if (response.reason == null) null else "\"${response.reason}\""
            FlexUtil.rejectPromise(flexWebView, funName, reason)
        } else if (response == null || response is Unit || response is Void) {
            FlexUtil.responsePromise(flexWebView, funName)
        } else {
            FlexUtil.responsePromise(flexWebView, funName, response)
        }
        finish()
    }

    fun promiseReturn(response: String) {
        pReturn(response)
    }

    fun promiseReturn(response: Int) {
        pReturn(response)
    }

    fun promiseReturn(response: Long) {
        pReturn(response)
    }

    fun promiseReturn(response: Float) {
        pReturn(response)
    }

    fun promiseReturn(response: Double) {
        pReturn(response)
    }

    fun promiseReturn(response: Char) {
        pReturn(response)
    }

    fun promiseReturn(response: Boolean) {
        pReturn(response)
    }

    fun promiseReturn(response: JSONArray) {
        pReturn(response)
    }

    fun promiseReturn(response: Array<*>) {
        pReturn(response)
    }

    fun promiseReturn(response: Iterable<*>) {
        pReturn(response)
    }

    fun promiseReturn(response: JSONObject) {
        pReturn(response)
    }

    fun promiseReturn(response: Map<String, *>) {
        pReturn(response)
    }

    fun <T : FlexType> promiseReturn(response: T) {
        pReturn(response)
    }

    fun promiseReturn(response: Unit) {
        pReturn(response)
    }

    fun promiseReturn(response: Void) {
        pReturn(response)
    }

    fun promiseReturn(response: BrowserException) {
        pReturn(response)
    }

    fun promiseReturn() {
        resolveVoid()
    }

    fun resolveVoid() {
        if (isFinished) {
            FlexUtil.INFO(FlexException.ERROR9)
            return
        }
        FlexUtil.responsePromise(flexWebView, funName)
        finish()
    }

    fun reject(reason: BrowserException) {
        if (isFinished) {
            FlexUtil.INFO(FlexException.ERROR9)
            return
        }
        val rejectReason = if (reason.reason == null) null else "\"${reason.reason}\""
        FlexUtil.rejectPromise(flexWebView, funName, rejectReason)
        finish()
    }

    fun reject(reason: String) {
        if (isFinished) {
            FlexUtil.INFO(FlexException.ERROR9)
            return
        }
        FlexUtil.rejectPromise(flexWebView, funName, reason)
        finish()
    }

    fun reject() {
        if (isFinished) {
            FlexUtil.INFO(FlexException.ERROR9)
            return
        }
        FlexUtil.rejectPromise(flexWebView, funName)
        finish()
    }

    private fun finish() {
        isFinished = true
        onFinished?.invoke()
    }

}
