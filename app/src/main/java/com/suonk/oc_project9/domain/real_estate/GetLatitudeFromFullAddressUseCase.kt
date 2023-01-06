package com.suonk.oc_project9.domain.real_estate

import android.content.Context
import android.location.Geocoder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetLatitudeFromFullAddressUseCase @Inject constructor() {

    fun invoke(fullAddress: String, context: Context): Position {
        return Geocoder(context).getFromLocationName(fullAddress, 1)[0].latitude
    }

    data class Position(
        val lat,
        val long
    )
}