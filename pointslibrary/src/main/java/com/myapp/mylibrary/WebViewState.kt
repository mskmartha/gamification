package com.myapp.mylibrary

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Stable
class WebViewState(
    initialUrl: String
) {
    var url by mutableStateOf(initialUrl)
}

@Composable
fun rememberWebViewState(initialUrl: String): WebViewState {
    return remember { WebViewState(initialUrl) }
}