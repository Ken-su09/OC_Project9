package com.suonk.oc_project9.utils.sort

import com.suonk.oc_project9.model.database.data.entities.real_estate.RealEstateEntityWithPhotos
import java.math.BigDecimal
import java.time.LocalDateTime

enum class Sorting(val comparator: Comparator<RealEstateEntityWithPhotos>) {
    DATE_ASC(Comparator.comparing { it.realEstateEntity.entryDate }),
    DATE_DESC(Comparator.comparing<RealEstateEntityWithPhotos?, LocalDateTime?> {
        it.realEstateEntity.entryDate
    }.reversed()),

    PRICE_ASC(Comparator.comparing { it.realEstateEntity.price }), PRICE_DESC(Comparator.comparing<RealEstateEntityWithPhotos?, BigDecimal?> { it.realEstateEntity.price }
        .reversed()),

    LIVING_SPACE_ASC(Comparator.comparing { it.realEstateEntity.livingSpace }), LIVING_SPACE_DESC(
        Comparator.comparing<RealEstateEntityWithPhotos?, Double?> { it.realEstateEntity.livingSpace }
            .reversed()),

    ROOMS_NUMBER_ASC(Comparator.comparing {
        it.realEstateEntity.numberRooms

    }),
    ROOMS_NUMBER_DESC(Comparator.comparing<RealEstateEntityWithPhotos?, Int?> { it.realEstateEntity.numberRooms }
        .reversed()),
}