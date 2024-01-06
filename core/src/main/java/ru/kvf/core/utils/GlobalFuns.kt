package ru.kvf.core.utils

import java.util.Calendar

fun Int?.toCalendarSort(): Int = if (this == 2) Calendar.MONTH else Calendar.DAY_OF_YEAR
