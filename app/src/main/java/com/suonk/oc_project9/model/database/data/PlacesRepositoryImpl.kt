
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

        return response?.results?.mapNotNull { nearbyPlaceResult ->
            PointOfInterest(
                id = nearbyPlaceResult.placeId ?: return@mapNotNull null,
                name = nearbyPlaceResult.name,
                address = nearbyPlaceResult.vicinity,
                icon = nearbyPlaceResult.icon,
                types = nearbyPlaceResult.types
            )
        }?.takeIf {
            it.isNotEmpty()
        }
    }
}