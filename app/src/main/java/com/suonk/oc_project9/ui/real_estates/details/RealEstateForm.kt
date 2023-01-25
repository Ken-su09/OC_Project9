package com.suonk.oc_project9.ui.real_estates.details

import com.suonk.oc_project9.ui.real_estates.carousel.PhotoViewState

data class RealEstateForm(
    val id: Long? = null,
    val type: String,
    val typePosition: Int,
    val price: Double,
    val livingSpace: Double,
    val numberRooms: Int,
    val numberBedroom: Int,
    val numberBathroom: Int,
    val description: String,
    val photos: List<PhotoViewState>,
    val city: String,
    val postalCode: String,
    val state: String,
    val streetName: String,
    val gridZone: String,
)