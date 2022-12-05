package com.suonk.oc_project9.model.database.data

import com.suonk.oc_project9.domain.PhotoRepository
import com.suonk.oc_project9.domain.RealEstateRepository
import com.suonk.oc_project9.model.database.dao.PhotoDao
import com.suonk.oc_project9.model.database.dao.RealEstateDao
import com.suonk.oc_project9.model.database.data.entities.PhotoEntity
import com.suonk.oc_project9.model.database.data.entities.RealEstateEntity
import com.suonk.oc_project9.model.database.data.entities.RealEstateEntityWithPhotos
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoRepositoryImpl @Inject constructor(private val dao: PhotoDao) :
    PhotoRepository {

    override suspend fun insertPhoto(photo: PhotoEntity) = dao.insertPhoto(photo)
    override suspend fun deletePhoto(photo: PhotoEntity) = dao.deletePhoto(photo)
}