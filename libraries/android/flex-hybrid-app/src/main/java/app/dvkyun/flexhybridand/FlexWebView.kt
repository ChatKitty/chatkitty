package app.dvkyun.flexhybridand

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.webkit.*
import androidx.annotation.CallSuper
import app.dvkyun.flexhybridand.forjava.*
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.InputStream
import java.io.Reader
import java.lang.Exception
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KVisibility
import kotlin.reflect.full.*

open class FlexWebView : WebView {

    companion object {
        private const val VERSION = "1.0.3"
        private val UNIQUE = UUID.randomUUID().toString()
        internal const val FLEX = "flex"

        /*
        * options
        * */
        private const val OPT_TIMEOUT = "timeout"
        private const val OPT_LOAD_WAIT = "flexLoadWait"

        /*
        * internal interface
        * */
        private const val INT_RETURN = "flexreturn"
        private const val INT_LOAD = "flexload"

        /*
        * event interface
        * */
        private const val EVT_SUC = "flexSuccess"
        private const val EVT_EXC = "flexException"
        private const val EVT_TIMEOUT = "flexTimeout"
        private const val EVT_INIT = "flexInit"
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    var activity: Activity? = null
        internal set
        get() = FlexUtil.getActivity(context)
    private val interfaceObj: FlexInterfaces = FlexInterfaces()
    private val options: JSONObject = JSONObject()
    private val dependencies: ArrayList<String> = ArrayList()
    private val returnFromWeb: HashMap<Int, suspend CoroutineScope.(FlexData) -> Unit> = HashMap()
    private val internalInterface =
        arrayOf(INT_RETURN, INT_LOAD, EVT_SUC, EVT_EXC, EVT_TIMEOUT, EVT_INIT)
    private var tCount = Runtime.getRuntime().availableProcessors()
    private var cContext: CoroutineContext? = null
    val scope by lazy {
        cContext?.let {
            CoroutineScope(it)
        } ?: CoroutineScope(Executors.newFixedThreadPool(tCount).asCoroutineDispatcher())
    }
    var isFlexLoad: Boolean = false
        private set
    private val beforeFlexLoadEvalList = ArrayList<BeforeFlexEval>()

    private var isAfterFirstLoad = false
    private var flexJsString: String
    private lateinit var mWebViewClient: FlexWebViewClient
    private lateinit var mWebChromeClient: FlexWebChromeClient

    var baseUrl: String? = null
        set(value) {
            if (field != null) {
                throw FlexException(FlexException.ERROR5)
            }
            if (value == null) {
                field = value
                return
            }
            field = value
            baseUri = Uri.parse(value)
        }
    var baseUri: Uri? = null
    internal val allowUrlMap: HashMap<String, Boolean> = HashMap()

    fun setAllowUrl(url: String, canUseFlex: Boolean = false) {
        allowUrlMap[url] = canUseFlex
    }

    fun removeAllowUrl(url: String) {
        allowUrlMap.remove(url)
    }

    private var flexEventList: ArrayList<Pair<FlexEvent, FlexListener>> = ArrayList()
    private var flexEventListJava: ArrayList<Pair<FlexEvent, FlexListenerForJava>> = ArrayList()

    fun addFlexEventListener(type: FlexEvent, listener: FlexListener) {
        flexEventList.add(Pair(type, listener))
    }

    fun addFlexEventListener(
        type: FlexEvent,
        listener: (view: FlexWebView, type: FlexEvent, url: String, funcName: String, msg: String?) -> Unit
    ) {
        flexEventList.add(Pair(type, FlexListener(listener)))
    }

    fun addFlexEventListener(listener: FlexListener) {
        flexEventList.add(Pair(FlexEvent.SUCCESS, listener))
        flexEventList.add(Pair(FlexEvent.EXCEPTION, listener))
        flexEventList.add(Pair(FlexEvent.TIMEOUT, listener))
        flexEventList.add(Pair(FlexEvent.INIT, listener))
    }

    fun addFlexEventListener(listener: (view: FlexWebView, type: FlexEvent, url: String, funcName: String, msg: String?) -> Unit) {
        flexEventList.add(Pair(FlexEvent.SUCCESS, FlexListener(listener)))
        flexEventList.add(Pair(FlexEvent.EXCEPTION, FlexListener(listener)))
        flexEventList.add(Pair(FlexEvent.TIMEOUT, FlexListener(listener)))
        flexEventList.add(Pair(FlexEvent.INIT, FlexListener(listener)))
    }

    fun addFlexEventListenerForJava(type: FlexEvent, listener: FlexListenerForJava) {
        flexEventListJava.add(Pair(type, listener))
    }

    fun addFlexEventListenerForJava(listener: FlexListenerForJava) {
        flexEventListJava.add(Pair(FlexEvent.SUCCESS, listener))
        flexEventListJava.add(Pair(FlexEvent.EXCEPTION, listener))
        flexEventListJava.add(Pair(FlexEvent.TIMEOUT, listener))
        flexEventListJava.add(Pair(FlexEvent.INIT, listener))
    }

    fun removeFlexEventListener(listener: FlexListener) {
        flexEventList = flexEventList.filter { _item ->
            _item.second.id != listener.id
        } as ArrayList<Pair<FlexEvent, FlexListener>>
    }

    fun removeAllFlexEventListener() {
        flexEventList = ArrayList()
        flexEventListJava = ArrayList()
    }

    init {
        val atv = activity ?: throw FlexException(FlexException.ERROR1)
        flexJsString = FlexUtil.fileToString(context.assets.open("FlexHybridAnd.js"))
        webChromeClient = FlexWebChromeClient(atv)
        webViewClient = FlexWebViewClient()
        super.addJavascriptInterface(InternalInterface(), UNIQUE)
        initialize()
    }

    /*
    * Initial setting
    * */
    @SuppressLint("SetJavaScriptEnabled")
    fun initialize() {
        setWebContentsDebuggingEnabled(true)
        settings.javaScriptEnabled = true
        settings.displayZoomControls = false
        settings.builtInZoomControls = false
        settings.setSupportZoom(false)
        settings.textZoom = 100
        settings.domStorageEnabled = true
        settings.loadWithOverviewMode = true
        settings.loadsImagesAutomatically = true
        settings.useWideViewPort = true
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        settings.mediaPlaybackRequiresUserGesture = false
        settings.enableSmoothTransition()
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            setRendererPriorityPolicy(RENDERER_PRIORITY_IMPORTANT, true)
        }
    }

