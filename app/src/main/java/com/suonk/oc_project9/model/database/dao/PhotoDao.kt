package com.suonk.oc_project9.model.database.dao

import androidx.room.*
import com.suonk.oc_project9.model.database.data.entities.real_estate.PhotoEntity

@Dao
interface PhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: PhotoEntity)

    @Delete
    suspend fun deletePhoto(photo: PhotoEntity)
}