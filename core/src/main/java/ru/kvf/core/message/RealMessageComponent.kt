package ru.kvf.core.message

import android.content.Context
import android.widget.Toast

class RealMessageComponent(
    private val context: Context
) : MessageComponent {
    override fun showMessage(text: String) {
        showToast(text)
    }

    override fun showMessage(textRes: Int, args: Any) {
        val text = context.resources.getString(textRes, args)
        showToast(text)
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
