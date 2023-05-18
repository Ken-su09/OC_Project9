package com.suonk.oc_project9.domain

import androidx.annotation.RequiresPermission
import com.suonk.oc_project9.model.database.data.entities.places.Position
import kotlinx.coroutines.flow.Flow

interface CurrentPositionRepository {

    fun getCurrentPositionFlow(): Flow<Position>

    @RequiresPermission(anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"])
    fun startLocationUpdates()

    fun stopLocationUpdates()
}