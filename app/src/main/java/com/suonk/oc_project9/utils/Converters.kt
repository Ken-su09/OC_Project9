package com.suonk.oc_project9.utils

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.*

class Converters {

    @TypeConverter
    fun fromLocalDateToLong(value: LocalDateTime?): Long? = value?.toEpochSecond(ZoneOffset.UTC)

    @TypeConverter
    fun fromLongToLocalDate(value: Long?): LocalDateTime? =
        value?.let { Instant.ofEpochSecond(it).atZone(ZoneOffset.UTC).toLocalDateTime() }
}