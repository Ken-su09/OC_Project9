package com.suonk.oc_project9.ui.filter

data class SearchViewState(
    val livingSpaceMin: String,
    val livingSpaceMax: String,
    val priceMin: String,
    val priceMax: String,
    val nbRoomsMin: String,
    val nbRoomsMax: String,
    val nbBedroomsMin: String,
    val nbBedroomsMax: String,
    val entryDateMin: String,
    val entryDateMax: String,
    val saleDateMin: String,
    val saleDateMax: String,
)