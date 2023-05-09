package com.suonk.oc_project9.domain.photo

import com.suonk.oc_project9.domain.PhotoRepository
import com.suonk.oc_project9.model.database.data.entities.real_estate.PhotoEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeletePhotoUseCase @Inject constructor(private val photoRepository: PhotoRepository) {

    suspend fun invoke(photoEntity: PhotoEntity) {
        photoRepository.deletePhoto(photoEntity)
    }
}