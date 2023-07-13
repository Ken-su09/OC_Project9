package com.suonk.oc_project9.domain.places

import com.suonk.oc_project9.domain.PlacesRepository
import com.suonk.oc_project9.model.database.data.entities.places.PointOfInterest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetNearbyPointsOfInterestUseCase @Inject constructor(private val repository: PlacesRepository) {

    suspend fun invoke(lat: Double, long: Double): List<PointOfInterest> {
        return repository.getNearbyPlaceResponse("$lat,$long") ?: emptyList()
    }
}