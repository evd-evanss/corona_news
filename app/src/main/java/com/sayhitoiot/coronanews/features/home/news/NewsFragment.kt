package com.sayhitoiot.coronanews.features.home.news

import android.annotation.SuppressLint
import android.net.http.SslError
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.commom.util.Constants.Companion.URL_NEWS
import kotlinx.android.synthetic.main.fragment_news.*

class NewsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadNews()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadNews() {
        val setting = fragmentNews_webview.settings
        setting.javaScriptEnabled = true
        setting.cacheMode = WebSettings.LOAD_DEFAULT
        fragmentNews_webview.isSoundEffectsEnabled = true;
        fragmentNews_webview.settings.setAppCacheEnabled(true);
        fragmentNews_webview.setLayerType(WebView.LAYER_TYPE_NONE, null)
        fragmentNews_webview.loadUrl(URL_NEWS)
        fragmentNews_webview.webViewClient = object : WebViewClient() {
            override
            fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                handler?.proceed()
            }
        }

    }


}
