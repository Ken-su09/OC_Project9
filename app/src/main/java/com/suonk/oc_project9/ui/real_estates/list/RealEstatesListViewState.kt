package com.suonk.oc_project9.ui.real_estates.list

import com.suonk.oc_project9.ui.real_estates.carousel.PhotoViewState

data class RealEstatesListViewState(
    val id: Long,
    val type: String,
    val price: String,
    val numberRooms: String,
    val livingSpace: String,
    val description: String,
    val photos: List<PhotoViewState>,
    val address: String,
    val date: String,
    val onClickedCallback: (Long) -> Unit,
)