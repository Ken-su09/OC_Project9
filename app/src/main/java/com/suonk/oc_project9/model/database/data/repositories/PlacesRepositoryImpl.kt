package com.suonk.oc_project9.model.database.data.repositories

import com.suonk.oc_project9.api.PlacesApiService
import com.suonk.oc_project9.domain.PlacesRepository
import com.suonk.oc_project9.model.database.data.entities.places.PointOfInterest
import kotlinx.coroutines.CancellationException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesRepositoryImpl @Inject constructor(private val apiService: PlacesApiService) : PlacesRepository {

    override suspend fun getNearbyPlaceResponse(location: String): List<PointOfInterest>? {
        val response = try {
            apiService.getNearbyPlacesResponse(location)
        } catch (cancellationException: CancellationException) {
            throw cancellationException
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

        return response?.results?.map { nearbyPlaceResult ->
            PointOfInterest(
                id = nearbyPlaceResult.placeId ?: nearbyPlaceResult.name,
                name = nearbyPlaceResult.name,
                address = nearbyPlaceResult.vicinity,
                icon = if (nearbyPlaceResult.photos?.isEmpty() == true) {
                    ""
                } else {
                    nearbyPlaceResult.photos?.get(0)?.photoReference ?: ""
                },
                types = nearbyPlaceResult.types
            )
        }?.takeIf {
            it.isNotEmpty()
        }
    }
}