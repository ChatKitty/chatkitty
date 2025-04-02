package app.dvkyun.flexhybridand

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import android.os.Build
import android.webkit.WebView
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.Reader
import java.lang.reflect.ParameterizedType
import java.util.regex.PatternSyntaxException
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.*
import kotlin.reflect.jvm.javaType

internal object FlexUtil {

    internal fun getActivity(context: Context): Activity? {
        if (context is ContextWrapper) {
            return if (context is Activity) {
                context
            } else {
                getActivity(context.baseContext)
            }
        }
        return null
    }

    internal fun evaluateJavaScript(webView: WebView?, javascript: String) {
        if (webView == null) {
            throw FlexException(FlexException.ERROR4)
        }
        webView.post {
            val js = "javascript:$javascript; void 0;"
            webView.evaluateJavascript(js, null)
        }
    }

    internal fun responsePromise(webView: WebView?, funName: String, response: Any? = null) {
        if (webView == null) {
            throw FlexException(FlexException.ERROR4)
        }
        webView.post {
            try {
                val eval =
                    if (response != null) "\$flex.flex.$funName(true, null, ${convertInput(response)})"
                    else "\$flex.flex.$funName(true)"
                val js =
                    "javascript:if(typeof \$flex.flex.$funName === 'function'){ $eval }; void 0;"
                webView.evaluateJavascript(js, null)
            } catch (e: Exception) {
                ERR(e)
                rejectPromise(webView, funName, e.cause?.message)
                e.printStackTrace()
            }
        }
    }

    internal fun rejectPromise(webView: WebView?, funName: String, reason: String? = null) {
        if (webView == null) {
            throw FlexException(FlexException.ERROR4)
        }
        webView.post {
            val eval = if (reason != null) "\$flex.flex.$funName(false, `$reason`)"
            else "\$flex.flex.$funName(false)"
            val js = "javascript:if(typeof \$flex.flex.$funName === 'function'){ $eval }; void 0;"
            webView.evaluateJavascript(js, null)
        }
    }

    internal fun convertJSONArray(value: JSONArray): Array<Any?> {
        return Array(value.length())
        { i ->
            val element = value[i]
            if (element is Int || element is Double || element is Boolean || element is String) {
                element
            } else if (element is JSONArray) {
                convertJSONArray(element)
            } else if (element is JSONObject) {
                convertJSONObject(element)
            } else if (element == null) {
                null
            } else {
                throw FlexException(FlexException.ERROR3)
            }
        }
    }

    internal fun convertJSONObject(value: JSONObject): Map<String, Any?> {
        val result = HashMap<String, Any?>()
        value.keys().forEach {
            if (value.isNull(it)) {
                result[it] = null
            } else {
                val element = value[it]
                if (element is Int || element is Double || element is Boolean || element is String) {
                    result[it] = element
                } else if (element is JSONArray) {
                    result[it] = convertJSONArray(element)
                } else if (element is JSONObject) {
                    result[it] = convertJSONObject(element)
                } else {
                    throw FlexException(FlexException.ERROR3)
                }
            }
        }
        return result
    }

