package com.example.collegeschedule.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun formatDate(dateString: String): String {
    val date = LocalDate.parse(dateString.substring(0, 10))
    return date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
}
