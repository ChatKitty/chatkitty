package app.dvkyun.flexhybridand

import app.dvkyun.flexhybridand.forjava.*
import kotlinx.coroutines.CoroutineScope
import kotlin.reflect.KClass

open class FlexInterfaces {

    internal val interfaces: HashMap<String, Pair<KClass<*>, suspend CoroutineScope.(data: Any) -> Any?>> =
        HashMap()
    internal val actions: HashMap<String, Pair<KClass<*>, suspend CoroutineScope.(action: FlexAction, data: Any) -> Unit>> =
        HashMap()
    internal val iTimeouts: HashMap<String, Int> = HashMap()

    private fun vailInterface(name: String, timeout: Int? = null) {
        if (interfaces[name] != null || actions[name] != null) throw FlexException(FlexException.ERROR7)
        if (name.contains(FlexWebView.FLEX)) throw FlexException(FlexException.ERROR8)
        timeout?.also {
            iTimeouts[name] = if (it != 0 && it < 100) 100 else it
        }
    }

    fun <T : FlexType, R> typeInterface(
        name: String,
        tClazz: KClass<T>,
        timeout: Int? = null,
        lambda: suspend CoroutineScope.(T) -> R?
    ): FlexInterfaces {
        vailInterface(name, timeout)
        interfaces[name] = Pair(tClazz, lambda as suspend CoroutineScope.(Any) -> Any?)
        return this
    }

    inline fun <reified T : FlexType, R> typeInterface(
        name: String,
        timeout: Int? = null,
        noinline lambda: suspend CoroutineScope.(T) -> R?
    ): FlexInterfaces {
        return typeInterface(name, T::class, timeout, lambda)
    }

    fun <R> setInterface(
        name: String,
        timeout: Int? = null,
        lambda: suspend CoroutineScope.(FlexArguments) -> R?
    ): FlexInterfaces {
        return typeInterface(name, FlexArguments::class, timeout, lambda)
    }

    fun <T : FlexType> typeAction(
        name: String,
        tClazz: KClass<T>,
        timeout: Int? = null,
        action: suspend CoroutineScope.(action: FlexAction, arguments: T) -> Unit
    ): FlexInterfaces {
        vailInterface(name, timeout)
        actions[name] = Pair(tClazz, action as suspend CoroutineScope.(FlexAction, Any) -> Unit)
        return this
    }

    inline fun <reified T : FlexType> typeAction(
        name: String,
        timeout: Int? = null,
        noinline action: suspend CoroutineScope.(action: FlexAction, arguments: T) -> Unit
    ): FlexInterfaces {
        return typeAction(name, T::class, timeout, action)
    }

    fun setAction(
        name: String,
        timeout: Int? = null,
        action: suspend CoroutineScope.(action: FlexAction, arguments: FlexArguments) -> Unit
    ): FlexInterfaces {
        return typeAction(name, FlexArguments::class, timeout, action)
    }

    fun setInterfaceForJava(name: String, timeout: Int?, invoke: InvokeFlexVoid): FlexInterfaces {
        return typeInterface(name, FlexArguments::class, timeout) { args -> invoke.invoke(args) }
    }

    fun <R> setInterfaceForJava(
        name: String,
        timeout: Int?,
        invoke: InvokeFlex<R>
    ): FlexInterfaces {
        return typeInterface(name, FlexArguments::class, timeout) { args -> invoke.invoke(args) }
    }

    fun setActionForJava(name: String, timeout: Int?, invoke: InvokeAction): FlexInterfaces {
        return typeAction(name, FlexArguments::class, timeout) { ac, args ->
            invoke.invoke(
                ac,
                args
            )
        }
    }

}