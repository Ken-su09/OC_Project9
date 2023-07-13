package com.suonk.oc_project9.ui.map

import com.suonk.oc_project9.model.database.data.entities.places.Position

data class MapViewState(
    val placeId: Long,
    val placeName: String,
    val latitude: Double,
    val longitude: Double,
    val markerIcon: Int,
    val list: List<Position>,
)