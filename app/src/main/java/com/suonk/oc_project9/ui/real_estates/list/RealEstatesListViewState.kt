package com.suonk.oc_project9.ui.real_estates.list

import com.suonk.oc_project9.utils.EquatableCallback

data class RealEstatesListViewState(
    val id: Long,
    val type: String,
    val price: String,
    val priceValue: Double,
    val numberRooms: String,
    val numberRoomsValue: Int,
    val livingSpace: String,
    val livingSpaceValue: Double,
    val description: String,
    val photos: List<ListPhotoViewState>,
    val address: String,
    val entryDate: String,
    val saleDate: String,
    val isSold: Boolean,
    val onClickedCallback: EquatableCallback,
)