    internal fun convertInput(value: Any?): String {
        return if (value is Int || value is Long || value is Double || value is Float || value is Boolean) {
            "$value"
        } else if (value is String || value is Char) {
            "`${value}`"
        } else if (value is Array<*>) {
            val vString = StringBuilder()
            vString.append("[")
            value.forEach {
                if (it == null) {
                    vString.append("null,")
                } else if (it is Int || it is Long || it is Double || it is Float || it is Boolean) {
                    vString.append("${it},")
                } else if (it is String || it is Char) {
                    vString.append("`${it}`,")
                } else if (it is Array<*> || it is Iterable<*> || it is Map<*, *> || it is JSONArray || it is JSONObject) {
                    vString.append("${convertInput(it)},")
                } else if (it is FlexType) {
                    vString.append("${convertInput(objectToMap(it))},")
                } else {
                    throw FlexException(FlexException.ERROR3)
                }
            }
            vString.append("]")
            vString.toString()
        } else if (value is Iterable<*>) {
            val vString = StringBuilder()
            vString.append("[")
            value.forEach {
                if (it == null) {
                    vString.append("null,")
                } else if (it is Int || it is Long || it is Double || it is Float || it is Boolean) {
                    vString.append("${it},")
                } else if (it is String || it is Char) {
                    vString.append("`${it}`,")
                } else if (it is Array<*> || it is Iterable<*> || it is Map<*, *> || it is JSONArray || it is JSONObject) {
                    vString.append("${convertInput(it)},")
                } else if (it is FlexType) {
                    vString.append("${convertInput(objectToMap(it))},")
                } else {
                    throw FlexException(FlexException.ERROR3)
                }
            }
            vString.append("]")
            vString.toString()
        } else if (value is JSONArray) {
            val vString = StringBuilder()
            vString.append("[")
            for (i in 0 until value.length()) {
                val element = value[i]
                if (element == null) {
                    vString.append("null,")
                } else if (element is Int || element is Long || element is Double || element is Float || element is Boolean) {
                    vString.append("${element},")
                } else if (element is String || element is Char) {
                    vString.append("`${element}`,")
                } else if (element is Array<*> || element is Iterable<*> || element is Map<*, *> || element is JSONArray || element is JSONObject) {
                    vString.append("${convertInput(element)},")
                } else if (element is FlexType) {
                    vString.append("${convertInput(objectToMap(element))},")
                } else {
                    throw FlexException(FlexException.ERROR3)
                }
            }
            vString.append("]")
            vString.toString()
        } else if (value is Map<*, *>) {
            val vString = StringBuilder()
            vString.append("{")
            value.forEach {
                val localKey = it.key
                val localValue = it.value
                if (localKey !is String) {
                    throw FlexException(FlexException.ERROR3)
                }
                if (localValue == null) {
                    vString.append("${localKey}: null,")
                } else if (localValue is Int || localValue is Long || localValue is Double || localValue is Float || localValue is Boolean) {
                    vString.append("${localKey}:${localValue},")
                } else if (localValue is String || localValue is Char) {
                    vString.append("${localKey}:`${localValue}`,")
                } else if (localValue is Array<*> || localValue is Iterable<*> || localValue is Map<*, *> || localValue is JSONArray || localValue is JSONObject) {
                    vString.append("${localKey}:${convertInput(localValue)},")
                } else if (localValue is FlexType) {
                    vString.append("${localKey}:${convertInput(objectToMap(localValue))},")
                } else {
                    throw FlexException(FlexException.ERROR3)
                }
            }
            vString.append("}")
            vString.toString()
        } else if (value is JSONObject) {
            val vString = StringBuilder()
            vString.append("{")
            value.keys().forEach {
                if (value.isNull(it)) {
                    vString.append("${it}: null,")
                } else {
                    val element = value[it]
                    if (element is Int || element is Long || element is Double || element is Float || element is Boolean) {
                        vString.append("${it}:${element},")
                    } else if (element is String || element is Char) {
                        vString.append("${it}:`${element}`,")
                    } else if (element is Array<*> || element is Iterable<*> || element is Map<*, *> || element is JSONArray || element is JSONObject) {
                        vString.append("${it}:${convertInput(element)},")
                    } else if (element is FlexType) {
                        vString.append("${it}:${convertInput(objectToMap(element))},")
                    } else {
                        throw FlexException(FlexException.ERROR3)
                    }
                }
            }
            vString.append("}")
            vString.toString()
        } else if (value == null) {
            "null"
        } else if (value is FlexType) {
            convertInput(objectToMap(value))
        } else {
            throw FlexException(FlexException.ERROR3)
        }
    }

    internal fun jsonArrayToFlexArguments(value: JSONArray?): FlexArguments {
        if (value == null) return FlexArguments(emptyArray())
        val res = FlexArguments(Array(value.length()) { FlexData() })
        for (i in 0 until value.length()) {
            when (val ele = value[i]) {
                is Int -> res[i] = FlexData(ele)
                is String -> res[i] = FlexData(ele)
                is Long -> res[i] = FlexData(ele)
                is Double -> res[i] = FlexData(ele)
                is Boolean -> res[i] = FlexData(ele)
                is JSONArray -> res[i] = FlexData(jsonArrayToFlexData(ele))
                is JSONObject -> res[i] = FlexData(jsonObjectToFlexData(ele))
            }
        }
        return res
    }

    internal fun jsonArrayToFlexData(value: JSONArray?): Array<FlexData> {
        if (value == null) return emptyArray()
        val res = Array(value.length()) { FlexData() }
        for (i in 0 until value.length()) {
            when (val ele = value[i]) {
                is Int -> res[i] = FlexData(ele)
                is String -> res[i] = FlexData(ele)
                is Long -> res[i] = FlexData(ele)
                is Double -> res[i] = FlexData(ele)
                is Boolean -> res[i] = FlexData(ele)
                is JSONArray -> res[i] = FlexData(jsonArrayToFlexData(ele))
                is JSONObject -> res[i] = FlexData(jsonObjectToFlexData(ele))
            }
        }
        return res
    }

    internal fun jsonObjectToFlexData(value: JSONObject?): Map<String, FlexData> {
        val res = HashMap<String, FlexData>()
        if (value == null) return res
        value.keys().forEach {
            when (val ele = value[it]) {
                is Int -> res[it] = FlexData(ele)
                is String -> res[it] = FlexData(ele)
                is Long -> res[it] = FlexData(ele)
                is Double -> res[it] = FlexData(ele)
                is Boolean -> res[it] = FlexData(ele)
                is JSONArray -> res[it] = FlexData(jsonArrayToFlexData(ele))
                is JSONObject -> res[it] = FlexData(jsonObjectToFlexData(ele))
                else -> res[it] = FlexData()
            }
        }
        return res
    }

