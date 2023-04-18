package com.suonk.oc_project9.model.database.data

import com.suonk.oc_project9.domain.SearchRepository
import com.suonk.oc_project9.ui.filter.Filter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor() : SearchRepository {

    private val currentFilterParameterFlow = MutableStateFlow<List<Filter>>(emptyList())

    override fun getCurrentFilterParametersFlow(): StateFlow<List<Filter>> {
        return currentFilterParameterFlow
    }

    private val currentSearchParameterFlow = MutableStateFlow("")

    override fun getCurrentSearchParametersFlow(): StateFlow<String> {
        return currentSearchParameterFlow
    }

    override fun setCurrentSearchParametersFlow(search: String) {
        currentSearchParameterFlow.tryEmit(search)
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
        currentFilterParameterFlow.update { filters ->
            filters + filter
        }
    }

    override fun removeFilter(filter: Filter) {
        currentFilterParameterFlow.update { filters ->
            filters - filter
        }
    }

    override fun reset() {
        currentFilterParameterFlow.value = emptyList()
    }
}