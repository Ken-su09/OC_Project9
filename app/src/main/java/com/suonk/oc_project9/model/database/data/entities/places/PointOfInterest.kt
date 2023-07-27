package com.suonk.oc_project9.model.database.data.entities.places

data class PointOfInterest(
    val id: String?,
    val name: String,
    val address: String,
    val types: List<String>
)