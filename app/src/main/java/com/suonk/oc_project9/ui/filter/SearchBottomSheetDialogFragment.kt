package com.suonk.oc_project9.ui.filter

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
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
        binding.livingSpaceMin.doAfterTextChanged { editable ->
            editable?.toString()?.let { viewModel.updateLivingSpaceMin(it) }
        }
        binding.livingSpaceMax.doAfterTextChanged { editable ->
            editable?.toString()?.let { viewModel.updateLivingSpaceMax(it) }
        }

        binding.priceMin.doAfterTextChanged { editable ->
            editable?.toString()?.let { viewModel.updatePriceMin(it) }
        }
        binding.priceMax.doAfterTextChanged { editable ->
            editable?.toString()?.let { viewModel.updatePriceMax(it) }
        }

        binding.nbRoomsMin.doAfterTextChanged { editable ->
            editable?.toString()?.let { viewModel.updateNumberRoomsMin(it) }
        }
        binding.nbRoomsMax.doAfterTextChanged { editable ->
            editable?.toString()?.let { viewModel.updateNumberRoomsMax(it) }
        }

        binding.nbBedroomsMin.doAfterTextChanged { editable ->
            editable?.toString()?.let { viewModel.updateNumberBedroomsMin(it) }
        }
        binding.nbBedroomsMax.doAfterTextChanged { editable ->
            editable?.toString()?.let { viewModel.updateNumberBedroomsMax(it) }
        }

        viewModel.viewStateLiveData.observe(this) {
            binding.livingSpaceMin.setText(it.livingSpaceMin)
        }
    }

//    private fun filterWithMoreCriteria() {
//        val bottomSheetBinding = FilterRealEstateBuilderBottomBinding.inflate(layoutInflater, null, false)
//        val builderBottom = BottomSheetDialogFragment()
//        builderBottom.show()
//
//        bottomSheetBinding.livingSpaceMin.doAfterTextChanged { editable ->
//            editable?.toString()?.let { viewModel.updateLivingSpaceMin(it) }
//        }
//        bottomSheetBinding.livingSpaceMax.doAfterTextChanged { editable ->
//            editable?.toString()?.let { viewModel.updateLivingSpaceMax(it) }
//        }
//
//        bottomSheetBinding.priceMin.doAfterTextChanged { editable ->
//            editable?.toString()?.let { viewModel.updatePriceMin(it) }
//        }
//        bottomSheetBinding.priceMax.doAfterTextChanged { editable ->
//            editable?.toString()?.let { viewModel.updatePriceMax(it) }
//        }
//
//        bottomSheetBinding.nbRoomsMin.doAfterTextChanged { editable ->
//            editable?.toString()?.let { viewModel.updateNumberRoomsMin(it) }
//        }
//        bottomSheetBinding.nbRoomsMax.doAfterTextChanged { editable ->
//            editable?.toString()?.let { viewModel.updateNumberRoomsMax(it) }
//        }
//
//        bottomSheetBinding.nbBedroomsMin.doAfterTextChanged { editable ->
//            editable?.toString()?.let { viewModel.updateNumberBedroomsMin(it) }
//        }
//        bottomSheetBinding.nbBedroomsMax.doAfterTextChanged { editable ->
//            editable?.toString()?.let { viewModel.updateNumberBedroomsMax(it) }
//        }
//
//        editTextTimePicker(bottomSheetBinding)
////        editTextTimePicker(bottomSheetBinding.saleDateFrom, bottomSheetBinding.saleDateTo)
//
//        bottomSheetBinding.filterValidation.setOnClickListener {
////            bottomSheetBinding.livingSpaceMin.editableText?.clear()
////            bottomSheetBinding.livingSpaceMax.editableText?.clear()
////            bottomSheetBinding.priceMin.editableText?.clear()
////            bottomSheetBinding.priceMax.editableText?.clear()
////            bottomSheetBinding.nbRoomsMin.editableText?.clear()
////            bottomSheetBinding.nbRoomsMax.editableText?.clear()
////            bottomSheetBinding.nbBedroomsMin.editableText?.clear()
////            bottomSheetBinding.nbBedroomsMax.editableText?.clear()
//            builderBottom.dismiss()
//        }
//
//        builderBottom.setCancelable(false)
//        builderBottom.show()
//
//        viewModel.numberOfRealEstatesSingleLiveEvent.observe(viewLifecycleOwner) {
//            it?.let {
//                bottomSheetBinding.filterValidation.text = it
//            }
//        }
//    }

    //region ============================================= Date =============================================

//    private fun editTextTimePicker(bottomSheetBinding: FilterRealEstateBuilderBottomBinding) {
//        val current = LocalDate.now()
//
//        bottomSheetBinding.entryDateFrom.setOnClickListener { view ->
//            val datePickerDialog = DatePickerDialog(
//                requireContext(), object : DatePickerDialog.OnDateSetListener {
//                    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
//                        viewModel.updateEntryDateFrom(year, month, dayOfMonth)
//                        if (month + 1 < 10) {
//                            bottomSheetBinding.entryDateFrom.setText(
//                                getString(
//                                    R.string.start_time_date_picker_0, month + 1, dayOfMonth, year
//                                )
//                            )
//                        } else {
//                            bottomSheetBinding.entryDateFrom.setText(
//                                getString(
//                                    R.string.start_time_date_picker, month + 1, dayOfMonth, year
//                                )
//                            )
//                        }
//                    }
//
//                }, current.year, current.month.value - 2, current.dayOfMonth
//            )
//            datePickerDialog.show()
//        }
//        bottomSheetBinding.entryDateTo.setOnClickListener { view ->
//            val datePickerDialog = DatePickerDialog(
//                requireContext(), object : DatePickerDialog.OnDateSetListener {
//                    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
//                        viewModel.updateEntryDateTo(year, month, dayOfMonth)
//                    }
//
//                }, current.year, current.month.value - 2, current.dayOfMonth
//            )
//            datePickerDialog.show()
//        }
//        bottomSheetBinding.saleDateFrom.setOnClickListener { view ->
//            val datePickerDialog = DatePickerDialog(
//                requireContext(), object : DatePickerDialog.OnDateSetListener {
//                    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
//                        viewModel.updateEntryDateTo(year, month, dayOfMonth)
//                    }
//
//                }, current.year, current.month.value - 2, current.dayOfMonth
//            )
//            datePickerDialog.show()
//        }
//        bottomSheetBinding.saleDateTo.setOnClickListener { view ->
//            val datePickerDialog = DatePickerDialog(
//                requireContext(), object : DatePickerDialog.OnDateSetListener {
//                    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
//                        viewModel.updateEntryDateTo(year, month, dayOfMonth)
//                    }
//
//                }, current.year, current.month.value - 2, current.dayOfMonth
//            )
//            datePickerDialog.show()
//        }
//    }

    //endregion
}