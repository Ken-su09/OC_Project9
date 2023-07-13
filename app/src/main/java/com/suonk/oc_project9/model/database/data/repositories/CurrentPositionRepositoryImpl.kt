package com.suonk.oc_project9.model.database.data.repositories

import androidx.annotation.RequiresPermission
import com.google.android.gms.location.*
import com.suonk.oc_project9.domain.CurrentPositionRepository
import com.suonk.oc_project9.model.database.data.entities.places.Position
import com.suonk.oc_project9.utils.CoroutineDispatcherProvider
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.minutes

@Singleton
class CurrentPositionRepositoryImpl @Inject constructor(
    private val locationProviderClient: FusedLocationProviderClient,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) : CurrentPositionRepository {

    private val positionFlow = MutableStateFlow<Position?>(null)

    private val locationRequest = LocationRequest.Builder(2.minutes.inWholeMilliseconds)
        .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
        .build()

    private var locationCallback: LocationCallback? = null

    override fun getCurrentPositionFlow() = positionFlow.filterNotNull()

    @RequiresPermission(anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"])
    override fun startLocationUpdates() {
        if (locationCallback == null) {
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    positionFlow.value = Position(
                        locationResult.lastLocation?.latitude ?: return,
                        locationResult.lastLocation?.longitude ?: return
                    )
                }
            }
            locationCallback?.let {
                locationProviderClient.requestLocationUpdates(
                    locationRequest,
                    coroutineDispatcherProvider.io.asExecutor(),
                    it
                )
            }
        }
    }

    override fun stopLocationUpdates() {
        locationCallback?.let {
            locationProviderClient.removeLocationUpdates(it)
        }
    }
}