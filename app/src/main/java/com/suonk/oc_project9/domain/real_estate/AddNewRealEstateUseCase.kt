package com.suonk.oc_project9.domain.real_estate

import com.suonk.oc_project9.domain.PhotoRepository
import com.suonk.oc_project9.domain.RealEstateRepository
import com.suonk.oc_project9.model.database.data.entities.RealEstateEntityWithPhotos
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddNewRealEstateUseCase @Inject constructor(
    private val realEstateRepository: RealEstateRepository,
    private val photoRepository: PhotoRepository,
) {

    suspend fun invoke(realEstate: RealEstateEntityWithPhotos) {
        realEstateRepository.insertRealEstate(realEstate.realEstateEntity)
        realEstate.photos.map {
            photoRepository.insertPhoto(it)
        }
    }
}