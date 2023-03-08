package com.suonk.oc_project9.ui.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.suonk.oc_project9.domain.SearchRepository
import com.suonk.oc_project9.utils.CoroutineDispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.text.ParseException
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository, coroutineDispatcherProvider: CoroutineDispatcherProvider
) : ViewModel() {

    private val listOfFiltersStateStateFlow = MutableStateFlow(emptyList<Filter>())

    val viewStateLiveData: LiveData<SearchViewState> = liveData(coroutineDispatcherProvider.io) {
        searchRepository.getCurrentSearchParametersFlow().collect {
            emit(
                SearchViewState(
                    livingSpaceMin = (it as Filter.LivingSpaceFilter).min.toString(),
                    livingSpaceMax = (it as Filter.LivingSpaceFilter).max.toString(),
                    priceMin = (it as Filter.PriceFilter).min.toString(),
                    priceMax = (it as Filter.PriceFilter).max.toString(),
                    nbRoomsMin = (it as Filter.NbRoomsFilter).min.toString(),
                    nbRoomsMax = (it as Filter.NbRoomsFilter).max.toString(),
                    nbBedroomsMin = (it as Filter.NbBedroomsFilter).min.toString(),
                    nbBedroomsMax = (it as Filter.NbBedroomsFilter).max.toString(),
                    entryDateMin = (it as Filter.EntryDateFilter).min.toString(),
                    entryDateMax = (it as Filter.EntryDateFilter).max.toString(),
                    saleDateMin = (it as Filter.SaleDateFilter).min.toString(),
                    saleDateMax = (it as Filter.SaleDateFilter).max.toString(),
                )
            )
        }
    }

    //region =============================================================== FILTERS ===============================================================

    fun resetFilters() {
        listOfFiltersStateStateFlow.update {
            emptyList()
        }
    }

    fun updateLivingSpaceMin(livingSpaceMin: String) {
        val casted = livingSpaceMin.toDoubleOrNull()
        listOfFiltersStateStateFlow.update { filters ->
            if (filters.filterIsInstance<Filter.LivingSpaceFilter>().isNotEmpty()) {
                val previousFilter = filters.filterIsInstance<Filter.LivingSpaceFilter>().first()
                filters - previousFilter + Filter.LivingSpaceFilter(casted, previousFilter.max)
            } else {
                filters + Filter.LivingSpaceFilter(casted, null)
            }
        }
    }

    fun updateLivingSpaceMax(livingSpaceMax: String) {
        val casted = livingSpaceMax.toDoubleOrNull()
        listOfFiltersStateStateFlow.update { filters ->
            if (filters.filterIsInstance<Filter.LivingSpaceFilter>().isNotEmpty()) {
                val previousFilter = filters.filterIsInstance<Filter.LivingSpaceFilter>().first()
                filters - previousFilter + Filter.LivingSpaceFilter(previousFilter.min, casted)
            } else {
                filters + Filter.LivingSpaceFilter(null, casted)
            }
        }
    }

    fun updatePriceMin(priceMin: String) {
        val casted = priceMin.toDoubleOrNull()
        listOfFiltersStateStateFlow.update { filters ->
            if (filters.filterIsInstance<Filter.PriceFilter>().isNotEmpty()) {
                val previousFilter = filters.filterIsInstance<Filter.PriceFilter>().first()
                filters - previousFilter + Filter.PriceFilter(casted, previousFilter.max)
            } else {
                filters + Filter.PriceFilter(casted, null)
            }
        }
    }

    fun updatePriceMax(priceMax: String) {
        val casted = priceMax.toDoubleOrNull()
        listOfFiltersStateStateFlow.update { filters ->
            if (filters.filterIsInstance<Filter.PriceFilter>().isNotEmpty()) {
                val previousFilter = filters.filterIsInstance<Filter.PriceFilter>().first()
                filters - previousFilter + Filter.PriceFilter(previousFilter.min, casted)
            } else {
                filters + Filter.PriceFilter(null, casted)
            }
        }
    }

    fun updateNumberRoomsMin(nbRoomsMin: String) {
        val casted = nbRoomsMin.toIntOrNull()
        listOfFiltersStateStateFlow.update { filters ->
            if (filters.filterIsInstance<Filter.NbRoomsFilter>().isNotEmpty()) {
                val previousFilter = filters.filterIsInstance<Filter.NbRoomsFilter>().first()
                filters - previousFilter + Filter.NbRoomsFilter(previousFilter.min, casted)
            } else {
                filters + Filter.NbRoomsFilter(null, casted)
            }
        }
    }

    fun updateNumberRoomsMax(nbRoomsMax: String) {
        val casted = nbRoomsMax.toIntOrNull()
        listOfFiltersStateStateFlow.update { filters ->
            if (filters.filterIsInstance<Filter.NbRoomsFilter>().isNotEmpty()) {
                val previousFilter = filters.filterIsInstance<Filter.NbRoomsFilter>().first()
                filters - previousFilter + Filter.NbRoomsFilter(previousFilter.min, casted)
            } else {
                filters + Filter.NbRoomsFilter(null, casted)
            }
        }
    }

    fun updateNumberBedroomsMin(nbBedroomsMin: String) {
        val casted = nbBedroomsMin.toIntOrNull()
        listOfFiltersStateStateFlow.update { filters ->
            if (filters.filterIsInstance<Filter.NbBedroomsFilter>().isNotEmpty()) {
                val previousFilter = filters.filterIsInstance<Filter.NbBedroomsFilter>().first()
                filters - previousFilter + Filter.NbBedroomsFilter(casted, previousFilter.max)
            } else {
                filters + Filter.NbBedroomsFilter(casted, null)
            }
        }
    }

    fun updateNumberBedroomsMax(nbBedroomsMax: String) {
        val casted = nbBedroomsMax.toIntOrNull()
        listOfFiltersStateStateFlow.update { filters ->
            if (filters.filterIsInstance<Filter.NbBedroomsFilter>().isNotEmpty()) {
                val previousFilter = filters.filterIsInstance<Filter.NbBedroomsFilter>().first()
                filters - previousFilter + Filter.NbBedroomsFilter(previousFilter.min, casted)
            } else {
                filters + Filter.NbBedroomsFilter(null, casted)
            }
        }
    }

    fun updateEntryDateFrom(year: Int, month: Int, dayOfMonth: Int) {
        try {
            val entryDateFrom = LocalDateTime.of(year, month, dayOfMonth, 0, 0)

            listOfFiltersStateStateFlow.update { filters ->
                if (filters.filterIsInstance<Filter.EntryDateFilter>().isNotEmpty()) {
                    val previousFilter = filters.filterIsInstance<Filter.EntryDateFilter>().first()
                    filters - previousFilter + Filter.EntryDateFilter(entryDateFrom, previousFilter.max)
                } else {
                    filters + Filter.EntryDateFilter(entryDateFrom, null)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateEntryDateTo(year: Int, month: Int, dayOfMonth: Int) {
        try {
            val entryDateTo = LocalDateTime.of(year, month, dayOfMonth, 0, 0)

            listOfFiltersStateStateFlow.update { filters ->
                if (filters.filterIsInstance<Filter.EntryDateFilter>().isNotEmpty()) {
                    val previousFilter = filters.filterIsInstance<Filter.EntryDateFilter>().first()
                    filters - previousFilter + Filter.EntryDateFilter(entryDateTo, previousFilter.max)
                } else {
                    filters + Filter.EntryDateFilter(entryDateTo, null)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateSaleDateFrom(year: Int, month: Int, dayOfMonth: Int) {
        try {
            val saleDateFrom = LocalDateTime.of(year, month, dayOfMonth, 0, 0)

            listOfFiltersStateStateFlow.update { filters ->
                if (filters.filterIsInstance<Filter.EntryDateFilter>().isNotEmpty()) {
                    val previousFilter = filters.filterIsInstance<Filter.EntryDateFilter>().first()
                    filters - previousFilter + Filter.EntryDateFilter(saleDateFrom, previousFilter.max)
                } else {
                    filters + Filter.EntryDateFilter(saleDateFrom, null)
                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    fun updateSaleDateTo(year: Int, month: Int, dayOfMonth: Int) {
        try {
            val saleDateTo = LocalDateTime.of(year, month, dayOfMonth, 0, 0)

            listOfFiltersStateStateFlow.update { filters ->
                if (filters.filterIsInstance<Filter.EntryDateFilter>().isNotEmpty()) {
                    val previousFilter = filters.filterIsInstance<Filter.EntryDateFilter>().first()
                    filters - previousFilter + Filter.EntryDateFilter(saleDateTo, previousFilter.max)
                } else {
                    filters + Filter.EntryDateFilter(saleDateTo, null)
                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    //endregion
}