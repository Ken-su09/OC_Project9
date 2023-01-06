package com.suonk.oc_project9.ui.real_estates.create

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
import com.suonk.oc_project9.databinding.FragmentRealEstateDetailsBinding
import com.suonk.oc_project9.ui.real_estates.carousel.PhotoViewState
import com.suonk.oc_project9.ui.real_estates.carousel.SliderAdapter
import com.suonk.oc_project9.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class RealEstateDetailsFragment : Fragment(R.layout.fragment_real_estate_details) {

    private val binding by viewBinding(FragmentRealEstateDetailsBinding::bind)

    private val viewModel: RealEstateDetailsViewModel by activityViewModels()

    private val sliderAdapter = SliderAdapter()
    private val currentList = arrayListOf<PhotoViewState>()

    private var latitude = 0.0
    private var longitude = 0.0

    private var map: GoogleMap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupSpinners()
        setupViewPager()

        binding.price.doAfterTextChanged {
            viewModel.onPriceChanged(it?.toString())
        }
        binding.typeContent.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                viewModel.onTypeChanged(position)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        binding.livingSpace.doAfterTextChanged {
            viewModel.onLivingSpaceChanged(it?.toString())
        }
        binding.nbRooms.doAfterTextChanged {
            viewModel.onNumberRoomsChanged(it?.toString())
        }
        binding.nbBedrooms.doAfterTextChanged {
            viewModel.onNumberBedroomsChanged(it?.toString())
        }
        binding.nbBathrooms.doAfterTextChanged {
            viewModel.onNumberBathroomsChanged(it?.toString())
        }
        binding.description.doAfterTextChanged {
            viewModel.onDescriptionChanged(it?.toString())
        }
        binding.cityBorough.doAfterTextChanged {
            viewModel.onCityChanged(it?.toString())
        }
        binding.postalCode.doAfterTextChanged {
            viewModel.onPostalCodeChanged(it?.toString())
        }
        binding.state.doAfterTextChanged {
            viewModel.onStateChanged(it?.toString())
        }
        binding.streetName.doAfterTextChanged {
            viewModel.onStreetNameChanged(it?.toString())
        }
        binding.gridZone.doAfterTextChanged {
            viewModel.onGridZoneChanged(it?.toString())
        }

        viewModel.photosLiveData.observe(viewLifecycleOwner) {
            currentList.clear()
            currentList.addAll(it)
            sliderAdapter.submitList(it)
        }
        viewModel.isListEmptySingleLiveEvent.observe(viewLifecycleOwner) { isEmpty ->
            isEmpty?.let {
                binding.noImagesIcon.isVisible = it
                binding.noImagesTitle.isVisible = it
                binding.deletePhoto.isVisible = !it
            }
        }
        viewModel.isFieldEmptySingleLiveEvent.observe(viewLifecycleOwner) {
            it?.let { isError ->
                if (!isError) {
                } else {
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.field_empty_toast_msg),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
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
                    R.id.action_save_real_estate -> {
                        addNewRealEstate()

                        viewModel.isFieldEmptySingleLiveEvent.observe(viewLifecycleOwner) {
                            it?.let { isError ->
                                if (!isError) {
                                    findNavController().navigate(RealEstateDetailsFragmentDirections.actionDetailsToList())
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        requireContext().getString(R.string.field_empty_toast_msg),
                                        Toast.LENGTH_SHORT
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

    //region =========================================== SETUP UI ===========================================

    private fun setupRealEstateDetails() {
        viewModel.realEstateDetailsViewStateLiveData.observe(viewLifecycleOwner) { realEstate ->
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

//            setupViewPager(realEstate.photos)
//            listOfPhotos.addAll(realEstate.photos)

//            latitude = realEstate.latitude
//            longitude = realEstate.longitude
            setupMap(realEstate.city, realEstate.latitude, realEstate.longitude)
        }
    }

    private fun setupViewPager() {
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

                Log.i("DeletePhoto", "binding.images.currentItem : ${binding.images.currentItem}")
                Log.i("DeletePhoto", "position : ${position}")
            }
        })
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

    //region ============================================ IMAGE =============================================

    private fun addNewImage() {
        val builderBottom = BottomSheetDialog(requireContext())
        builderBottom.setContentView(R.layout.builder_bottom_layout)

        val galleryCameraLayout = builderBottom.findViewById<ConstraintLayout>(R.id.gallery_layout)

        galleryCameraLayout?.setOnClickListener {
            openGallery()
            builderBottom.dismiss()
        }

        val addImageButton = builderBottom.findViewById<AppCompatImageButton>(R.id.add_image_button)
        val addImageEditText =
            builderBottom.findViewById<AppCompatEditText>(R.id.add_image_edit_text)

        addImageButton?.let {
            addImageEditText?.let {
                addImageToViewPager(addImageButton, addImageEditText)
                builderBottom.dismiss()
            }
        }

        builderBottom.show()
    }

    private fun addImageToViewPager(button: AppCompatImageButton, editText: AppCompatEditText) {
        button.setOnClickListener {
            editText.text?.toString()?.let {
                viewModel.setPhotosLiveData(it, photos = currentList, false)
            }
            editText.text?.clear()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.data?.let {
                    viewModel.setPhotosLiveData(it.toString(), photos = currentList, true)
                }
            }
        }

    //endregion

    //region ============================================= MAP ==============================================

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

    private fun addNewRealEstate() {
        viewModel.onAddRealButtonClicked()
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
}