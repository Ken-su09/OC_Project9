package com.suonk.oc_project9.domain.real_estate.filter_sort_search.sort_filter_parameters

import com.suonk.oc_project9.domain.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCurrentSortFilterParametersUseCase @Inject constructor(private val searchRepository: SearchRepository) {

    fun invoke(): Flow<Int> {
        return searchRepository.getCurrentSortFilterParametersFlow()
    }
}