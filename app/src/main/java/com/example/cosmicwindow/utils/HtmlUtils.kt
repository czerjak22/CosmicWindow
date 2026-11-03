package com.example.cosmicwindow.utils

import org.jsoup.Jsoup

fun String?.stripHtml(): String = this?.let { Jsoup.parse(it).text() }.orEmpty()

fun String?.extractVideoLink(): String? {
    if (this.isNullOrBlank()) return null
    val doc = Jsoup.parse(this)

    val video = doc.selectFirst("video source[src]")?.attr("src")
        ?: doc.selectFirst("video[src]")?.attr("src")
        ?: doc.selectFirst("iframe[src]")?.attr("src")   // YouTube / Vimeo days

    return video
}