package com.sayhitoiot.coronanews.features.news

import android.annotation.SuppressLint
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.commom.util.Constants
import kotlinx.android.synthetic.main.dialog_ssl_error.view.*
import kotlinx.android.synthetic.main.fragment_news.*


class NewsFragment : Fragment() {

    private var buttonCancel: MaterialButton? = null
    private var buttonConfirm: MaterialButton? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadNews()
        onBackPressed()
    }

    private fun onBackPressed() {
        fragmentNews_webview.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (event?.action !=KeyEvent.ACTION_DOWN)
                    return true;
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (fragmentNews_webview.canGoBack()) {
                        fragmentNews_webview.goBack();
                    } else {
                        requireActivity().onBackPressed();
                    }
                    return true;
            }
                return false
        }
     })

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadNews() {
        val setting = fragmentNews_webview.settings
        setting.javaScriptEnabled = true
        setting.cacheMode = WebSettings.LOAD_DEFAULT
        fragmentNews_webview.isSoundEffectsEnabled = true;
        fragmentNews_webview.settings.setAppCacheEnabled(true);
        fragmentNews_webview.setLayerType(WebView.LAYER_TYPE_NONE, null)
        fragmentNews_webview.loadUrl(Constants.URL_NEWS)
        fragmentNews_webview.webViewClient = object : WebViewClient() {
            override
            fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_ssl_error, null)
                val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext()).setView(dialogView)
                val dialog = builder.show()

                buttonCancel = dialogView.dialogError_Button_cancel
                buttonConfirm = dialogView.dialogError_Button_confirm

                buttonConfirm?.setOnClickListener {
                    handler?.proceed()
                    dialog?.dismiss()
                }

                buttonCancel?.setOnClickListener {
                    handler?.cancel()
                    dialog?.dismiss()
                }
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {

                super.shouldOverrideUrlLoading(view, request)
                if (Uri.parse(Constants.URL_NEWS).host.equals(Constants.URL_NEWS)) {
                    return false;
                }
                return true;
            }
        }

    }

}
