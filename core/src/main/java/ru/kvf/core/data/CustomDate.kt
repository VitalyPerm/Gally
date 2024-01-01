package ru.kvf.core.data

import android.annotation.SuppressLint
import android.content.res.Resources
import org.koin.java.KoinJavaComponent.inject
import ru.kvf.core.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CustomDate(
    val date: Calendar
) : Comparable<CustomDate> {

    private val resources: Resources by inject(Resources::class.java)

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

        return (otherYear == year && otherDayOfYear == dayOfYear)
    }

    override fun hashCode(): Int {
        val year = date.get(Calendar.YEAR)
        val dayOfYear = date.get(Calendar.DAY_OF_YEAR)
        return year.hashCode() + dayOfYear.hashCode()
    }

    override fun toString(): String = when {
        isUnknown() -> resources.getString(R.string.date_unknown)
        isToday() -> resources.getString(R.string.today)
        isYesterday() -> resources.getString(R.string.yesterday)
        else -> sdf.format(date.time)
    }

    private fun isToday() = this == today
    private fun isYesterday() = this == yesterday
    private fun isUnknown() = date.time.time == 0L
}

@SuppressLint("ConstantLocale")
private val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
private val today = CustomDate(Calendar.getInstance())
private val yesterday = CustomDate(
    Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR, -1)
    }
)
