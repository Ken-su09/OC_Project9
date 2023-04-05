package com.suonk.oc_project9.utils

import androidx.room.TypeConverter
import com.suonk.oc_project9.model.database.data.entities.places.PointOfInterest
import java.math.BigDecimal
import java.time.*

class Converters {

    @TypeConverter
    fun fromLocalDateToLong(value: LocalDateTime?): Long? = value?.toEpochSecond(ZoneOffset.UTC)

    @TypeConverter
    fun fromLongToLocalDate(value: Long?): LocalDateTime? =
        value?.let { Instant.ofEpochSecond(it).atZone(ZoneOffset.UTC).toLocalDateTime() }

    @TypeConverter
    fun fromBigDecimalToString(value: BigDecimal): String =
        value.toString()

    @TypeConverter
    fun fromStringToBigDecimal(value: String): BigDecimal =
        BigDecimal(value)
}