    @CallSuper
    override fun destroy() {
        scope.cancel(CancellationException(FlexException.ERROR13))
        super.destroy()
    }

    fun <T : FlexType, R> typeInterface(
        name: String,
        tClazz: KClass<T>,
        timeout: Int? = null,
        lambda: suspend CoroutineScope.(T) -> R?
    ): FlexWebView {
        interfaceObj.typeInterface(name, tClazz, timeout, lambda)
        return this
    }

    inline fun <reified T : FlexType, R> typeInterface(
        name: String,
        timeout: Int? = null,
        noinline lambda: suspend CoroutineScope.(arguments: T) -> R?
    ): FlexWebView {
        return typeInterface(name, T::class, timeout, lambda)
    }

    fun <R> setInterface(
        name: String,
        timeout: Int? = null,
        lambda: suspend CoroutineScope.(FlexArguments) -> R?
    ): FlexWebView {
        return typeInterface(name, FlexArguments::class, timeout, lambda)
    }

    fun <T : FlexType> typeAction(
        name: String,
        tClazz: KClass<T>,
        timeout: Int? = null,
        action: suspend CoroutineScope.(action: FlexAction, arguments: T) -> Unit
    ): FlexWebView {
        interfaceObj.typeAction(name, tClazz, timeout, action)
        return this
    }

