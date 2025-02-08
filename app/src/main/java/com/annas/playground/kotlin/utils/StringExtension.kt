package com.annas.playground.kotlin.utils

import com.annas.playground.kotlin.constants.StringConstant
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object StringExtension {
    fun String.isEmailValid(): Boolean {
        val pattern = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"
        return this.matches(pattern.toRegex())
    }

    fun String.toMilliseconds(
        pattern: String = StringConstant.DEFAULT_DATE_FORMAT,
        locale: Locale = Locale.getDefault()
    ): Long {
        val milliseconds = System.currentTimeMillis()
        val formatter = SimpleDateFormat(pattern, locale)
        return runCatching {
            val d: Date? = formatter.parse(this)
            d?.time ?: milliseconds
        }.getOrDefault(milliseconds)
    }
}
