package com.suonk.oc_project9.ui.real_estates.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.suonk.oc_project9.R
import com.suonk.oc_project9.databinding.FragmentRealEstatesListBinding
import com.suonk.oc_project9.ui.main.MainViewModel
import com.suonk.oc_project9.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RealEstatesListFragment : Fragment(R.layout.fragment_real_estates_list) {

    private val viewModel by viewModels<RealEstatesListViewModel>()
    private val mainViewModel by viewModels<MainViewModel>()
    private val binding by viewBinding(FragmentRealEstatesListBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listAdapter = RealEstatesListAdapter()

        viewModel.realEstateLiveData.observe(viewLifecycleOwner) { list ->
            listAdapter.submitList(list)
        }

        viewModel.realEstatesViewAction.observe(viewLifecycleOwner) { action ->
            when (action) {
                is RealEstatesListViewModel.RealEstatesViewAction.Navigate.Detail ->
                    findNavController().navigate(RealEstatesListFragmentDirections.navigateToDetails(action.realEstateId))
                null -> TODO()
            }
        }

        binding.realEstatesList.adapter = listAdapter

        binding.addRealEstate.setOnClickListener {
            findNavController().navigate(R.id.createRealEstateFragment)
        }
    }
}