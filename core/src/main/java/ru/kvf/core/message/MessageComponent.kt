package ru.kvf.core.message

import androidx.annotation.StringRes

interface MessageComponent {
    fun showMessage(@StringRes textRes: Int, args: Any)
    fun showMessage(text: String)
}
