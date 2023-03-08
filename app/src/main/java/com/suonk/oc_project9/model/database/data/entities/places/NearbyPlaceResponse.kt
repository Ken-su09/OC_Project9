package com.suonk.oc_project9.model.database.data.entities.places

import com.google.gson.annotations.SerializedName
import java.util.*

data class NearbyPlaceResponse(
    @field:SerializedName("html_attributions") private val htmlAttributions: List<com.google.protobuf.Any>,
    @field:SerializedName("next_page_token") private val nextPageToken: String,
    @field:SerializedName("results") val results: List<NearbyPlaceResult>,
    @field:SerializedName("status") private val status: String
)