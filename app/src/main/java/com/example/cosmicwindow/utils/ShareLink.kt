package com.example.cosmicwindow.utils

import android.content.Context
import android.content.Intent


fun Context.shareLink(url: String, chooserTitle: String = "Share link") {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, url)
    }
    startActivity(Intent.createChooser(intent, chooserTitle))
}