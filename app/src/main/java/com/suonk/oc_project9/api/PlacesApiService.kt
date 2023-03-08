package com.suonk.oc_project9.api

import com.suonk.oc_project9.BuildConfig
import com.suonk.oc_project9.model.database.data.entities.places.NearbyPlaceResponse
import dagger.Provides
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApiService {

    @GET("maps/api/place/nearbysearch/json?" + "radius=2000" + "&key=" + BuildConfig.PLACES_API_KEY)
    suspend fun getNearbyPlacesResponse(@Query("location") location: String): NearbyPlaceResponse

    @GET("maps/api/place/nearbysearch/json?" + "radius=2000" + "&key=" + BuildConfig.PLACES_API_KEY)
    fun getNearbyPlacesResponseBis(@Query("location") location: String): Call<NearbyPlaceResponse?>
}