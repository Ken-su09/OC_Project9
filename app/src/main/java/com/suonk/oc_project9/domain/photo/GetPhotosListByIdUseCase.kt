package com.suonk.oc_project9.domain.photo

import com.suonk.oc_project9.domain.PhotoRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetPhotosListByIdUseCase @Inject constructor(private val photoRepository: PhotoRepository) {

    fun invoke(id: Long) = photoRepository.getListOfPhotosByRealEstateId(id)
}