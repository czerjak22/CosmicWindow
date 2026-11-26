package com.example.cosmicwindow.core.utils

import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun getDayTextSince(date: LocalDate): String =
    when (val daysAgo = ChronoUnit.DAYS.between(date, LocalDate.now())) {
        0L -> "Today"
        1L -> "Yesterday"
        else -> "$daysAgo days ago"
    }
