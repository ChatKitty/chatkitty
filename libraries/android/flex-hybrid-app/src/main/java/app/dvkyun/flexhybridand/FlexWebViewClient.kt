package app.dvkyun.flexhybridand

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.CallSuper
import androidx.annotation.RequiresApi
import androidx.webkit.WebViewAssetLoader

open class FlexWebViewClient : WebViewClient(), FlexWebViewClientInterface {

    private var assetLoader: WebViewAssetLoader? = null

    internal var isAssetLoaderUse = false
    internal var assetLoaderPrefixPath = "/assets/"

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        if (view is FlexWebView) {
            return !checkAllowedUrl(view, request)
        }
        return false
    }

    @Suppress("DEPRECATION")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (view is FlexWebView) {
            return !checkAllowedUrl(view, url)
        }
        return false
    }

    @CallSuper
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        if (view is FlexWebView && url != null) {
            if (FlexUtil.checkAllowSite(view.baseUrl, url)) view.flexInitInPage()
            else {
                view.allowUrlMap.forEach {
                    if (it.value && FlexUtil.checkAllowSite(it.key, url)) {
                        view.flexInitInPage()
                        return@forEach
                    }
                }
            }
        }
        super.onPageStarted(view, url, favicon)
    }

    @Suppress("DEPRECATION")
    override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
        if (isAssetLoaderUse && view != null && url != null) {
            if (assetLoader == null) {
                assetLoader = WebViewAssetLoader
                    .Builder()
                    .addPathHandler(
                        assetLoaderPrefixPath,
                        WebViewAssetLoader.AssetsPathHandler(view.context)
                    ).build()
            }
            return assetLoader?.shouldInterceptRequest(Uri.parse(url))
        }
        assetLoader = null
        return null
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? {
        if (request?.isForMainFrame == true && view is FlexWebView) {
            checkAllowedUrl(view, request)
        }
        if (isAssetLoaderUse && view != null && request?.url != null) {
            if (assetLoader == null) {
                assetLoader = WebViewAssetLoader
                    .Builder()
                    .addPathHandler(
                        assetLoaderPrefixPath,
                        WebViewAssetLoader.AssetsPathHandler(view.context)
                    ).build()
            }
            return assetLoader?.shouldInterceptRequest(request.url)
        }
        assetLoader = null
        return null
    }

    override fun notAllowedUrlLoad(view: WebView, request: WebResourceRequest?, url: String?) {
        FlexUtil.ERR("Unacceptable url called - $url")
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun checkAllowedUrl(view: FlexWebView, request: WebResourceRequest?): Boolean {
        return checkAllowedUrl(view, request?.url?.toString(), request)
    }

    private fun checkAllowedUrl(
        view: FlexWebView,
        requestUrl: String?,
        request: WebResourceRequest? = null
    ): Boolean {
        if (requestUrl == null) return true
        if (view.baseUrl == null && view.allowUrlMap.size == 0) return true
        if (view.baseUrl != null && FlexUtil.checkAllowSite(view.baseUrl, requestUrl)) return true
        view.allowUrlMap.forEach {
            if (FlexUtil.checkAllowSite(it.key, requestUrl)) return true
        }
        view.activity?.runOnUiThread {
            notAllowedUrlLoad(view, request, requestUrl)
        }
        return false
    }

}

private interface FlexWebViewClientInterface {
    fun notAllowedUrlLoad(view: WebView, request: WebResourceRequest? = null, url: String? = null)
}
