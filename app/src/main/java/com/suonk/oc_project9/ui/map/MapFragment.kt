package com.suonk.oc_project9.ui.map

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.suonk.oc_project9.R
import com.suonk.oc_project9.databinding.FragmentMapBinding
import com.suonk.oc_project9.ui.loan_simulator.LoanSimulatorFragment
import com.suonk.oc_project9.ui.main.MainActivity
import com.suonk.oc_project9.ui.real_estates.list.RealEstatesListFragment
import com.suonk.oc_project9.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {

    private val binding by viewBinding(FragmentMapBinding::bind)
    private val viewModel by viewModels<MapViewModel>()

    private lateinit var googleMap: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.map.onCreate(savedInstanceState)
        binding.map.getMapAsync(this)

        setupBottomNavigationView()
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

    override fun onLowMemory() {
        super.onLowMemory()
        binding.map.onLowMemory()
    }


    private fun setupBottomNavigationView() {
        binding.bottomNavigation.menu.findItem(R.id.nav_map).isChecked = true
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            val fragmentToCommit = when (menuItem.itemId) {
                R.id.nav_real_estate -> {
                    RealEstatesListFragment()
                }
                R.id.nav_map -> {
                    MapFragment()
                }
                R.id.nav_loan_simulator -> {
                    LoanSimulatorFragment()
                }
                else -> {
                    RealEstatesListFragment()
                }
            }

            replaceFragment(fragmentToCommit, "RealEstatesListFragment / MapFragment")

            true
        }
    }

    //region ================================================================= MAP ==================================================================

    private fun jumpToLocation(location: LatLng) {
        binding.jumpTo.setOnClickListener {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 15f))
        }
    }

    //endregion

    private fun replaceFragment(fragment: Fragment, tag: String) {
        if (requireActivity() is MainActivity) {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, tag).addToBackStack(null)
                .commit()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        viewModel.mapViewStateLiveData.observe(viewLifecycleOwner) {
            jumpToLocation(LatLng(it.latitude, it.longitude))
            map.addMarker(MarkerOptions().position(LatLng(it.latitude, it.longitude)).title(""))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 15f))

            it.list.map { position ->
                map.addMarker(
                    MarkerOptions().position(LatLng(position.lat, position.long)).title("")
                )
            }
        }
    }
}