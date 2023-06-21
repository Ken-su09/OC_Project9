package com.suonk.oc_project9.ui.loan_simulator

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.suonk.oc_project9.R
import com.suonk.oc_project9.databinding.FragmentLoanSimulatorBinding
import com.suonk.oc_project9.ui.main.MainActivity
import com.suonk.oc_project9.ui.map.MapFragment
import com.suonk.oc_project9.ui.real_estates.list.RealEstatesListFragment
import com.suonk.oc_project9.utils.showToast
import com.suonk.oc_project9.utils.toCharSequence
import com.suonk.oc_project9.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoanSimulatorFragment : Fragment(R.layout.fragment_loan_simulator) {

    private val viewModel by viewModels<LoanSimulatorViewModel>()
    private val binding by viewBinding(FragmentLoanSimulatorBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calculateButton.setOnClickListener {
            viewModel.onMoneyValueValidation(
                binding.moneyNeededContent.text?.toString(),
                binding.interestRateContent.text?.toString(),
                binding.monthsNeededContent.text?.toString()
            )
        }

        viewModel.toastMessageSingleLiveEvent.observe(viewLifecycleOwner) {
            it.showToast(requireContext())
        }
        setupDataFromViewModelToView()
        setupBottomNavigationView()
    }

    private fun setupDataFromViewModelToView() {
        viewModel.loanSimulatorViewStateLiveData.observe(viewLifecycleOwner) {
            binding.resultContent.text = it.result.toCharSequence(requireContext())
        }
    }

    private fun setupBottomNavigationView() {
        binding.bottomNavigation.menu.findItem(R.id.nav_loan_simulator).isChecked = true
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

    private fun replaceFragment(fragment: Fragment, tag: String) {
        if (requireActivity() is MainActivity) {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, tag).addToBackStack(null)
                .commit()
        }
    }
}