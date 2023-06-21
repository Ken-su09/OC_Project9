package com.suonk.oc_project9.ui.real_estates.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.suonk.oc_project9.BuildConfig
import com.suonk.oc_project9.R
import com.suonk.oc_project9.databinding.AddNewPhotoBuilderBottomBinding
import com.suonk.oc_project9.databinding.FragmentRealEstateDetailsBinding
import com.suonk.oc_project9.ui.main.MainActivity
import com.suonk.oc_project9.ui.real_estates.details.point_of_interest.PointOfInterestListAdapter
import com.suonk.oc_project9.utils.showToast
import com.suonk.oc_project9.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import kotlin.math.abs

@AndroidEntryPoint
class RealEstateDetailsFragment : Fragment(R.layout.fragment_real_estate_details) {

    private val binding by viewBinding(FragmentRealEstateDetailsBinding::bind)
    private val viewModel by viewModels<RealEstateDetailsViewModel>()

    private var isUpdatingFromViewState = false
    private var imageUri: Uri? = null

    private val takePictureCallback = registerForActivityResult(ActivityResultContracts.TakePicture()) { successful ->
        if (successful) {
            imageUri?.let {
                viewModel.onNewPhotoAdded(it.toString())
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.map.onCreate(savedInstanceState)

        setupToolbar()
        setupSpinners()
        setupRealEstateDetails()

        viewModel.finishSavingSingleLiveEvent.observe(viewLifecycleOwner) {
            startActivity(Intent(requireActivity(), MainActivity::class.java))
            requireActivity().finish()
        }
        viewModel.toastMessageSingleLiveEvent.observe(viewLifecycleOwner) {
            it.showToast(requireContext())
        }
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

    //region ================================================================ TOOLBAR ===============================================================

    private fun setupToolbar() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.details_toolbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    android.R.id.home -> {
                        startActivity(Intent(requireActivity(), MainActivity::class.java))
                        true
                    }
                    R.id.action_change_map -> {
                        binding.map.isVisible = !binding.map.isVisible
                        if (binding.pointOfInterestList.isVisible) {
                            binding.pointOfInterestList.isVisible = !binding.pointOfInterestList.isVisible
                        }
                        true
                    }
                    R.id.action_show_point_of_interest -> {
                        binding.pointOfInterestList.isVisible = !binding.pointOfInterestList.isVisible
                        if (binding.map.isVisible) {
                            binding.map.isVisible = !binding.map.isVisible
                        }
                        true
                    }
                    R.id.action_save_real_estate -> {
                        viewModel.onSaveRealEstateButtonClicked(
                            type = binding.typeContent.selectedItemPosition,
                            price = binding.price.text?.toString() ?: "0.0",
                            livingSpace = binding.livingSpace.text?.toString() ?: "0.0",
                            numberRooms = binding.nbRooms.text?.toString() ?: "0",
                            numberBedroom = binding.nbBedrooms.text?.toString() ?: "0",
                            numberBathroom = binding.nbBathrooms.text?.toString() ?: "0",
                            description = binding.description.text?.toString() ?: "",
                            postalCode = binding.postalCode.text?.toString() ?: "",
                            state = binding.state.text?.toString() ?: "",
                            city = binding.cityBorough.text?.toString() ?: "",
                            streetName = binding.streetName.text?.toString() ?: "",
                            gridZone = binding.gridZone.text?.toString() ?: ""
                        )
                        true
                    }
                    R.id.action_add_image -> {
                        addNewImage()
                        true
                    }
                    R.id.action_is_sold -> {
                        viewModel.onSoldRealEstateClick()
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

    //region =============================================================== SETUP UI ===============================================================

    private fun setupRealEstateDetails() {
        viewModel.realEstateDetailsViewStateLiveData.observe(viewLifecycleOwner) { realEstate ->
            isUpdatingFromViewState = true

            binding.gridZone.setText(realEstate.gridZone)
            binding.streetName.setText(realEstate.streetName)
            binding.cityBorough.setText(realEstate.city)
            binding.state.setText(realEstate.state)
            binding.postalCode.setText(realEstate.postalCode)

            binding.price.setText(realEstate.price)
            binding.livingSpace.setText(realEstate.livingSpace)

            binding.nbRooms.setText(realEstate.numberRooms)
            binding.nbBedrooms.setText(realEstate.numberBedroom)
            binding.nbBathrooms.setText(realEstate.numberBathroom)

            binding.description.setText(realEstate.description)

            binding.typeContent.setSelection(realEstate.typePosition, true)

            setupViewPager(realEstate.photos)
            binding.noImagesIcon.isVisible = realEstate.noPhoto
            binding.noImagesTitle.isVisible = realEstate.noPhoto

            binding.isSoldIcon.isVisible = realEstate.isSold

            setupMap(realEstate.city, realEstate.latitude, realEstate.longitude)

            isUpdatingFromViewState = false

            val listAdapter = PointOfInterestListAdapter()
            listAdapter.submitList(realEstate.pointsOfInterestViewState)

            binding.pointOfInterestList.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL))
            binding.pointOfInterestList.adapter = listAdapter
        }
    }

    private fun setupViewPager(photos: List<DetailsPhotoViewState>) {
        val detailsSliderAdapter = DetailsSliderAdapter()
        detailsSliderAdapter.submitList(photos)
        binding.images.adapter = detailsSliderAdapter
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
            requireContext(), android.R.layout.simple_spinner_item, resources.getStringArray(R.array.types_array)
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

    //region ================================================================ IMAGE =================================================================

    private fun addNewImage() {
        val bottomSheetBinding = AddNewPhotoBuilderBottomBinding.inflate(layoutInflater, null, false)
        val builderBottom = BottomSheetDialog(requireContext())
        builderBottom.setContentView(bottomSheetBinding.root)

        bottomSheetBinding.galleryLayout.setOnClickListener {
            openGallery()
            builderBottom.dismiss()
        }

        bottomSheetBinding.cameraLayout.setOnClickListener {
            openCamera()
            builderBottom.dismiss()
        }

        builderBottom.show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }

    private fun openCamera() {
        imageUri = FileProvider.getUriForFile(
            requireContext(), BuildConfig.APPLICATION_ID + ".provider", File.createTempFile(
                "JPEG_", ".jpg", requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            )
        )

        takePictureCallback.launch(imageUri)
    }

    //endregion

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