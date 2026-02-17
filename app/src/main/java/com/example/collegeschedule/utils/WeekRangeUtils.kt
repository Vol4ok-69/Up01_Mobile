package com.example.collegeschedule.utils

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun getForwardWeekRange(): Pair<String, String> {

    val formatter = DateTimeFormatter.ISO_DATE
    val today = LocalDate.now()

    val monday = today.with(DayOfWeek.MONDAY)
    val saturday = monday.plusDays(5)

    return monday.format(formatter) to saturday.format(formatter)
}


