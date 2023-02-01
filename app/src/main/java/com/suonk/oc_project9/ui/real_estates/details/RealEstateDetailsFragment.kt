package com.suonk.oc_project9.ui.real_estates.details

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.suonk.oc_project9.R
import com.suonk.oc_project9.databinding.BuilderBottomLayoutBinding
import com.suonk.oc_project9.databinding.FragmentRealEstateDetailsBinding
import com.suonk.oc_project9.ui.real_estates.carousel.PhotoViewState
import com.suonk.oc_project9.ui.real_estates.carousel.SliderAdapter
import com.suonk.oc_project9.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class RealEstateDetailsFragment : Fragment(R.layout.fragment_real_estate_details) {

    private val binding by viewBinding(FragmentRealEstateDetailsBinding::bind)

    private val viewModel by viewModels<RealEstateDetailsViewModel>()

    private var isUpdatingFromViewState = false
    private var map: GoogleMap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.map.onCreate(savedInstanceState)

        setupToolbar()
        setupSpinners()
        setupRealEstateDetails()
        setupDoAfterDataChanged()

        viewModel.finishSavingSingleLiveEvent.observe(viewLifecycleOwner) {
            findNavController().navigate(RealEstateDetailsFragmentDirections.actionDetailsToList())
        }
        viewModel.isFieldEmptySingleLiveEvent.observe(viewLifecycleOwner) {
            it?.let { isError ->
                if (isError) {
                    Toast.makeText(requireContext(), requireContext().getString(R.string.field_empty_toast_msg), Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.toastMessageSingleLiveEvent.observe(viewLifecycleOwner) {
            it?.let { toastMessage ->
                Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
            }
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
//                viewModel.isFieldEmptySingleLiveEvent
                menuInflater.inflate(R.menu.details_toolbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_change_map -> {
                        binding.map.isVisible = !binding.map.isVisible
                        true
                    }
                    R.id.action_save_real_estate -> {
                        viewModel.onSaveRealEstateButtonClicked()

                        viewModel.isFieldEmptySingleLiveEvent.observe(viewLifecycleOwner) {
                            it?.let { isError ->
                                if (!isError) {
                                    findNavController().navigate(RealEstateDetailsFragmentDirections.actionDetailsToList())
                                } else {
                                    Toast.makeText(
                                        requireContext(), requireContext().getString(R.string.field_empty_toast_msg), Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                        true
                    }
                    R.id.action_add_image -> {
                        addNewImage()
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
//        var cpt = 0
        viewModel.realEstateDetailsViewStateLiveData.observe(viewLifecycleOwner) { realEstate ->
//            Log.i("RealEstateFlow", "Passe par là : ${cpt++}")

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
            binding.deletePhoto.isVisible = !realEstate.noPhoto

            setupMap(realEstate.city, realEstate.latitude, realEstate.longitude)

            isUpdatingFromViewState = false
        }
    }

    private fun setupViewPager(photos: List<PhotoViewState>) {
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
        binding.images.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)

                binding.deletePhoto.setOnClickListener {

                }
            }
        })
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
        val bottomSheetBinding = BuilderBottomLayoutBinding.inflate(layoutInflater, null, false)
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
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(intent)
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data

            data?.data?.let {
                Log.i("ConvertToBitmap", "it (Uri) : $it")
                viewModel.onNewPhotoAdded(it)
            }

            data?.extras?.let {
//                it.get("data") as Bitmap

//                viewModel.onNewPhotoAdded(it.get("data") as Bitmap)

//            data?.data?.let {
//                logo.setImageBitmap()
//                Log.i("RealEstateFlow", "Photo : $it")
//                viewModel.onNewPhotoAdded(it)
//            }
            }
        }
    }

    //endregion

    //region ================================================================= MAP ==================================================================

    private fun setupMap(city: String, latitude: Double, longitude: Double) {
        MapsInitializer.initialize(requireActivity())
        binding.map.getMapAsync { googleMap ->
            map = googleMap

            map?.let {
                it.addMarker(
                    MarkerOptions().position(LatLng(latitude, longitude)).title(city)
                )
                it.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 15f))
            }
        }
    }

    //endregion

    //region ============================================================= DATA CHANGED =============================================================

    private fun setupDoAfterDataChanged() {
        binding.typeContent.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (!isUpdatingFromViewState) {
                    viewModel.onTypeChanged(position)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        binding.price.doAfterTextChanged {
            if (!isUpdatingFromViewState) {
                viewModel.onPriceChanged(it?.toString())
            }
        }
        binding.livingSpace.doAfterTextChanged {
            if (!isUpdatingFromViewState) {
                viewModel.onLivingSpaceChanged(it?.toString())
            }
        }
        binding.nbRooms.doAfterTextChanged {
            if (!isUpdatingFromViewState) {
                viewModel.onNumberRoomsChanged(it?.toString())
            }
        }
        binding.nbBedrooms.doAfterTextChanged {
            if (!isUpdatingFromViewState) {
                viewModel.onNumberBedroomsChanged(it?.toString())
            }
        }
        binding.nbBathrooms.doAfterTextChanged {
            if (!isUpdatingFromViewState) {
                viewModel.onNumberBathroomsChanged(it?.toString())
            }
        }
        binding.description.doAfterTextChanged {
            if (!isUpdatingFromViewState) {
                viewModel.onDescriptionChanged(it?.toString())
            }
        }
        binding.cityBorough.doAfterTextChanged {
            if (!isUpdatingFromViewState) {
                viewModel.onCityChanged(it?.toString())
            }
        }
        binding.postalCode.doAfterTextChanged {
            if (!isUpdatingFromViewState) {
                viewModel.onPostalCodeChanged(it?.toString())
            }
        }
        binding.state.doAfterTextChanged {
            if (!isUpdatingFromViewState) {
                viewModel.onStateChanged(it?.toString())
            }
        }
        binding.streetName.doAfterTextChanged {
            if (!isUpdatingFromViewState) {
                viewModel.onStreetNameChanged(it?.toString())
            }
        }
        binding.gridZone.doAfterTextChanged {
            if (!isUpdatingFromViewState) {
                viewModel.onGridZoneChanged(it?.toString())
            }
        }
    }

    private fun addNewRealEstate() {
//            estate = RealEstateDetailsViewState(
//                id = 0,
//                type = binding.typeContent.selectedItem.toString(),
//                typePosition = binding.typeContent.selectedItemPosition,
//                price = binding.price.editText?.text?.toString().toString(),
//                livingSpace = binding.livingSpace.editText?.text?.toString().toString(),
//                numberRooms = binding.nbRooms.editText?.text?.toString().toString(),
//                numberBedroom = binding.nbBedrooms.editText?.text?.toString().toString(),
//                numberBathroom = binding.nbBathrooms.editText?.text?.toString().toString(),
//                description = binding.description.editText?.text?.toString().toString(),
//                photos = currentList,
//                city = binding.cityBorough.editText?.text?.toString().toString(),
//                postalCode = binding.postalCode.editText?.text?.toString().toString(),
//                state = binding.state.editText?.text?.toString().toString(),
//                streetName = binding.streetName.editText?.text?.toString().toString(),
//                gridZone = binding.gridZone.editText?.text?.toString().toString(),
//            )
    }

    private fun saveRealEstateDetails() {
//        viewModel.saveRealEstateDetails(
//            estate = RealEstateDetailsViewState(
//                id = args.id,
//                type = binding.typeContent.selectedItem.toString(),
//                typePosition = binding.typeContent.selectedItemPosition,
//                price = binding.price.editText?.text?.toString().toString(),
//                livingSpace = binding.livingSpace.editText?.text?.toString().toString(),
//                numberRooms = binding.nbRooms.editText?.text?.toString().toString(),
//                numberBedroom = binding.nbBedrooms.editText?.text?.toString().toString(),
//                numberBathroom = binding.nbBathrooms.editText?.text?.toString().toString(),
//                description = binding.description.editText?.text?.toString().toString(),
//                photos = listOfPhotos,
//                city = binding.cityBorough.editText?.text?.toString().toString(),
//                postalCode = binding.postalCode.editText?.text?.toString().toString(),
//                state = binding.state.editText?.text?.toString().toString(),
//                streetName = binding.streetName.editText?.text?.toString().toString(),
//                gridZone = binding.gridZone.editText?.text?.toString().toString(),
//                latitude = latitude,
//                longitude = longitude,
//            )
//        )
    }

    //endregion
}