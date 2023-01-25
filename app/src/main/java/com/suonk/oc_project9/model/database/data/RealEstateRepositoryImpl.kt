package com.suonk.oc_project9.model.database.data

import com.suonk.oc_project9.domain.RealEstateRepository
import com.suonk.oc_project9.model.database.dao.RealEstateDao
import com.suonk.oc_project9.model.database.data.entities.RealEstateEntity
import com.suonk.oc_project9.model.database.data.entities.RealEstateEntityWithPhotos
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealEstateRepositoryImpl @Inject constructor(private val dao: RealEstateDao) :
    RealEstateRepository {

    override fun getAllRealEstatesWithPhotos(): Flow<List<RealEstateEntityWithPhotos>> =
        dao.getAllRealEstatesWithPhotos()

    override fun getRealEstateById(id: Long): Flow<RealEstateEntityWithPhotos> =
        dao.getRealEstateById(id)

    override suspend fun upsertRealEstate(estate: RealEstateEntity) = dao.upsertRealEstate(estate)

    //region ============================================ UPDATE ============================================

    override suspend fun updateRealEstate(estate: RealEstateEntity) = dao.updateRealEstate(estate)

    //endregion

    override suspend fun deleteRealEstate(estate: RealEstateEntity) = dao.deleteRealEstate(estate)

    override suspend fun deleteRealEstateById(id: Int) = dao.deleteRealEstateById(id)
}