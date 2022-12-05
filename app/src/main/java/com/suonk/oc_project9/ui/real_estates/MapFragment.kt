package com.suonk.oc_project9.ui.real_estates

import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : SupportMapFragment() {

    private var googleMap: GoogleMap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupGoogleMap()
    }

    private fun setupGoogleMap() {
        getMapAsync { map ->
            googleMap = map

            googleMap?.let {
                val sydney = LatLng(-34.0, 151.0)
                it.addMarker(
                    MarkerOptions()
                        .position(sydney)
                        .title("Marker in Sydney")
                )
                it.moveCamera(CameraUpdateFactory.newLatLng(sydney))
            }
        }
    }
}