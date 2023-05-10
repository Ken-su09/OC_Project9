package com.suonk.oc_project9.model.database.data

import com.suonk.oc_project9.domain.PhotoRepository
import com.suonk.oc_project9.model.database.dao.PhotoDao
import com.suonk.oc_project9.model.database.data.entities.real_estate.PhotoEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoRepositoryImpl @Inject constructor(private val dao: PhotoDao) : PhotoRepository {

    override suspend fun insertPhoto(photo: PhotoEntity) = dao.insertPhoto(photo)
    override suspend fun deletePhoto(photo: PhotoEntity) = dao.deletePhoto(photo)

    override fun getListOfPhotosByRealEstateId(id: Long) = dao.getListOfPhotosByRealEstateId(id)
}