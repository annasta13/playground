package com.annas.playground.utils

import com.annas.playground.constants.StringConstant
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object StringExtension {
    fun String.isEmailValid(): Boolean {
        val pattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"
        return this.matches(pattern.toRegex())
    }

    fun String.toMilliseconds(pattern: String = StringConstant.DEFAULT_DATE_FORMAT, locale: Locale = Locale.getDefault()): Long {
        var milliseconds = System.currentTimeMillis()
        val formatter = SimpleDateFormat(pattern, locale)
        try {
            val d: Date? = formatter.parse(this)
            milliseconds = d?.time ?: milliseconds
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return milliseconds
    }
}
