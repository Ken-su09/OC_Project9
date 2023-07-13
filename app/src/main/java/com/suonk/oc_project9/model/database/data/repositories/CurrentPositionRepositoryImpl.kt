package com.suonk.oc_project9.model.database.data.repositories

import android.os.Looper
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.suonk.oc_project9.domain.CurrentPositionRepository
import com.suonk.oc_project9.model.database.data.entities.places.Position
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentPositionRepositoryImpl @Inject constructor(
    private val locationProviderClient: FusedLocationProviderClient, private val looper: Looper
) : CurrentPositionRepository {

    private val positionFlow = MutableStateFlow(Position(0.0, 0.0))

    private val locationRequest = LocationRequest.create().apply {
        interval = 120000 // 10 seconds
        fastestInterval = 120000 // 5 seconds
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }

    private var locationCallback: LocationCallback? = null

    override fun getCurrentPositionFlow() = positionFlow

    @RequiresPermission(anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"])
    override fun startLocationUpdates() {
        if (locationCallback == null) {
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    if (locationResult.lastLocation != null) {
                        positionFlow.update {
                            Position(locationResult.lastLocation?.latitude ?: 0.0, locationResult.lastLocation?.longitude ?: 0.0)
                        }
                    }
                }
            }
        }
        locationCallback?.let {
            locationProviderClient.requestLocationUpdates(locationRequest, it, looper)
        }
    }

    override fun stopLocationUpdates() {
        locationCallback?.let {
            locationProviderClient.removeLocationUpdates(it)
        }
    }
}