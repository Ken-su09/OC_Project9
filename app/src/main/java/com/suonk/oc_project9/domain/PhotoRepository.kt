package com.suonk.oc_project9.domain

import com.suonk.oc_project9.model.database.data.entities.real_estate.PhotoEntity

interface PhotoRepository {

    suspend fun insertPhoto(photo: PhotoEntity)
    suspend fun deletePhoto(photo: PhotoEntity)
}