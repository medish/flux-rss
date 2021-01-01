package com.example.projet_android.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.example.projet_android.R
import kotlinx.android.synthetic.main.activity_web_view.*

class WebView : AppCompatActivity() {
    lateinit var url :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        webView.webViewClient = WebViewClient()
        url = intent.getStringExtra("link") ?: ""
        webView.loadUrl(url)
    }
}