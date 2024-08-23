package com.myapp.mylibrary

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.myapp.mylibrary.repository.PointsResponse
import com.myapp.samplepoc.ui.theme.LibraryTheme

class PointsActivity : ComponentActivity() {

    private val pointsResponse by lazy {
        intent.getSerializableExtra("data") as PointsResponse
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            LibraryTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Transparent
                ) { innerPadding ->

                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Column(
                            modifier = Modifier
                                .background(Color.White)
                                .padding(innerPadding)
                                .imePadding()
                        ) {

                            val state =
                                rememberWebViewState("http://sanasahallc.com/home?accruedpoints=${pointsResponse.accruedPoints.orEmpty()}")
                            //   var webViewLocal: WebView? = null

                            TopAppBar(
                                title = { Text(text = "Points") },
                                actions = {

                                    IconButton(onClick = {
                                        //
                                        finish()
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Close"
                                        )
                                    }
                                }
                            )

                            AndroidView(
                                factory = { context ->

                                    WebView(context).apply {
                                        webViewClient = object : WebViewClient() {
                                            override fun onPageFinished(
                                                view: WebView?,
                                                url: String?
                                            ) {
                                                super.onPageFinished(view, url)
                                            }
                                        }

                                        webChromeClient = object : WebChromeClient() {
                                            override fun onProgressChanged(
                                                view: WebView?,
                                                newProgress: Int
                                            ) {

                                            }
                                        }

                                        settings.javaScriptEnabled = true
                                        addJavascriptInterface(
                                            WebAppInterface(context),
                                            "Android"
                                        )
                                    }
                                },
                                update = { webView ->
                                    webView.loadUrl(state.url)
                                    // webViewLocal = webView
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

        }
    }
}

