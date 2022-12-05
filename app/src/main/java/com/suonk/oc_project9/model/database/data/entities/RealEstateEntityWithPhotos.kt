package com.suonk.oc_project9.model.database.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

@Entity
data class RealEstateEntityWithPhotos(
    @Embedded
    val realEstateEntity: RealEstateEntity,
    @Relation(
        entity = PhotoEntity::class,
        parentColumn = "id",
        entityColumn = "realEstateId"
    )
    val photos: List<PhotoEntity>
)