    inline fun <reified T : FlexType> typeAction(
        name: String,
        timeout: Int? = null,
        noinline action: suspend CoroutineScope.(action: FlexAction, arguments: T) -> Unit
    ): FlexWebView {
        return typeAction(name, T::class, timeout, action)
    }

    fun setAction(
        name: String,
        timeout: Int? = null,
        action: suspend CoroutineScope.(action: FlexAction, arguments: FlexArguments) -> Unit
    ): FlexWebView {
        return typeAction(name, FlexArguments::class, timeout, action)
    }

    fun setInterfaceForJava(name: String, timeout: Int?, invoke: InvokeFlexVoid): FlexWebView {
        interfaceObj.setInterfaceForJava(name, timeout, invoke)
        return this
    }

    fun <R> setInterfaceForJava(name: String, timeout: Int?, invoke: InvokeFlex<R>): FlexWebView {
        interfaceObj.setInterfaceForJava(name, timeout, invoke)
        return this
    }

    fun setActionForJava(name: String, timeout: Int?, invoke: InvokeAction): FlexWebView {
        return typeAction(name, FlexArguments::class, timeout) { ac, args ->
            invoke.invoke(
                ac,
                args
            )
        }
    }

    fun addFlexInterface(flexInterfaces: Any) {
        if (isAfterFirstLoad) {
            throw FlexException(FlexException.ERROR6)
        }
        flexInterfaces::class.members.forEach {
            if (it.hasAnnotation<FlexFuncInterface>()) {
                if (it.visibility != KVisibility.PUBLIC) {
                    throw FlexException(FlexException.ERROR12)
                }
                var findArgs: Int = -1
                val params: Array<Any?> = arrayOfNulls(it.valueParameters.size)
                it.valueParameters.forEachIndexed { index, kParameter ->
                    if ((kParameter.type.classifier as? KClass<*>)?.allSuperclasses?.contains(
                            FlexType::class.starProjectedType.classifier as KClass<*>
                        ) == true
                    ) {
                        findArgs = index
                    }
                }
                if (findArgs == -1) {
                    throw FlexException(FlexException.ERROR10)
                }
                interfaceObj.setInterface(it.name) { arguments: FlexArguments ->
                    params[findArgs] = arguments
                    if (it.isSuspend) it.callSuspend(flexInterfaces, *params)
                    else it.call(flexInterfaces, *params)
                }
            } else if (it.hasAnnotation<FlexActionInterface>()) {
                if (it.visibility != KVisibility.PUBLIC) {
                    throw FlexException(FlexException.ERROR12)
                }
                var findArgs: Int = -1
                var findAction: Int = -1
                val params: Array<Any?> = arrayOfNulls(it.valueParameters.size)
                it.valueParameters.forEachIndexed { index, kParameter ->
                    if ((kParameter.type.classifier as? KClass<*>)?.allSuperclasses?.contains(
                            FlexType::class.starProjectedType.classifier as KClass<*>
                        ) == true
                    ) {
                        findArgs = index
                    } else if (kParameter.type.classifier == FlexAction::class.starProjectedType.classifier) {
                        findAction = index
                    }
                }
                if (findArgs == -1 || findAction == -1) {
                    throw FlexException(FlexException.ERROR11)
                }
                setAction(it.name) { action, arguments: FlexArguments ->
                    params[findAction] = action
                    params[findArgs] = arguments
                    if (it.isSuspend) it.callSuspend(flexInterfaces, *params)
                    else it.call(flexInterfaces, *params)
                }
            }
        }
        if (flexInterfaces is FlexInterfaces) {
            interfaceObj.interfaces.putAll(flexInterfaces.interfaces)
            interfaceObj.actions.putAll(flexInterfaces.actions)
            interfaceObj.iTimeouts.putAll(flexInterfaces.iTimeouts)
        }
    }

