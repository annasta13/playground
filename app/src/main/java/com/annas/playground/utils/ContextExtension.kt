package com.annas.playground.utils

import android.content.Context
import android.widget.Toast

object ContextExtension {
    fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, length).show()
    }
}
