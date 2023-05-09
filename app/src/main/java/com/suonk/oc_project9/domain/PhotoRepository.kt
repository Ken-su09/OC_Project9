package com.suonk.oc_project9.domain

import com.suonk.oc_project9.model.database.data.entities.real_estate.PhotoEntity
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {

    suspend fun insertPhoto(photo: PhotoEntity)
    suspend fun deletePhoto(photo: PhotoEntity)

    fun getListOfPhotosByRealEstateId(id: Long): Flow<List<PhotoEntity>>
}