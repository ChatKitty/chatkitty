package app.dvkyun.flexhybridand

import java.lang.Exception

class FlexException : Exception {

    constructor(msg: String) : super(msg)
    constructor(e: Throwable) : super(e)
    constructor(msg: String, e: Throwable) : super(msg, e)

    companion object {
        const val ERROR1 = "Context must be Activity Context."
        const val ERROR2 = "ViewClient or ChromeClient must be FlexViewClient or FlexChromeClient"
        const val ERROR3 =
            "Only Web interfaces with Null, Int, Double, Float, Boolean, Char, String, JSONObject, JSONArray, Array<Any>(Object[]), Iterable<Any>, and Map<String,Any> data are available."
        const val ERROR4 = "FlexWebView is null."
        const val ERROR5 = "BaseUrl cannot be changed after initialization."
        const val ERROR6 =
            "The interfaces and options can only be added or changed before the first page is loaded."
        const val ERROR7 = "An interface or action with the same name has already been added."
        const val ERROR8 = "Interface or task name cannot be set to 'flex'."
        const val ERROR9 = "PromiseReturn cannot be called twice in a single FlexAction."
        const val ERROR10 = "FlexFuncInterface must contain FlexArguments."
        const val ERROR11 = "FlexActionInterface must contain FlexArguments and FlexAction."
        const val ERROR12 = "FlexFuncInterface and FlexActionInterface must be public."
        const val ERROR13 = "FlexWebView is destroyed."
        const val ERROR14 = "The Unique interface cannot be removed."
        const val ERROR15 = "The saved data and the called type do not match."
        const val ERROR16 = "interface does not exist."
        const val ERROR17 =
            "Only FlexArguments classes or classes that inherited FlexDataClass annotations can be used as Arguments."
    }
}