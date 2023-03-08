package com.suonk.oc_project9.domain

import com.suonk.oc_project9.ui.filter.Filter
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun getCurrentSearchParametersFlow(): Flow<Filter>
}