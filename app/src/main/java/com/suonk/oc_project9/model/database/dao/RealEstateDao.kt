package com.suonk.oc_project9.model.database.dao

import android.database.Cursor
import androidx.room.Query
import androidx.room.Dao
import androidx.room.Transaction
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import androidx.room.OnConflictStrategy
import com.suonk.oc_project9.model.database.data.entities.real_estate.RealEstateEntity
import com.suonk.oc_project9.model.database.data.entities.real_estate.RealEstateEntityWithPhotos
import kotlinx.coroutines.flow.Flow

@Dao
interface RealEstateDao {

    //region ============================================= GET ==============================================

    @Transaction
    @Query("SELECT * FROM real_estate ORDER BY id ASC")
    fun getAllRealEstatesWithPhotosCursor(): Cursor

    @Transaction
    @Query("SELECT * FROM real_estate ORDER BY id ASC")
    fun getAllRealEstatesWithPhotos(): Flow<List<RealEstateEntityWithPhotos>>

    @Transaction
    @Query("SELECT * FROM real_estate ORDER BY price DESC")
    fun getAllRealEstatesWithPhotosSortByPrice(): Flow<List<RealEstateEntityWithPhotos>>

    @Transaction
    @Query("SELECT * FROM real_estate WHERE id = :id LIMIT 1")
    fun getRealEstateById(id: Long): Flow<RealEstateEntityWithPhotos?>

    //endregion

    //region ============================================ INSERT ============================================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRealEstate(realEstate: RealEstateEntity): Long

    //endregion

    //region ============================================ UPDATE ============================================

    @Update
    suspend fun updateRealEstate(realEstate: RealEstateEntity)

    //endregion

    //region ============================================ DELETE ============================================

    @Delete
    suspend fun deleteRealEstate(realEstate: RealEstateEntity)

    @Query("DELETE from real_estate WHERE id = :id")
    suspend fun deleteRealEstateById(id: Long)

    //endregion
}