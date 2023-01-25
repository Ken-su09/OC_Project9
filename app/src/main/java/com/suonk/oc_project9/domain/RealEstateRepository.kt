package com.suonk.oc_project9.domain

import com.suonk.oc_project9.model.database.data.entities.RealEstateEntity
import com.suonk.oc_project9.model.database.data.entities.RealEstateEntityWithPhotos
import kotlinx.coroutines.flow.Flow

interface RealEstateRepository {

    fun getAllRealEstatesWithPhotos(): Flow<List<RealEstateEntityWithPhotos>>
    fun getRealEstateById(id: Long): Flow<RealEstateEntityWithPhotos>

    suspend fun upsertRealEstate(estate: RealEstateEntity): Long

    //region ============================================ UPDATE ============================================

    suspend fun updateRealEstate(estate: RealEstateEntity)

    //endregion

    suspend fun deleteRealEstate(estate: RealEstateEntity)
    suspend fun deleteRealEstateById(id: Int)
}