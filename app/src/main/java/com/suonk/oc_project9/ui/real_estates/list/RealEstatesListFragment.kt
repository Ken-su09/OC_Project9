package com.suonk.oc_project9.ui.real_estates.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.suonk.oc_project9.R
import com.suonk.oc_project9.databinding.FragmentRealEstatesListBinding
import com.suonk.oc_project9.ui.filter.SearchBottomSheetDialogFragment
import com.suonk.oc_project9.ui.loan_simulator.LoanSimulatorFragment
import com.suonk.oc_project9.ui.main.MainActivity
import com.suonk.oc_project9.ui.map.MapFragment
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsFragment
import com.suonk.oc_project9.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RealEstatesListFragment : Fragment(R.layout.fragment_real_estates_list) {

    private val viewModel by viewModels<RealEstatesListViewModel>()
    private val binding by viewBinding(FragmentRealEstatesListBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupBottomNavigationView()

        val listAdapter = RealEstatesListAdapter()

        viewModel.realEstateLiveData.observe(viewLifecycleOwner) { list ->
            listAdapter.submitList(list)
        }

        binding.realEstatesList.adapter = listAdapter
        binding.addRealEstate.setOnClickListener {
            if (resources.getBoolean(R.bool.isTablet)) {
                replaceFragment(R.id.fragment_container_details, RealEstateDetailsFragment(), "RealEstateDetailsFragment")
            } else {
                replaceFragment(R.id.fragment_container, RealEstateDetailsFragment(), "RealEstateDetailsFragment")
            }
        }
    }

    //region ========================================================= TOOLBAR / BOTTOM NAV =========================================================

    private fun setupToolbar() {
        binding.toolbar.inflateMenu(R.menu.main_toolbar_menu)

        binding.toolbar.menu?.let { menu ->
            val searchItem = menu.findItem(R.id.action_search)
            val searchView = searchItem?.actionView as SearchView
            searchView.imeOptions = EditorInfo.IME_ACTION_DONE;
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(search: String?): Boolean {
                    search?.let {
//                        viewModel.onSearchQueryChanged()
//                        viewModel.onSearchQueryDone(search)
                    }
                    return false
                }

                override fun onQueryTextChange(search: String?): Boolean {
                    search?.let {
                        viewModel.onSearchQueryChanged(search)
                    }
                    return true
                }
            })
        }
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add_more_criteria -> {
                    val modalBottomSheet = SearchBottomSheetDialogFragment()

                    if (requireActivity() is MainActivity) {
                        modalBottomSheet.show(requireActivity().supportFragmentManager, "")
                    }
                }
                else -> {
                    viewModel.onSortedOrFilterClicked(it.itemId)
                    it.isChecked = true
                }
            }
            true
        }

//        val menuHost: MenuHost = requireActivity()
//        menuHost.addMenuProvider(object : MenuProvider {
//            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//                menuInflater.inflate(R.menu.main_toolbar_menu, menu)
//
//                val searchItem = menu.findItem(R.id.action_search)
//                val searchView = searchItem?.actionView as SearchView
//                searchView.imeOptions = EditorInfo.IME_ACTION_DONE;
//                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//                    override fun onQueryTextSubmit(search: String?): Boolean {
//                        search?.let {
////                            viewModel.onSearchQueryDone(search)
//                        }
//
//                        return false
//                    }
//
//                    override fun onQueryTextChange(search: String?): Boolean {
//                        search?.let {
//                            viewModel.onSearchQueryChanged(search)
//                        }
//                        return true
//                    }
//                })
//            }
//
//            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
//                if (menuItem.itemId == R.id.add_more_criteria) {
//                    val modalBottomSheet = SearchBottomSheetDialogFragment()
//                    modalBottomSheet.show(requireFragmentManager(), "")
//                } else {
//                    viewModel.onSortedOrFilterClicked(menuItem.itemId)
//                    menuItem.isChecked = true
//                }
//                return true
//            }
//        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupBottomNavigationView() {
        binding.bottomNavigation.menu.findItem(R.id.nav_real_estate).isChecked = true
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

            replaceFragment(R.id.fragment_container, fragmentToCommit, "RealEstatesListFragment / MapFragment")

            true
        }
    }

    //endregion

    private fun replaceFragment(fragmentContainer: Int, fragment: Fragment, tag: String) {
        if (requireActivity() is MainActivity) {
            requireActivity().supportFragmentManager.beginTransaction().replace(fragmentContainer, fragment, tag).addToBackStack(null)
                .commit()
        }
    }
}