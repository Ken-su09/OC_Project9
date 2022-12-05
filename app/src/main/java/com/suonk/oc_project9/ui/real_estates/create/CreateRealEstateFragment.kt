package com.suonk.oc_project9.ui.real_estates.create

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.suonk.oc_project9.R
import com.suonk.oc_project9.databinding.FragmentCreateRealEstateBinding
import com.suonk.oc_project9.databinding.FragmentRealEstatesListBinding
import com.suonk.oc_project9.ui.main.MainViewModel
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewState
import com.suonk.oc_project9.ui.real_estates.list.RealEstatesListViewModel
import com.suonk.oc_project9.ui.real_estates.list.SliderAdapter
import com.suonk.oc_project9.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class CreateRealEstateFragment : Fragment(R.layout.fragment_create_real_estate) {

    private val viewModel by viewModels<CreateRealEstateViewModel>()
    private val mainViewModel by viewModels<MainViewModel>()
    private val binding by viewBinding(FragmentCreateRealEstateBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupSpinners()
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
                    R.id.action_add_image -> {

                        true
                    }
                    R.id.action_save_real_estate -> {
                        addNewRealEstate()
                        findNavController().navigate(CreateRealEstateFragmentDirections.actionCreateToList())
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

    private fun addNewRealEstate() {
        viewModel.saveRealEstateDetails(
            estate = RealEstateDetailsViewState(
                id = 0,
                type = binding.typeContent.selectedItem.toString(),
                typePosition = binding.typeContent.selectedItemPosition,
                price = binding.price.editText?.text?.toString().toString(),
                livingSpace = binding.livingSpace.editText?.text?.toString().toString(),
                numberRooms = binding.nbRooms.editText?.text?.toString().toString(),
                numberBedroom = binding.nbBedrooms.editText?.text?.toString().toString(),
                numberBathroom = binding.nbBathrooms.editText?.text?.toString().toString(),
                description = binding.description.editText?.text?.toString().toString(),
                photos = arrayListOf(),
                city = binding.cityBorough.editText?.text?.toString().toString(),
                postalCode = binding.postalCode.editText?.text?.toString().toString(),
                state = binding.state.editText?.text?.toString().toString(),
                streetName = binding.streetName.editText?.text?.toString().toString(),
                gridZone = binding.gridZone.editText?.text?.toString().toString(),

            )
        )
    }
}