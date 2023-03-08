package com.suonk.oc_project9.model.database.data.entities.real_estate

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val realEstateId: Long,
    val photo: String
)