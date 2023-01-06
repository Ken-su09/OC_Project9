package com.suonk.oc_project9.ui.real_estates.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.suonk.oc_project9.R
import com.suonk.oc_project9.databinding.FragmentRealEstatesListBinding
import com.suonk.oc_project9.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RealEstatesListFragment : Fragment(R.layout.fragment_real_estates_list) {

    private val viewModel by viewModels<RealEstatesListViewModel>()
    private val binding by viewBinding(FragmentRealEstatesListBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()

        val listAdapter = RealEstatesListAdapter()

        viewModel.realEstateLiveData.observe(viewLifecycleOwner) { list ->
            listAdapter.submitList(null)
            listAdapter.submitList(list)
        }
        viewModel.realEstatesViewAction.observe(viewLifecycleOwner) { action ->
            when (action) {
                is RealEstatesListViewModel.RealEstatesViewAction.Navigate.Detail -> findNavController().navigate(
                    RealEstatesListFragmentDirections.navigateToDetails(action.realEstateId)
                )
                else -> {

                }
            }
        }

        binding.realEstatesList.adapter = listAdapter
        binding.addRealEstate.setOnClickListener {
            findNavController().navigate(R.id.createRealEstateFragment)
        }
    }

    //region =========================================== TOOLBAR ============================================

    private fun setupToolbar() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_toolbar_menu, menu)

                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem?.actionView as SearchView
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                viewModel.setMutableState(menuItem.itemId)
                menuItem.isChecked = true
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    //endregion
}