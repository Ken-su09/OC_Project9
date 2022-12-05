package com.suonk.oc_project9.ui.real_estates.details

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.suonk.oc_project9.R
import com.suonk.oc_project9.databinding.FragmentRealEstateDetailsBinding
import com.suonk.oc_project9.ui.main.MainViewModel
import com.suonk.oc_project9.ui.real_estates.create.CreateRealEstateFragmentDirections
import com.suonk.oc_project9.ui.real_estates.list.SliderAdapter
import com.suonk.oc_project9.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class RealEstateDetailsFragment : Fragment(R.layout.fragment_real_estate_details) {

    private val viewModel by viewModels<RealEstateDetailsViewModel>()
    private val binding by viewBinding(FragmentRealEstateDetailsBinding::bind)
    private val args: RealEstateDetailsFragmentArgs by navArgs()

    private var listOfPhotos = arrayListOf<String>()
    private var latitude = 0.0
    private var longitude = 0.0

    private var map: GoogleMap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.map.onCreate(savedInstanceState)

        setupToolbar()
        setupRealEstateDetails()
        setupSpinners()
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }

    override fun onStart() {
        super.onStart()
        binding.map.onStart()
    }

    //region =========================================== TOOLBAR ============================================

    private fun setupToolbar() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.details_toolbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_change_map -> {
                        binding.map.isVisible = !binding.map.isVisible
                        true
                    }
                    R.id.action_add_image -> {

                        true
                    }
                    R.id.action_save_real_estate -> {
                        saveRealEstateDetails()
                        findNavController().navigate(RealEstateDetailsFragmentDirections.actionDetailsToList())
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    //endregion

    //region =========================================== SETUP UI ===========================================

    private fun setupRealEstateDetails() {
        viewModel.realEstateDetailsViewStateLiveData.observe(viewLifecycleOwner) { realEstate ->
            binding.gridZone.editText?.setText(realEstate.gridZone)
            binding.streetName.editText?.setText(realEstate.streetName)
            binding.cityBorough.editText?.setText(realEstate.city)
            binding.state.editText?.setText(realEstate.state)
            binding.postalCode.editText?.setText(realEstate.postalCode)

            binding.price.editText?.setText(realEstate.price)
            binding.livingSpace.editText?.setText(realEstate.livingSpace)

            binding.nbRooms.editText?.setText(realEstate.numberRooms)
            binding.nbBedrooms.editText?.setText(realEstate.numberBedroom)
            binding.nbBathrooms.editText?.setText(realEstate.numberBathroom)

            binding.description.editText?.setText(realEstate.description)

            binding.typeContent.setSelection(realEstate.typePosition, true)

            setupViewPager(realEstate.photos)
            listOfPhotos.addAll(realEstate.photos)

            latitude = realEstate.latitude
            longitude = realEstate.longitude
            setupMap(realEstate.city, realEstate.latitude, realEstate.longitude)
        }
    }

    private fun setupViewPager(photos: List<String>) {
        val sliderAdapter = SliderAdapter()
        sliderAdapter.submitList(photos)
        binding.images.adapter = sliderAdapter
        binding.images.clipToPadding = false
        binding.images.clipChildren = false

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(30))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.25f
        }

        binding.images.setPageTransformer(compositePageTransformer)
        binding.images.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {})
    }

    private fun setupSpinners() {
        val typeArrayAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.types_array)
        )
        binding.typeContent.adapter = typeArrayAdapter
        binding.typeContent.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
    }

    //endregion

    //region =========================================== SETUP MAP ==========================================

    private fun setupMap(city: String, latitude: Double, longitude: Double) {
        MapsInitializer.initialize(requireActivity())
        binding.map.getMapAsync { googleMap ->
            map = googleMap

            map?.let {
                it.addMarker(
                    MarkerOptions()
                        .position(LatLng(latitude, longitude))
                        .title(city)
                )
                it.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 15f))
            }
        }
    }

    //endregion

    private fun saveRealEstateDetails() {
        viewModel.saveRealEstateDetails(
            estate = RealEstateDetailsViewState(
                id = args.id,
                type = binding.typeContent.selectedItem.toString(),
                typePosition = binding.typeContent.selectedItemPosition,
                price = binding.price.editText?.text?.toString().toString(),
                livingSpace = binding.livingSpace.editText?.text?.toString().toString(),
                numberRooms = binding.nbRooms.editText?.text?.toString().toString(),
                numberBedroom = binding.nbBedrooms.editText?.text?.toString().toString(),
                numberBathroom = binding.nbBathrooms.editText?.text?.toString().toString(),
                description = binding.description.editText?.text?.toString().toString(),
                photos = listOfPhotos,
                city = binding.cityBorough.editText?.text?.toString().toString(),
                postalCode = binding.postalCode.editText?.text?.toString().toString(),
                state = binding.state.editText?.text?.toString().toString(),
                streetName = binding.streetName.editText?.text?.toString().toString(),
                gridZone = binding.gridZone.editText?.text?.toString().toString(),
                latitude = latitude,
                longitude = longitude,
            )
        )
    }
}