    fun setAllowFileAccessAndUrlAccessInFile(allow: Boolean) {
        settings.allowFileAccess = allow
        settings.allowFileAccessFromFileURLs = allow
        settings.allowUniversalAccessFromFileURLs = allow
    }

    fun setAssetsLoaderUse(use: Boolean, path: String = "/assets/") {
        webViewClient.isAssetLoaderUse = use
        webViewClient.assetLoaderPrefixPath = path
    }

    fun setInterfaceTimeout(timeout: Int) {
        if (isAfterFirstLoad) {
            throw FlexException(FlexException.ERROR6)
        }
        options.put(OPT_TIMEOUT, if (timeout != 0 && timeout < 100) 100 else timeout)
    }

    fun setFlexOnLoadWait(timeout: Int) {
        if (isAfterFirstLoad) {
            throw FlexException(FlexException.ERROR6)
        }
        options.put(OPT_LOAD_WAIT, timeout)
    }

    fun setInterfaceThreadCount(count: Int) {
        if (isAfterFirstLoad) {
            throw FlexException(FlexException.ERROR6)
        }
        tCount = count
    }

    fun setCoroutineContext(coroutineContext: CoroutineContext) {
        if (isAfterFirstLoad) {
            throw FlexException(FlexException.ERROR6)
        }
        cContext = coroutineContext
    }

    fun setDependency(inputStream: InputStream) {
        if (isAfterFirstLoad) {
            throw FlexException(FlexException.ERROR6)
        }
        dependencies.add(FlexUtil.fileToString(inputStream))
    }

    fun setDependency(reader: Reader) {
        if (isAfterFirstLoad) {
            throw FlexException(FlexException.ERROR6)
        }
        dependencies.add(FlexUtil.fileToString(reader))
    }

    fun setDependency(js: String) {
        if (isAfterFirstLoad) {
            throw FlexException(FlexException.ERROR6)
        }
        dependencies.add(js)
    }

    fun evalFlexFunc(funcName: String) {
        if (!isFlexLoad) {
            beforeFlexLoadEvalList.add(BeforeFlexEval(funcName))
        } else {
            FlexUtil.evaluateJavaScript(
                this,
                "!function(){try{const e=\$flex.web.$funcName();if(e instanceof Promise){e.then(e=>{\$flex.flexSuccess('web.$funcName',location.href,e)}).catch(e=>{\$flex.flexException('web.$funcName',location.href,e.toString())})}else{\$flex.flexSuccess('web.$funcName',location.href,e)}}catch(e){\$flex.flexException('web.$funcName',location.href,e.toString())}}();"
            )
        }
    }

    fun evalFlexFunc(funcName: String, response: suspend CoroutineScope.(FlexData) -> Unit) {
        if (!isFlexLoad) {
            beforeFlexLoadEvalList.add(BeforeFlexEval(funcName, response))
        } else {
            val tID = Random.nextInt(10000)
            returnFromWeb[tID] = response
            FlexUtil.evaluateJavaScript(
                this,
                "!function(){try{const e=\$flex.web.$funcName();if(e instanceof Promise){e.then(e=>{\$flex.flexSuccess('web.$funcName',location.href,e),\$flex.flexreturn({TID:$tID,Value:e,Error:!1})}).catch(e=>{\$flex.flexException('web.$funcName',location.href,e.toString()),\$flex.flexreturn({TID:$tID,Value:e,Error:!0})})}else{\$flex.flexSuccess('web.$funcName',location.href,e),\$flex.flexreturn({TID:$tID,Value:e,Error:!1})}}catch(e){\$flex.flexException('web.$funcName',location.href,e.toString()),\$flex.flexreturn({TID:$tID,Value:e,Error:!0})}}();"
            )
        }
    }

