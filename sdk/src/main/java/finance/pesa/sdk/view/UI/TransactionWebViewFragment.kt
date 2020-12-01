package finance.pesa.sdk.view.UI

import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import finance.pesa.sdk.R
import finance.pesa.sdk.utils.Constants
import finance.pesa.sdk.utils.UserInterface


class TransactionWebViewFragment : AppCompatActivity() {


    private var url: String? = null
    private var webView: WebView? = null
    private var ivBack: ImageView? = null

    companion object {
        fun newInstance(url: String): TransactionWebViewFragment {
            val transactionWebViewFragment = TransactionWebViewFragment()
            transactionWebViewFragment.url = url
            return transactionWebViewFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_transaction)
        webView = findViewById(R.id.webview)
        ivBack = findViewById(R.id.iv_back)
        url = Constants.kovan_transaction_url + intent.getStringExtra("transactionHash")
        webView?.webViewClient = HelloWebViewClient()
        webView?.settings!!.javaScriptEnabled = true
        ivBack?.setOnClickListener { onBackPressed() }
        webView?.loadUrl(url!!)
        UserInterface.changeStatusBar(this, R.color.white_black)
        UserInterface.changeNavigationBar(this, R.color.white_black)
        UserInterface.showCancellableProgress("Loading...", this)
    }

    private inner class HelloWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            UserInterface.hideProgress(this@TransactionWebViewFragment)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView!!.canGoBack()) {
            webView!!.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}