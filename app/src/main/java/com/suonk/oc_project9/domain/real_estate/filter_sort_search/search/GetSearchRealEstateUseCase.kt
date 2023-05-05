package com.suonk.oc_project9.domain.real_estate.filter_sort_search.search

import com.suonk.oc_project9.domain.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSearchRealEstateUseCase @Inject constructor(private val searchRepository: SearchRepository) {

    fun invoke() : Flow<String> = searchRepository.getCurrentSearchParametersFlow()
}