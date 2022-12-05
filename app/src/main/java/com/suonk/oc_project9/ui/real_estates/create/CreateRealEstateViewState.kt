package com.suonk.oc_project9.ui.real_estates.create

data class CreateRealEstateViewState(
    val id: Long = 0,
    val type: String,
    val price: String,
    val livingSpace: String,
    val numberRooms: String,
    val numberBedroom: String,
    val numberBathroom: String,
    val description: String,
    val photos: List<String>,
) {
}