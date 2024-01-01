package ru.kvf.core.data

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CustomDate(
    private val date: Calendar
) : Comparable<CustomDate> {

    override fun compareTo(other: CustomDate): Int {
        return this.date.time.compareTo(other.date.time)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CustomDate

        val otherYear = other.date.get(Calendar.YEAR)
        val otherDayOfYear = other.date.get(Calendar.DAY_OF_YEAR)

        val year = date.get(Calendar.YEAR)
        val dayOfYear = date.get(Calendar.DAY_OF_YEAR)

        return !(otherYear != year && otherDayOfYear != dayOfYear)
    }

    override fun hashCode(): Int {
        val year = date.get(Calendar.YEAR)
        val dayOfYear = date.get(Calendar.DAY_OF_YEAR)
        return year.hashCode() + dayOfYear.hashCode()
    }

    override fun toString(): String = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        .run { format(date.time) }
}
