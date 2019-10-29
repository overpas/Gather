package by.overpass.gather.commons.android

import android.widget.DatePicker
import java.util.*

fun DatePicker.date(): Date = Calendar.getInstance()
        .apply { set(year, month, dayOfMonth) }
        .time