    fun evalFlexFunc(funcName: String, sendData: Any) {
        if (!isFlexLoad) {
            beforeFlexLoadEvalList.add(BeforeFlexEval(funcName, sendData))
        } else {
            FlexUtil.evaluateJavaScript(
                this,
                "!function(){try{const e=\$flex.web.$funcName(${FlexUtil.convertInput(sendData)});if(e instanceof Promise){e.then(e=>{\$flex.flexSuccess('web.$funcName',location.href,e)}).catch(e=>{\$flex.flexException('web.$funcName',location.href,e.toString())})}else{\$flex.flexSuccess('web.$funcName',location.href,e)}}catch(e){\$flex.flexException('web.$funcName',location.href,e.toString())}}();"
            )
        }
    }

    fun evalFlexFunc(
        funcName: String,
        sendData: Any,
        response: suspend CoroutineScope.(FlexData) -> Unit
    ) {
        if (!isFlexLoad) {
            beforeFlexLoadEvalList.add(BeforeFlexEval(funcName, sendData, response))
        } else {
            val tID = Random.nextInt(10000)
            returnFromWeb[tID] = response
            FlexUtil.evaluateJavaScript(
                this,
                "!function(){try{const e=\$flex.web.$funcName(${FlexUtil.convertInput(sendData)});if(e instanceof Promise){e.then((e)=>{\$flex.flexSuccess('web.$funcName',location.href,e);\$flex.flexreturn({TID:$tID,Value:e,Error:!1})}).catch((e)=>{\$flex.flexException('web.$funcName',location.href,e.toString());\$flex.flexreturn({TID:$tID,Value:e,Error:!0})})}else{\$flex.flexSuccess('web.$funcName',location.href,e);\$flex.flexreturn({TID:$tID,Value:e,Error:!1})}}catch(e){\$flex.flexException('web.$funcName',location.href,e.toString());\$flex.flexreturn({TID:$tID,Value:e,Error:!0})}}();"
            )
        }
    }

    fun evalFlexFuncWithRespForJava(funcName: String, response: FlexDataListener) {
        evalFlexFunc(funcName) { data -> response.onResponse(data) }
    }

    fun evalFlexFuncWithRespForJava(funcName: String, sendData: Any, response: FlexDataListener) {
        evalFlexFunc(funcName, sendData) { data -> response.onResponse(data) }
    }

    @CallSuper
    override fun getWebChromeClient(): FlexWebChromeClient {
        return mWebChromeClient
    }

    @CallSuper
    override fun setWebChromeClient(client: WebChromeClient?) {
        if (client !is FlexWebChromeClient) throw FlexException(FlexException.ERROR2)
        mWebChromeClient = client
        super.setWebChromeClient(client)
    }

    @CallSuper
    override fun getWebViewClient(): FlexWebViewClient {
        return mWebViewClient
    }

    @CallSuper
    override fun setWebViewClient(client: WebViewClient) {
        if (client !is FlexWebViewClient) throw FlexException(FlexException.ERROR2)
        mWebViewClient = client
        super.setWebViewClient(client)
    }

    @CallSuper
    @SuppressLint("JavascriptInterface")
    override fun addJavascriptInterface(`object`: Any, name: String) {
        if (name.contains("flex")) {
            throw FlexException(FlexException.ERROR8)
        }
        super.addJavascriptInterface(`object`, name)
    }

    @CallSuper
    override fun removeJavascriptInterface(name: String) {
        if (name == UNIQUE) {
            throw FlexException(FlexException.ERROR14)
        }
        super.removeJavascriptInterface(name)
    }

