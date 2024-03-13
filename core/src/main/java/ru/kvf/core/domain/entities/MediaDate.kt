package ru.kvf.core.domain.entities

import android.annotation.SuppressLint
import android.content.res.Resources
import org.koin.java.KoinJavaComponent
import ru.kvf.core.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MediaDate(
    val date: Calendar,
    private val sortBy: Int = Calendar.DAY_OF_YEAR
) : Comparable<MediaDate> {

    companion object {
        val empty = MediaDate(Calendar.getInstance())
    }

    private val resources: Resources by KoinJavaComponent.inject(Resources::class.java)

    override fun compareTo(other: MediaDate): Int {
        return this.date.time.compareTo(other.date.time)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MediaDate

        val otherYear = other.date.get(Calendar.YEAR)
        val otherDayOfYear = other.date.get(sortBy)

        val year = date.get(Calendar.YEAR)
        val dayOfYear = date.get(sortBy)

        return (otherYear == year && otherDayOfYear == dayOfYear)
    }

    override fun hashCode(): Int {
        val year = date.get(Calendar.YEAR)
        val dayOfYear = date.get(sortBy)
        return year.hashCode() + dayOfYear.hashCode()
    }

    override fun toString(): String = if (sortBy == Calendar.DAY_OF_YEAR) {
        when {
            isUnknown() -> resources.getString(R.string.date_unknown)
            isToday() -> resources.getString(R.string.today)
            isYesterday() -> resources.getString(R.string.yesterday)
            else -> sdfDaily.format(date.time)
        }
    } else {
        sdfMonthly.format(date.time)
    }

    private fun isToday() = this == today
    private fun isYesterday() = this == yesterday
    private fun isUnknown() = date.time.time == 0L
}

@SuppressLint("ConstantLocale")
private val sdfDaily = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
@SuppressLint("ConstantLocale")
private val sdfMonthly = SimpleDateFormat("MMM yy", Locale.getDefault())
private val today = MediaDate(Calendar.getInstance())
private val yesterday = MediaDate(
    Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR, -1)
    }
)
