package com.suonk.oc_project9.ui.filter

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.suonk.oc_project9.R
import com.suonk.oc_project9.databinding.FilterRealEstateBottomSheetBinding
import com.suonk.oc_project9.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class SearchBottomSheetDialogFragment : BottomSheetDialogFragment(R.layout.filter_real_estate_bottom_sheet) {

    private val viewModel by viewModels<SearchViewModel>()
    private val binding by viewBinding(FilterRealEstateBottomSheetBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.onResetFiltersClicked()
        setupButtons()
        editTextTimePicker()

        viewModel.finishSavingSingleLiveEvent.observe(viewLifecycleOwner) {
            dismiss()
        }
    }

    private fun setupButtons() {
        binding.filterValidation.setOnClickListener {
            viewModel.onValidateClicked(
                binding.livingSpaceMin.text?.toString(),
                binding.livingSpaceMax.text?.toString(),
                binding.priceMin.text?.toString(),
                binding.priceMax.text?.toString(),
                binding.nbRoomsMin.text?.toString(),
                binding.nbRoomsMax.text?.toString(),
                binding.nbBedroomsMin.text?.toString(),
                binding.nbBedroomsMax.text?.toString(),
                binding.nbBathroomsMin.text?.toString(),
                binding.nbBathroomsMax.text?.toString(),
                binding.entryDateFromValue.text?.toString(),
                binding.entryDateToValue.text?.toString(),
                binding.saleDateFromValue.text?.toString(),
                binding.saleDateToValue.text?.toString(),
            )
        }
    }

    //region ================================================================= Date =================================================================

    private fun editTextTimePicker() {
        val currentDate = LocalDate.now()

        val year = currentDate.year
        val month = currentDate.monthValue - 1
        val day = currentDate.dayOfMonth

        binding.entryDateFromValue.setOnClickListener { view ->
            val datePickerDialog = DatePickerDialog(
                requireContext(), { p0, year, month, dayOfMonth ->
                    if (month + 1 < 10) {
                        binding.entryDateFromValue.setText(getString(R.string.start_time_date_picker_0, month + 1, dayOfMonth, year))
                    } else {
                        binding.entryDateFromValue.setText(getString(R.string.start_time_date_picker, month + 1, dayOfMonth, year))
                    }
                }, year, month, day
            )
            datePickerDialog.show()
        }
        binding.entryDateToValue.setOnClickListener { view ->
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { p0, year, month, dayOfMonth ->
                    if (month + 1 < 10) {
                        binding.entryDateToValue.setText(getString(R.string.start_time_date_picker_0, month + 1, dayOfMonth, year))
                    } else {
                        binding.entryDateToValue.setText(getString(R.string.start_time_date_picker, month + 1, dayOfMonth, year))
                    }
                }, year, month, day
            )
            datePickerDialog.show()
        }
        binding.saleDateFromValue.setOnClickListener { view ->
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { p0, year, month, dayOfMonth ->
                    if (month + 1 < 10) {
                        binding.saleDateFromValue.setText(getString(R.string.start_time_date_picker_0, month + 1, dayOfMonth, year))
                    } else {
                        binding.saleDateFromValue.setText(getString(R.string.start_time_date_picker, month + 1, dayOfMonth, year))
                    }
                },
                year, month, day
            )
            datePickerDialog.show()
        }
        binding.saleDateToValue.setOnClickListener { view ->
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { p0, year, month, dayOfMonth ->
                    if (month + 1 < 10) {
                        binding.saleDateToValue.setText(getString(R.string.start_time_date_picker_0, month + 1, dayOfMonth, year))
                    } else {
                        binding.saleDateToValue.setText(getString(R.string.start_time_date_picker, month + 1, dayOfMonth, year))
                    }
                },
                year, month, day


            )
            datePickerDialog.show()
        }
    }

    //endregion
}