package com.suonk.oc_project9.utils.sort

import com.suonk.oc_project9.model.database.data.entities.RealEstateEntityWithPhotos

enum class Sorting(val comparator: Comparator<RealEstateEntityWithPhotos>) {
    DATE_ASC(Comparator.comparing { it.realEstateEntity.entryDate }),
    DATE_DESC(Comparator.comparing<RealEstateEntityWithPhotos?, Long?> {
        it.realEstateEntity.entryDate
    }.reversed()),

    CHRONOLOGICAL_ASC(Comparator.comparing { it.realEstateEntity.id }), CHRONOLOGICAL_DESC(
        Comparator.comparing { it.realEstateEntity.id }),

    PRICE_ASC(Comparator.comparing { it.realEstateEntity.price }), PRICE_DESC(Comparator.comparing<RealEstateEntityWithPhotos?, Double?> { it.realEstateEntity.price }
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