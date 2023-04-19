package com.suonk.oc_project9.domain

import com.suonk.oc_project9.ui.filter.Filter
import com.suonk.oc_project9.utils.sort.Sorting
import kotlinx.coroutines.flow.StateFlow

interface SearchRepository {

    fun getCurrentFilterParametersFlow(): StateFlow<List<Filter>>

    fun getCurrentSearchParametersFlow(): StateFlow<String>
    fun setCurrentSearchParameters(search: String)

    fun getCurrentSortFilterParametersFlow(): StateFlow<Int>
    fun getCurrentSortParameterFlow(): StateFlow<Sorting>
    fun setCurrentSortFilterParametersFlow(itemId: Int)

    fun updateFilter(filter: Filter)

    fun addFilter(filter: Filter)

    fun removeFilter(filter: Filter)

    fun reset()
}