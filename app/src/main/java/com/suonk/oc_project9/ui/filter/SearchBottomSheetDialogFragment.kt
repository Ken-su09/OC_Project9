package com.suonk.oc_project9.ui.filter

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.suonk.oc_project9.R
import com.suonk.oc_project9.databinding.FilterRealEstateBottomSheetBinding
import com.suonk.oc_project9.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchBottomSheetDialogFragment : BottomSheetDialogFragment(R.layout.filter_real_estate_bottom_sheet) {

    private val viewModel by viewModels<SearchViewModel>()
    private val binding by viewBinding(FilterRealEstateBottomSheetBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupEditTextsContentWithViewModel()
        setupButtons()
    }

    private fun setupButtons() {
        binding.filterValidation.setOnClickListener {
            viewModel.onValidateClicked()
            dismiss()
        }
        binding.resetFilter.setOnClickListener {
            viewModel.onResetFiltersClicked()
        }
    }

    private fun setupEditTextsContentWithViewModel() {
        val listAdapter = SearchListAdapter()
        viewModel.viewStateLiveData.observe(this) { list ->
            listAdapter.submitList(list)
            binding.list.adapter = listAdapter
        }
    }

    //region ============================================= Date =============================================

//    private fun editTextTimePicker() {
//
//        binding.entryDateFrom.setOnClickListener { view ->
//            val datePickerDialog = DatePickerDialog(
//                requireContext(), { p0, year, month, dayOfMonth ->
//                    viewModel.updateEntryDateFrom(year, month, dayOfMonth)
//                    if (month + 1 < 10) {
//                        binding.entryDateFrom.setText(
//                            getString(
//                                R.string.start_time_date_picker_0, month + 1, dayOfMonth, year
//                            )
//                        )
//                    } else {
//                        binding.entryDateFrom.setText(
//                            getString(
//                                R.string.start_time_date_picker, month + 1, dayOfMonth, year
//                            )
//                        )
//                    }
//                }, current.year, current.month.value - 2, current.dayOfMonth
//            )
//            datePickerDialog.show()
//        }
//        binding.entryDateTo.setOnClickListener { view ->
//            val datePickerDialog = DatePickerDialog(
//                requireContext(),
//                { p0, year, month, dayOfMonth -> viewModel.updateEntryDateTo(year, month, dayOfMonth) },
//                current.year,
//                current.month.value - 2,
//                current.dayOfMonth
//            )
//            datePickerDialog.show()
//        }
//        binding.saleDateFrom.setOnClickListener { view ->
//            val datePickerDialog = DatePickerDialog(
//                requireContext(),
//                { p0, year, month, dayOfMonth -> viewModel.updateEntryDateTo(year, month, dayOfMonth) },
//                current.year,
//                current.month.value - 2,
//                current.dayOfMonth
//            )
//            datePickerDialog.show()
//        }
//        binding.saleDateTo.setOnClickListener { view ->
//            val datePickerDialog = DatePickerDialog(
//                requireContext(),
//                { p0, year, month, dayOfMonth -> viewModel.updateEntryDateTo(year, month, dayOfMonth) },
//                current.year,
//                current.month.value - 2,
//                current.dayOfMonth
//            )
//            datePickerDialog.show()
//        }
//    }

    //endregion
}