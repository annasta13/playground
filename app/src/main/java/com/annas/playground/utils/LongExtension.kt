package com.annas.playground.utils

import com.annas.playground.constants.StringConstant
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object LongExtension {
    fun Long.toDateString(pattern: String = StringConstant.DEFAULT_DATE_FORMAT): String {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        return formatter.format(Date(this))
    }
}
