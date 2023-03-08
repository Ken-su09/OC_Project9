package com.suonk.oc_project9.ui.filter

import com.suonk.oc_project9.model.database.data.entities.real_estate.RealEstateEntityWithPhotos
import java.time.LocalDateTime

sealed class Filter {
    abstract fun isMatching(entity: RealEstateEntityWithPhotos): Boolean

    data class LivingSpaceFilter(val min: Double?, val max: Double?) : Filter() {
        override fun isMatching(entity: RealEstateEntityWithPhotos): Boolean = when {
            min == null && max != null -> entity.realEstateEntity.livingSpace < max
            max == null && min != null -> entity.realEstateEntity.livingSpace > min
            max != null && min != null -> entity.realEstateEntity.livingSpace in min..max
            else -> true
        }
    }

    data class PriceFilter(val min: Double?, val max: Double?) : Filter() {
        override fun isMatching(entity: RealEstateEntityWithPhotos): Boolean = when {
            min == null && max != null -> {
                entity.realEstateEntity.price <= max
            }
            max == null && min != null -> {
                entity.realEstateEntity.price >= min
            }
            max != null && min != null -> {
                entity.realEstateEntity.price in min..max
            }
            else -> true
        }
    }

    data class NbRoomsFilter(val min: Int?, val max: Int?) : Filter() {
        override fun isMatching(entity: RealEstateEntityWithPhotos): Boolean = when {
            min == null && max != null -> entity.realEstateEntity.numberRooms < max
            max == null && min != null -> entity.realEstateEntity.numberRooms > min
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

    data class EntryDateFilter(val min: LocalDateTime?, val max: LocalDateTime?) : Filter() {
        override fun isMatching(entity: RealEstateEntityWithPhotos): Boolean = when {
            min == null && max != null -> entity.realEstateEntity.entryDate.isBefore(max)
            max == null && min != null -> entity.realEstateEntity.entryDate.isAfter(min)
            min != null && max != null -> entity.realEstateEntity.entryDate.isBefore(max) && entity.realEstateEntity.entryDate.isAfter(min)
            else -> true
        }
    }

    data class SaleDateFilter(val min: LocalDateTime?, val max: LocalDateTime?) : Filter() {
        override fun isMatching(entity: RealEstateEntityWithPhotos): Boolean = when {
            min == null && max != null -> entity.realEstateEntity.saleDate?.isBefore(max) == true
            max == null && min != null -> entity.realEstateEntity.saleDate?.isAfter(min) == true
            min != null && max != null -> entity.realEstateEntity.saleDate?.isBefore(max) == true && entity.realEstateEntity.saleDate.isAfter(
                min
            )
            else -> true
        }
    }
}