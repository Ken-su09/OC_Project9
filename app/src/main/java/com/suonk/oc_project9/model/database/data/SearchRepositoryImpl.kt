package com.suonk.oc_project9.model.database.data

import com.suonk.oc_project9.domain.SearchRepository
import com.suonk.oc_project9.ui.filter.Filter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class SearchRepositoryImpl : SearchRepository {

    private val currentSearchParameterFlow: Flow<Filter> = MutableStateFlow<Filter>(    )

    override fun getCurrentSearchParametersFlow(): Flow<Filter> {

    }
}