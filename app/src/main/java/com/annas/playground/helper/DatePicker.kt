package com.annas.playground.helper

import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import androidx.compose.runtime.MutableState
import com.annas.playground.constants.StringConstant.DEFAULT_DATE_FORMAT
import com.annas.playground.utils.StringExtension.toMilliseconds
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DatePicker(private val context: Context, isDarkTheme: Boolean) {
    private val calendar = Calendar.getInstance()
    private val formatter = SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault())
    private val theme = if (isDarkTheme) android.R.style.Theme_Material_Dialog_Alert
    else android.R.style.Theme_Material_Light_Dialog_Alert

    fun showDatePicker(
        state: MutableState<String>,
        minDate: Long? = null,
        maxDate: Long? = null
    ) {
        val time = state.value.toMilliseconds(DEFAULT_DATE_FORMAT, Locale.getDefault())
        calendar.time = Date(time)
        val datePicker = DatePickerDialog(
            context, theme, { _, year, monthOfYear, dayOfMonth ->
                calendar.set(year, monthOfYear, dayOfMonth)
                val date = formatter.format(calendar.time)
                state.value = date
            },
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        )
        minDate?.let { datePicker.datePicker.minDate = it }
        maxDate?.let { datePicker.datePicker.maxDate = it }
        datePicker.show()
        datePicker.setOnCancelListener { dialog: DialogInterface? -> dialog?.dismiss() }
    }
}
