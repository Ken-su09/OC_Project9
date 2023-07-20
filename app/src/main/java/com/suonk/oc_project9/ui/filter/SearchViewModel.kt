package com.suonk.oc_project9.ui.filter

import androidx.lifecycle.ViewModel
import com.suonk.oc_project9.domain.SearchRepository
import com.suonk.oc_project9.domain.more_criteria.ToggleFilterUseCase
import com.suonk.oc_project9.domain.more_criteria.model.FilterQuery
import com.suonk.oc_project9.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val toggleFilterUseCase: ToggleFilterUseCase,
    private val searchRepository: SearchRepository
) : ViewModel() {

    val finishSavingSingleLiveEvent = SingleLiveEvent<Unit>()

    fun onResetFiltersClicked() {
        searchRepository.reset()
    }

    fun onValidateClicked(
        livingSpaceMin: String?,
        livingSpaceMax: String?,
        priceMin: String?,
        priceMax: String?,
        nbRoomsMin: String?,
        nbRoomsMax: String?,
        nbBedroomsMin: String?,
        nbBedroomsMax: String?,
        nbBathroomsMin: String?,
        nbBathroomsMax: String?,
        entryDateFromValue: String?,
        entryDateToValue: String?,
        saleDateFromValue: String?,
        saleDateToValue: String?,
    ) {
        val castedLivingSpaceMin = livingSpaceMin?.toDoubleOrNull()
        val castedLivingSpaceMax = livingSpaceMax?.toDoubleOrNull()

        toggleFilterUseCase.invoke(
            FilterQuery.LivingSpaceFilter(
                min = FilterQuery.SearchParam.Update(castedLivingSpaceMin), max = FilterQuery.SearchParam.Update(castedLivingSpaceMax)
            )
        )

        val castedPriceMin = if (priceMin?.isNotEmpty() == true) {
            BigDecimal(priceMin)
        } else {
            null
        }
        val castedPriceMax = if (priceMax?.isNotEmpty() == true) {
            BigDecimal(priceMax)
        } else {
            null
        }

        toggleFilterUseCase.invoke(
            FilterQuery.PriceFilter(
                min = FilterQuery.SearchParam.Update(castedPriceMin), max = FilterQuery.SearchParam.Update(castedPriceMax)
            )
        )

        val castedNbRoomsMin = nbRoomsMin?.toIntOrNull()
        val castedNbRoomsMax = nbRoomsMax?.toIntOrNull()

        toggleFilterUseCase.invoke(
            FilterQuery.NbRoomsFilter(
                min = FilterQuery.SearchParam.Update(castedNbRoomsMin), max = FilterQuery.SearchParam.Update(castedNbRoomsMax)
            )
        )

        val castedNbBedroomsMin = nbBedroomsMin?.toIntOrNull()
        val castedNbBedroomsMax = nbBedroomsMax?.toIntOrNull()

        toggleFilterUseCase.invoke(
            FilterQuery.NbBedroomsFilter(
                min = FilterQuery.SearchParam.Update(castedNbBedroomsMin), max = FilterQuery.SearchParam.Update(castedNbBedroomsMax)
            )
        )

        val castedNbBathroomsMin = nbBathroomsMin?.toIntOrNull()
        val castedNbBathroomsMax = nbBathroomsMax?.toIntOrNull()

        toggleFilterUseCase.invoke(
            FilterQuery.NbBathroomsFilter(
                min = FilterQuery.SearchParam.Update(castedNbBathroomsMin), max = FilterQuery.SearchParam.Update(castedNbBathroomsMax)
            )
        )

        val castedEntryDateFromValue = transformStringToLocalDateTime(entryDateFromValue)
        val castedEntryDateToValue = transformStringToLocalDateTime(entryDateToValue)

        toggleFilterUseCase.invoke(
            FilterQuery.EntryDateFilter(
                from = FilterQuery.SearchParam.Update(castedEntryDateFromValue), to = FilterQuery.SearchParam.Update(castedEntryDateToValue)
            )
        )

        val castedSaleDateFromValue = transformStringToLocalDateTime(saleDateFromValue)
        val castedSaleDateToValue = transformStringToLocalDateTime(saleDateToValue)

        toggleFilterUseCase.invoke(
            FilterQuery.SaleDateFilter(
                from = FilterQuery.SearchParam.Update(castedSaleDateFromValue), to = FilterQuery.SearchParam.Update(castedSaleDateToValue)
            )
        )


        finishSavingSingleLiveEvent.setValue(Unit)
    }

    private fun transformStringToLocalDateTime(time: String?): LocalDateTime? {
        return if (time == null) {
            null
        } else {
            println("time.split(.).size : ${time.split(".").size}")
            if (time.split(".").size > 2) {
                val day = time.split(".")[0].toIntOrNull() ?: 0
                val month = time.split(".")[1].toIntOrNull() ?: 0
                val year = time.split(".")[2].toIntOrNull() ?: 0

                if (day == 0 || month == 0 || year == 0) {
                    null
                } else {
                    LocalDateTime.of(year, month, day, 0, 0, 0)
                }
            } else {
                null
            }
        }
    }
}