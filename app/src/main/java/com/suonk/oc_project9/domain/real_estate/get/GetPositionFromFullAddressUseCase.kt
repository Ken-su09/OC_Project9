package com.suonk.oc_project9.domain.real_estate.get

import android.content.Context
import android.location.Geocoder
import android.os.Build
import android.util.Log
import com.suonk.oc_project9.model.database.data.entities.places.Position
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetPositionFromFullAddressUseCase @Inject constructor(

) {

    fun invoke(fullAddress: String, context: Context): Position {
        var latitude = 0.0
        var longitude = 0.0

        if (Build.VERSION.SDK_INT >= 33) {
            Geocoder(context).getFromLocationName(fullAddress, 1) {
                latitude = it[0].latitude
                longitude = it[0].longitude
            }
        } else {
            if (Geocoder(context).getFromLocationName(fullAddress, 1)?.isNotEmpty() == true) {
                latitude = Geocoder(context).getFromLocationName(fullAddress, 1)?.get(0)?.latitude ?: 0.0
                longitude = Geocoder(context).getFromLocationName(fullAddress, 1)?.get(0)?.longitude ?: 0.0
            }
        }

        return Position(latitude, longitude)
    }
}