    internal fun anyToFlexData(value: Any?): FlexData {
        return when (value) {
            is Int -> FlexData(value)
            is String -> FlexData(value)
            is Long -> FlexData(value)
            is Double -> FlexData(value)
            is Boolean -> FlexData(value)
            is JSONArray -> FlexData(jsonArrayToFlexData(value))
            is JSONObject -> FlexData(jsonObjectToFlexData(value))
            is BrowserException -> FlexData(value)
            else -> FlexData()
        }
    }

    internal fun mapToObject(map: Map<String, Any?>, clazz: KClass<*>): Any {
        val constructor = clazz.constructors.first()
        val argsValue: HashMap<String, Any?> = HashMap()
        clazz.memberProperties.map {
            argsValue[it.name] = anyToObject(map[it.name], it.returnType)
        }
        val args = constructor
            .parameters
            .map { it to argsValue[it.name] }
            .toMap()
        return constructor.callBy(args)
    }

    internal fun anyToObject(value: Any?, type: KType): Any? {
        if (value == null) return null
        val clazz = type.classifier as KClass<*>
        return when {
            clazz.allSuperclasses.contains(Iterable::class.starProjectedType.classifier as KClass<*>) -> {
                val arrGenericType =
                    ((type.javaType as ParameterizedType).actualTypeArguments[0] as Class<*>).kotlin.starProjectedType
                val arrValue = value as Array<*>
                val listResult: ArrayList<Any?> = ArrayList()
                arrValue.forEach { any ->
                    listResult.add(anyToObject(any, arrGenericType))
                }
                listResult
            }
            clazz.allSuperclasses.contains(Map::class.starProjectedType.classifier as KClass<*>) -> {
                val keyGenericType =
                    ((type.javaType as ParameterizedType).actualTypeArguments[0] as Class<*>).kotlin.starProjectedType
                if (String::class.starProjectedType != keyGenericType) throw Exception()
                mapToObject(value as Map<String, Any?>, clazz)
            }
            clazz.allSuperclasses.contains(FlexType::class.starProjectedType.classifier as KClass<*>) -> mapToObject(
                value as Map<String, Any?>,
                clazz
            )
            else -> value
        }
    }

    internal fun <T : Any> objectToMap(data: T): Map<String, Any?> {
        val props = data::class.memberProperties.associateBy { it.name }
        return props.keys.associateWith { props[it]?.getter?.call(data) }
    }

    private const val TAG = "FLEXWEBVIEW"

    internal fun INFO(msg: Any?) {
        android.util.Log.i(TAG, msg.toString())
    }

    internal fun DEBUG(msg: Any?) {
        android.util.Log.d(TAG, msg.toString())
    }

    internal fun VERBOSE(msg: Any?) {
        android.util.Log.v(TAG, msg.toString())
    }

    internal fun WARN(msg: Any?) {
        android.util.Log.w(TAG, msg.toString())
    }

    internal fun ERR(msg: Any?) {
        android.util.Log.e(TAG, msg.toString())
    }


    internal fun fileToString(inputStream: InputStream): String {
        try {
            val bufferedReader = BufferedReader(inputStream.reader())
            val sb = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                sb.append(line).append("\n")
            }
            bufferedReader.close()
            return sb.toString()
        } catch (e: java.lang.Exception) {
            throw FlexException(e)
        }
    }

    internal fun fileToString(reader: Reader): String {
        try {
            val bufferedReader = BufferedReader(reader)
            val sb = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                sb.append(line).append("\n")
            }
            bufferedReader.close()
            return sb.toString()
        } catch (e: java.lang.Exception) {
            throw FlexException(e)
        }
    }

    internal fun checkAllowSite(origin: String?, input: String?): Boolean {
        if (origin == null) return true
        if (input == null) return false
        if (origin == input) return true
        try {
            if (origin.toRegex().containsMatchIn(input)) return true
        } catch (e: PatternSyntaxException) {
            e.printStackTrace()
        }
        val originUri = Uri.parse(origin)
        val inputUri = Uri.parse(input)
        if (originUri.scheme == null) {
            val isOriginWild = origin.startsWith(".")
            val originRealDomain = if (isOriginWild) origin.substring(1) else origin
            if (originRealDomain == inputUri.host) return true
            else if (isOriginWild) {
                val originDomains = origin.split(".")
                val inputDomains = inputUri.host?.split(".")
                if (originDomains.size != inputDomains?.size) return false
                var isAllow = true
                inputDomains.forEachIndexed { i, domain ->
                    if (i != 0 && originDomains[i] != domain) {
                        isAllow = false
                        return@forEachIndexed
                    }
                }
                return isAllow
            }
        } else if (originUri.scheme.equals(inputUri.scheme) && originUri.host.equals(inputUri.host)) return true
        return false
    }
}