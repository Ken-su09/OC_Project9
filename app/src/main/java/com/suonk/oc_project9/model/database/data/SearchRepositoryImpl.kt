package com.suonk.oc_project9.model.database.data

import com.suonk.oc_project9.R
import com.suonk.oc_project9.domain.SearchRepository
import com.suonk.oc_project9.ui.filter.Filter
import com.suonk.oc_project9.utils.sort.Sorting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor() : SearchRepository {

    private val currentMoreCriteriaFilterParameterFlow = MutableStateFlow<List<Filter>>(emptyList())
    private val currentSearchParameterFlow = MutableStateFlow("")
    private val currentSortFilterParameterFlow = MutableStateFlow(R.id.remove_filter)
    private val currentSortParameterFlow = MutableStateFlow(Sorting.DATE_ASC)

    override fun getCurrentFilterParametersFlow(): StateFlow<List<Filter>> {
        return currentMoreCriteriaFilterParameterFlow
    }

    override fun getCurrentSearchParametersFlow(): StateFlow<String> {
        return currentSearchParameterFlow
    }

    override fun setCurrentSearchParameters(search: String) {
        currentSearchParameterFlow.tryEmit(search)
    }

    override fun getCurrentSortParameterFlow(): StateFlow<Sorting> {
        return currentSortParameterFlow
    }

    override fun getCurrentSortFilterParametersFlow(): StateFlow<Int> {
        return currentSortFilterParameterFlow
    }

    override fun setCurrentSortFilterParametersFlow(itemId: Int) {
        when (itemId) {
            R.id.sort_by_date_asc -> currentSortParameterFlow.tryEmit(Sorting.DATE_ASC)
            R.id.sort_by_price_asc -> currentSortParameterFlow.tryEmit(Sorting.PRICE_ASC)
            R.id.sort_by_living_space_asc -> currentSortParameterFlow.tryEmit(Sorting.LIVING_SPACE_ASC)
            R.id.sort_by_rooms_number_asc -> currentSortParameterFlow.tryEmit(Sorting.ROOMS_NUMBER_ASC)
            R.id.sort_by_date_desc -> currentSortParameterFlow.tryEmit(Sorting.DATE_DESC)
            R.id.sort_by_price_desc -> currentSortParameterFlow.tryEmit(Sorting.PRICE_DESC)
            R.id.sort_by_living_space_desc -> currentSortParameterFlow.tryEmit(Sorting.LIVING_SPACE_DESC)
            R.id.sort_by_rooms_number_desc -> currentSortParameterFlow.tryEmit(Sorting.ROOMS_NUMBER_DESC)
            R.id.remove_filter -> {
                currentSortFilterParameterFlow.tryEmit(R.id.remove_filter)
                reset()
            }
            R.id.house_filter -> currentSortFilterParameterFlow.tryEmit(R.id.house_filter)
            R.id.penthouse_filter -> currentSortFilterParameterFlow.tryEmit(R.id.penthouse_filter)
            R.id.duplex_filter -> currentSortFilterParameterFlow.tryEmit(R.id.duplex_filter)
            R.id.flat_filter -> currentSortFilterParameterFlow.tryEmit(R.id.flat_filter)
            R.id.loft_filter -> currentSortFilterParameterFlow.tryEmit(R.id.loft_filter)
            else -> currentSortFilterParameterFlow.tryEmit(R.id.remove_filter)
        }
    }

    override fun updateFilter(filter: Filter) {
//        when (filter) {
//            is Filter.LivingSpaceFilter -> {
//                currentSearchParameterFlow.update {
//                    it[FilterType.LivingSpace] = filter
//
//                    Log.i("UpdateFilter", "it : $it")
//                    it
//                }
//            }
//            is Filter.PriceFilter -> {
//                currentSearchParameterFlow.update {
//                    it[FilterType.Price] = filter
//                    it
//                }
//            }
//            is Filter.NbRoomsFilter -> {
//                currentSearchParameterFlow.update {
//                    it[FilterType.NbRooms] = filter
//
//                    Log.i("UpdateFilter", "it : $it")
//                    it
//                }
//            }
//            is Filter.NbBedroomsFilter -> {
//                currentSearchParameterFlow.update {
//                    it[FilterType.NbBedrooms] = filter
//
//                    Log.i("UpdateFilter", "it : $it")
//                    it
//                }
//            }
//            is Filter.EntryDateFilter -> {
//                currentSearchParameterFlow.update {
//                    it[FilterType.EntryDate] = filter
//
//                    Log.i("UpdateFilter", "it : $it")
//                    it
//                }
//            }
//            is Filter.SaleDateFilter -> {
//                currentSearchParameterFlow.update {
//                    it[FilterType.SaleDate] = filter
//
//                    Log.i("UpdateFilter", "it : $it")
//                    it
//                }
//            }
//        }
    }

    override fun addFilter(filter: Filter) {
        currentMoreCriteriaFilterParameterFlow.update { filters ->
            filters + filter
        }
    }

    override fun removeFilter(filter: Filter) {
        currentMoreCriteriaFilterParameterFlow.update { filters ->
            filters - filter
        }
    }

    override fun reset() {
        currentMoreCriteriaFilterParameterFlow.value = emptyList()
    }
}