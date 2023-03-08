package com.suonk.oc_project9.domain

import androidx.lifecycle.LiveData
import com.suonk.oc_project9.model.database.data.entities.places.PointOfInterest
import kotlinx.coroutines.flow.MutableStateFlow

interface PlacesRepository {

    suspend fun getNearbyPlaceResponse(location: String): List<PointOfInterest>?
}