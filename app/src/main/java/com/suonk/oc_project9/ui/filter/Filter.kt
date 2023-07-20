package com.suonk.oc_project9.ui.filter

import android.util.Log
import com.suonk.oc_project9.model.database.data.entities.real_estate.RealEstateEntityWithPhotos
import java.math.BigDecimal
import java.time.LocalDateTime

sealed class Filter {

    abstract fun isMatching(entity: RealEstateEntityWithPhotos): Boolean

    data class LivingSpaceFilter(val min: Double?, val max: Double?) : Filter() {
        override fun isMatching(entity: RealEstateEntityWithPhotos): Boolean {
            return when {
                min == null && max != null -> entity.realEstateEntity.livingSpace <= max
                max == null && min != null -> entity.realEstateEntity.livingSpace >= min
                max != null && min != null -> entity.realEstateEntity.livingSpace in min..max
                else -> true
            }
        }
    }

    data class PriceFilter(val min: BigDecimal?, val max: BigDecimal?) : Filter() {
        override fun isMatching(entity: RealEstateEntityWithPhotos): Boolean = when {
            min == null && max != null -> entity.realEstateEntity.price <= max
            max == null && min != null -> entity.realEstateEntity.price >= min
            max != null && min != null -> entity.realEstateEntity.price in min..max
            else -> true
        }
    }

    data class NbRoomsFilter(val min: Int?, val max: Int?) : Filter() {
        override fun isMatching(entity: RealEstateEntityWithPhotos): Boolean = when {
            min == null && max != null -> entity.realEstateEntity.numberRooms <= max
            max == null && min != null -> entity.realEstateEntity.numberRooms >= min
            max != null && min != null -> entity.realEstateEntity.numberRooms in min..max
            else -> true
        }
    }

    data class NbBedroomsFilter(val min: Int?, val max: Int?) : Filter() {
        override fun isMatching(entity: RealEstateEntityWithPhotos): Boolean = when {
            min == null && max != null -> entity.realEstateEntity.numberBedroom <= max
            max == null && min != null -> entity.realEstateEntity.numberBedroom >= min
            max != null && min != null -> entity.realEstateEntity.numberBedroom in min..max
            else -> true
        }
    }

    data class NbBathroomsFilter(val min: Int?, val max: Int?) : Filter() {
        override fun isMatching(entity: RealEstateEntityWithPhotos): Boolean = when {
            min == null && max != null -> entity.realEstateEntity.numberBathroom <= max
            max == null && min != null -> entity.realEstateEntity.numberBathroom >= min
            max != null && min != null -> entity.realEstateEntity.numberBathroom in min..max
            else -> true
        }
    }

    data class EntryDateFilter(val from: LocalDateTime?, val to: LocalDateTime?) : Filter() {
        override fun isMatching(entity: RealEstateEntityWithPhotos): Boolean {
            return when {
                from == null && to != null -> entity.realEstateEntity.entryDate.isBefore(to)
                to == null && from != null -> entity.realEstateEntity.entryDate.isAfter(from)
                from != null && to != null -> entity.realEstateEntity.entryDate.isBefore(to) && entity.realEstateEntity.entryDate.isAfter(from)
                else -> true
            }
        }
    }

    data class SaleDateFilter(val from: LocalDateTime?, val to: LocalDateTime?) : Filter() {
        override fun isMatching(entity: RealEstateEntityWithPhotos): Boolean = when {
            from == null && to != null -> entity.realEstateEntity.saleDate?.isBefore(to) == true
            to == null && from != null -> entity.realEstateEntity.saleDate?.isAfter(from) == true
            from != null && to != null -> entity.realEstateEntity.saleDate?.isBefore(to) == true && entity.realEstateEntity.saleDate.isAfter(
                from
            )
            else -> true
        }
    }
}