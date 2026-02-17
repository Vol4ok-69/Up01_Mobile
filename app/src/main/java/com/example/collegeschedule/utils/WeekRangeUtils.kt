package com.example.collegeschedule.utils

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun getForwardWeekRange(weekOffset: Int): Pair<String, String> {

    val formatter = DateTimeFormatter.ISO_DATE
    val today = LocalDate.now()

    val monday = today
        .with(DayOfWeek.MONDAY)
        .plusWeeks(weekOffset.toLong())

    val saturday = monday.plusDays(5)

    return monday.format(formatter) to saturday.format(formatter)
}



