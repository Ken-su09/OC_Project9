package com.suonk.oc_project9.domain

import com.suonk.oc_project9.ui.filter.Filter
import com.suonk.oc_project9.utils.filter.FilterType
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

interface SearchRepository {

    fun getCurrentFilterParametersFlow(): StateFlow<List<Filter>>

    fun getCurrentSearchParametersFlow(): StateFlow<String>
    fun setCurrentSearchParametersFlow(search: String)

    fun updateFilter(filter: Filter)

    fun addFilter(filter: Filter)

    fun removeFilter(filter: Filter)

    fun reset()
}