package com.suonk.oc_project9.model.database.data.entities.places

data class NearbyPlaceResult(
    val business_status: String,
    val geometry: Geometry,
    val icon: String,
    val icon_background_color: String,
    val icon_mask_base_uri: String,
    val name: String,
    val openingHours: OpeningHours?,
    val permanently_closed: Boolean?,
    val photos: List<Photo>?,
    val placeId: String?,
    val plus_code: PlusCode,
    val price_level: Int,
    val rating: Double,
    val reference: String,
    val scope: String,
    val types: List<String>,
    val user_ratings_total: Int,
    val vicinity: String
)