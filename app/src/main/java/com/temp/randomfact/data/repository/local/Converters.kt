package com.temp.randomfact.data.repository.local

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): Date? {
        return if (value.isNullOrEmpty()) null else SimpleDateFormat.getDateInstance().parse(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): String {
        return date?.let { SimpleDateFormat.getDateInstance().format(it) } ?: ""
    }
}