package com.suonk.oc_project9.domain.real_estate

import android.content.Context
import android.location.Geocoder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetLongitudeFromFullAddressUseCase @Inject constructor() {

    fun invoke(fullAddress: String, context: Context): Double {
        return Geocoder(context).getFromLocationName(fullAddress, 1)[0].longitude
    }
}