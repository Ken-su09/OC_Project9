package com.suonk.oc_project9.ui.real_estates.list

import com.suonk.oc_project9.utils.EquatableCallback

data class RealEstatesListViewState(
    val id: Long,
    val type: String,
    val price: String,
    val numberRooms: String,
    val livingSpace: String,
    val description: String,
    val photos: List<ListPhotoViewState>,
    val address: String,
    val entryDate: String,
    val saleDate: String,
    val isSold: Boolean,
    val onClickedCallback: EquatableCallback,
)