package com.suonk.oc_project9.utils

import androidx.room.TypeConverter
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class Converters {

    @TypeConverter
    fun fromLocalDateToLong(value: LocalDateTime?): Long? = value?.toEpochSecond(ZoneOffset.UTC)

    @TypeConverter
    fun fromLongToLocalDate(value: Long?): LocalDateTime? =
        value?.let { Instant.ofEpochSecond(it).atZone(ZoneOffset.UTC).toLocalDateTime() }

    @TypeConverter
    fun fromBigDecimalToString(value: BigDecimal): String = value.toString()

    @TypeConverter
    fun fromStringToBigDecimal(value: String): BigDecimal = BigDecimal(value)
}