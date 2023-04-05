package com.suonk.oc_project9.model.database.dao

import androidx.room.*
import com.suonk.oc_project9.model.database.data.entities.real_estate.RealEstateEntity
import com.suonk.oc_project9.model.database.data.entities.real_estate.RealEstateEntityWithPhotos
import kotlinx.coroutines.flow.Flow

@Dao
interface RealEstateDao {

    //region ============================================= GET ==============================================

    @Transaction
    @Query("SELECT * FROM real_estate ORDER BY id ASC")
    fun getAllRealEstatesWithPhotos(): Flow<List<RealEstateEntityWithPhotos>>

//    @Query("SELECT * FROM real_estate WHERE BLABLA from ORDER BY id ASC OFFSET 0 LIMIT 30")

    @Transaction
    @Query("SELECT * FROM real_estate ORDER BY price DESC")
    fun getAllRealEstatesWithPhotosSortByPrice(): Flow<List<RealEstateEntityWithPhotos>>

//    @Transaction
//    @Query("SELECT * FROM real_estate WHERE ")
//    fun getAllRealEstatesWithPhotosWithInput(input: String): Flow<List<RealEstateEntityWithPhotos>>

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
    suspend fun deleteRealEstateById(id: Int)

    //endregion
}