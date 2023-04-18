package com.suonk.oc_project9.domain.real_estate.upsert

import com.suonk.oc_project9.domain.PhotoRepository
import com.suonk.oc_project9.domain.RealEstateRepository
import com.suonk.oc_project9.model.database.data.entities.real_estate.PhotoEntity
import com.suonk.oc_project9.model.database.data.entities.real_estate.RealEstateEntity
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpsertNewRealEstateUseCase @Inject constructor(
    private val realEstateRepository: RealEstateRepository,
    private val photoRepository: PhotoRepository,
) {

    suspend fun invoke(realEstate: RealEstateEntity, photos: List<RealEstateDetailsViewModel.AggregatedPhoto>) {
        val id = realEstateRepository.upsertRealEstate(realEstate)
        photos.map { photo ->
            photoRepository.insertPhoto(PhotoEntity(0, id, photo.uri))
        }
    }
}