    internal fun flexInitInPage() {
        if (!isAfterFirstLoad) {
            flexJsString = flexJsString.replaceFirst("definefromAnd", "'$UNIQUE'")
            flexJsString = flexJsString.replaceFirst("versionFromAnd", "'$VERSION'")
            val keys = StringBuilder()
            keys.append("[\"")
            keys.append(internalInterface.joinToString(separator = "\",\""))
            if (interfaceObj.interfaces.size > 0) {
                keys.append("\",\"")
                keys.append(interfaceObj.interfaces.keys.joinToString(separator = "\",\""))
            }
            if (interfaceObj.actions.size > 0) {
                keys.append("\",\"")
                keys.append(interfaceObj.actions.keys.joinToString(separator = "\",\""))
            }
            keys.append("\"]")
            flexJsString = flexJsString.replaceFirst("keysfromAnd", keys.toString())
            flexJsString = flexJsString.replaceFirst(
                "timesfromAnd",
                FlexUtil.convertInput(interfaceObj.iTimeouts)
            )
            flexJsString =
                flexJsString.replaceFirst("optionsfromAnd", FlexUtil.convertInput(options))
            flexJsString = flexJsString.replaceFirst(
                "defineflexfromAnd",
                FlexUtil.convertInput(internalInterface)
            )
            val device = JSONObject()
            device.put("os", "Android")
            device.put("version", Build.VERSION.SDK_INT)
            device.put("model", Build.MODEL)
            flexJsString =
                flexJsString.replaceFirst("deviceinfoFromAnd", FlexUtil.convertInput(device))
        }
        isAfterFirstLoad = true
        isFlexLoad = false
        FlexUtil.evaluateJavaScript(this, flexJsString)
        dependencies.forEach {
            FlexUtil.evaluateJavaScript(this, it)
        }
    }

