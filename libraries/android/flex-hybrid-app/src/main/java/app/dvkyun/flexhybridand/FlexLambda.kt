package app.dvkyun.flexhybridand

import kotlinx.coroutines.CoroutineScope

class FlexLambda {
    companion object {
        fun <T : FlexType> void(lambda: suspend CoroutineScope.(T) -> Unit): suspend CoroutineScope.(T) -> Unit =
            lambda

        fun <T : FlexType> int(lambda: suspend CoroutineScope.(T) -> Int?): suspend CoroutineScope.(T) -> Int? =
            lambda

        fun <T : FlexType> long(lambda: suspend CoroutineScope.(T) -> Long?): suspend CoroutineScope.(T) -> Long? =
            lambda

        fun <T : FlexType> float(lambda: suspend CoroutineScope.(T) -> Float?): suspend CoroutineScope.(T) -> Float? =
            lambda

        fun <T : FlexType> double(lambda: suspend CoroutineScope.(T) -> Double?): suspend CoroutineScope.(T) -> Double? =
            lambda

        fun <T : FlexType> char(lambda: suspend CoroutineScope.(T) -> Char?): suspend CoroutineScope.(T) -> Char? =
            lambda

        fun <T : FlexType> string(lambda: suspend CoroutineScope.(T) -> String?): suspend CoroutineScope.(T) -> String? =
            lambda

        fun <T : FlexType> bool(lambda: suspend CoroutineScope.(T) -> Boolean?): suspend CoroutineScope.(T) -> Boolean? =
            lambda

        fun <T : FlexType> array(lambda: suspend CoroutineScope.(T) -> Array<*>?): suspend CoroutineScope.(T) -> Array<*>? =
            lambda

        fun <T : FlexType> list(lambda: suspend CoroutineScope.(T) -> Iterable<*>?): suspend CoroutineScope.(T) -> Iterable<*>? =
            lambda

        fun <T : FlexType> map(lambda: suspend CoroutineScope.(T) -> Map<String, *>?): suspend CoroutineScope.(T) -> Map<String, *>? =
            lambda

        fun <T : FlexType, R : FlexType> model(lambda: suspend CoroutineScope.(T) -> R?): suspend CoroutineScope.(T) -> R? =
            lambda

        fun <T : FlexType> action(lambda: suspend CoroutineScope.(action: FlexAction, arguments: T) -> Unit): suspend CoroutineScope.(action: FlexAction, arguments: T) -> Unit =
            lambda
    }
}