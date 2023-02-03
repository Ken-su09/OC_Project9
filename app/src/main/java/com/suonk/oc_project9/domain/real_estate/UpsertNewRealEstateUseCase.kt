package com.suonk.oc_project9.domain.real_estate

import android.net.Uri
import com.suonk.oc_project9.domain.PhotoRepository
import com.suonk.oc_project9.domain.RealEstateRepository
import com.suonk.oc_project9.model.database.data.entities.PhotoEntity
import com.suonk.oc_project9.model.database.data.entities.RealEstateEntity
import com.suonk.oc_project9.ui.real_estates.details.DetailsPhotoViewState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpsertNewRealEstateUseCase @Inject constructor(
    private val realEstateRepository: RealEstateRepository,
    private val photoRepository: PhotoRepository,
) {

    suspend fun invoke(realEstate: RealEstateEntity, photos: List<DetailsPhotoViewState>) {
        val id = realEstateRepository.upsertRealEstate(realEstate)
        photos.map { photo ->
            photoRepository.insertPhoto(PhotoEntity(0, id, photo.uri.toString()))
        }
    }
}