    private inner class InternalInterface {
        @JavascriptInterface
        fun flexInterface(input: String) {
            scope.launch {
                try {
                    val data = JSONObject(input)
                    val intName: String = data.getString("intName")
                    val fName: String = data.getString("funName")
                    val args: JSONArray = data.getJSONArray("arguments")
                    try {
                        if (interfaceObj.interfaces[intName] != null) { // call interface
                            val dataInfo = interfaceObj.interfaces[intName] ?: throw FlexException(
                                FlexException.ERROR16
                            )
                            val value = dataInfo.second.let {
                                when {
                                    dataInfo.first == FlexArguments::class -> it(
                                        FlexUtil.jsonArrayToFlexArguments(
                                            args
                                        )
                                    )
                                    dataInfo.first.allSuperclasses.contains(FlexType::class.starProjectedType.classifier as KClass<*>) -> it(
                                        FlexUtil.mapToObject(
                                            FlexUtil.convertJSONObject(
                                                args.getJSONObject(0)
                                            ), dataInfo.first
                                        )
                                    )
                                    else -> throw FlexException(FlexException.ERROR17)
                                }
                            }
                            if (value is BrowserException) {
                                val reason =
                                    if (value.reason == null) null else "\"${value.reason}\""
                                FlexUtil.rejectPromise(this@FlexWebView, fName, reason)
                            } else if (value == null || value == Unit || value is Void) {
                                FlexUtil.responsePromise(this@FlexWebView, fName)
                            } else {
                                FlexUtil.responsePromise(this@FlexWebView, fName, value)
                            }
                        } else if (interfaceObj.actions[intName] != null) { // call Action
                            val dataInfo = interfaceObj.actions[intName] ?: throw FlexException(
                                FlexException.ERROR16
                            )
                            dataInfo.second.also {
                                when {
                                    dataInfo.first == FlexArguments::class -> it(
                                        FlexAction(
                                            fName,
                                            this@FlexWebView
                                        ), FlexUtil.jsonArrayToFlexArguments(args)
                                    )
                                    dataInfo.first.allSuperclasses.contains(FlexType::class.starProjectedType.classifier as KClass<*>) -> it(
                                        FlexAction(fName, this@FlexWebView),
                                        FlexUtil.mapToObject(
                                            FlexUtil.convertJSONObject(
                                                args.getJSONObject(0)
                                            ), dataInfo.first
                                        )
                                    )
                                    else -> throw FlexException(FlexException.ERROR17)
                                }
                            }
                        } else { // call library interface
                            when (intName) {
                                INT_RETURN -> { // flexreturn
                                    val iData = args.getJSONObject(0)
                                    val tID = iData.getInt("TID")
                                    val value = iData.opt("Value")
                                    val error = iData.getBoolean("Error")
                                    if (error) {
                                        val errMsg: String =
                                            value?.let { value.toString() } ?: "null"
                                        returnFromWeb[tID]?.also {
                                            it(
                                                FlexUtil.anyToFlexData(
                                                    BrowserException(errMsg)
                                                )
                                            )
                                        }
                                    } else {
                                        returnFromWeb[tID]?.also { it(FlexUtil.anyToFlexData(value)) }
                                    }
                                    FlexUtil.responsePromise(this@FlexWebView, fName)
                                }
                                INT_LOAD -> { // flexload
                                    if (isFlexLoad) {
                                        FlexUtil.responsePromise(this@FlexWebView, fName)
                                        return@launch
                                    }
                                    isFlexLoad = true
                                    beforeFlexLoadEvalList.forEach {
                                        if (it.sendData != null && it.response != null) {
                                            evalFlexFunc(it.name, it.sendData, it.response)
                                        } else if (it.sendData != null && it.response == null) {
                                            evalFlexFunc(it.name, it.sendData)
                                        } else if (it.sendData == null && it.response != null) {
                                            evalFlexFunc(it.name, it.response)
                                        } else {
                                            evalFlexFunc(it.name)
                                        }
                                    }
                                    beforeFlexLoadEvalList.clear()
                                    FlexUtil.responsePromise(this@FlexWebView, fName)
                                }
                                EVT_SUC, EVT_EXC, EVT_TIMEOUT, EVT_INIT -> { // events
                                    FlexUtil.responsePromise(this@FlexWebView, fName)
                                    val funcName = args.getString(0)
                                    val url = args.getString(1)
                                    val msg = if (args.length() > 2) args.getString(2) else null
                                    val type: FlexEvent =
                                        when (intName) {
                                            EVT_SUC -> FlexEvent.SUCCESS
                                            EVT_EXC -> FlexEvent.EXCEPTION
                                            EVT_TIMEOUT -> FlexEvent.TIMEOUT
                                            EVT_INIT -> FlexEvent.INIT
                                            else -> FlexEvent.EXCEPTION
                                        }
                                    flexEventList.forEach { _item ->
                                        if (_item.first == type) {
                                            launch(Dispatchers.Main) {
                                                _item.second.listener(
                                                    this@FlexWebView,
                                                    type,
                                                    url,
                                                    funcName,
                                                    msg
                                                )
                                            }
                                        }
                                    }
                                    flexEventListJava.forEach { _item ->
                                        if (_item.first == type) {
                                            launch(Dispatchers.Main) {
                                                _item.second.onEvent(
                                                    this@FlexWebView,
                                                    type,
                                                    url,
                                                    funcName,
                                                    msg
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } catch (e: Exception) {
                        FlexUtil.ERR(e)
                        FlexUtil.rejectPromise(this@FlexWebView, fName, e.cause?.message)
                        e.printStackTrace()
                    }
                } catch (e: JSONException) {
                    FlexUtil.ERR(e)
                    e.printStackTrace()
                }
            }
        }
    }

    private inner class BeforeFlexEval {
        val name: String
        val sendData: Any?
        val response: (suspend CoroutineScope.(FlexData) -> Unit)?

        constructor(name: String) {
            this.name = name
            sendData = null
            response = null
        }

        constructor(name: String, sendData: Any) {
            this.name = name
            this.sendData = sendData
            response = null
        }

        constructor(name: String, response: suspend CoroutineScope.(FlexData) -> Unit) {
            this.name = name
            this.response = response
            sendData = null
        }

        constructor(
            name: String,
            sendData: Any,
            response: suspend CoroutineScope.(FlexData) -> Unit
        ) {
            this.name = name
            this.sendData = sendData
            this.response = response
        }
    }

}
