package com.suonk.oc_project9.model.database.dao

import androidx.room.*
import com.suonk.oc_project9.model.database.data.entities.real_estate.PhotoEntity
import com.suonk.oc_project9.model.database.data.entities.real_estate.RealEstateEntityWithPhotos
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Transaction
    @Query("SELECT * FROM photo_entity WHERE realEstateId = :id")
    fun getListOfPhotosByRealEstateId(id: Long): Flow<List<PhotoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: PhotoEntity)

    @Delete
    suspend fun deletePhoto(photo: PhotoEntity)
}