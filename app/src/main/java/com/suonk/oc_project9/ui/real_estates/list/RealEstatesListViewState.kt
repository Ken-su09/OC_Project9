package com.suonk.oc_project9.ui.real_estates.list

data class RealEstatesListViewState(
    val id: Long,
    val type: String,
    val price: String,
    val numberRooms: String,
    val livingSpace: String,
    val description: String,
    val photos: List<String>,
    val address: String,
    val onClickedCallback: (Long) -> Unit,
)