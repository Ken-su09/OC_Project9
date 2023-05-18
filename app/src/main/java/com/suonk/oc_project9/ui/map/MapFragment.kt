package com.suonk.oc_project9.ui.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.suonk.oc_project9.R
import com.suonk.oc_project9.databinding.FragmentMapBinding
import com.suonk.oc_project9.databinding.FragmentRealEstateDetailsBinding
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewModel
import com.suonk.oc_project9.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map) {

    private val binding by viewBinding(FragmentMapBinding::bind)
    private val viewModel by viewModels<MapViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.map.onCreate(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        binding.map.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }


    //region ================================================================= MAP ==================================================================

    private fun setupMap(city: String, latitude: Double, longitude: Double) {
        binding.map.getMapAsync { googleMap ->
            googleMap.addMarker(
                MarkerOptions().position(LatLng(latitude, longitude)).title(city)
            )
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 15f))
        }
    }

    //endregion
}