package com.suonk.oc_project9.ui.real_estates.details

import com.suonk.oc_project9.model.database.data.entities.places.PointOfInterest

data class RealEstateDetailsViewState(
    val id: Long = 0L,
    val type: String,
    val typePosition: Int,
    val price: String,
    val livingSpace: String,
    val numberRooms: String,
    val numberBedroom: String,
    val numberBathroom: String,
    val description: String,
    val photos: List<DetailsPhotoViewState>,
    val city: String,
    val postalCode: String,
    val state: String,
    val streetName: String,
    val gridZone: String,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val noPhoto: Boolean,
    val entryDate: Long,
    val saleDate: Long?,
    val isSold: Boolean,
    val pointsOfInterest: List<PointOfInterest>
)