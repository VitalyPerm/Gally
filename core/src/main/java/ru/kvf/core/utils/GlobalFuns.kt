package ru.kvf.core.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import java.util.Calendar

fun Int?.toCalendarSort(): Int = if (this == 2) Calendar.MONTH else Calendar.DAY_OF_YEAR

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
