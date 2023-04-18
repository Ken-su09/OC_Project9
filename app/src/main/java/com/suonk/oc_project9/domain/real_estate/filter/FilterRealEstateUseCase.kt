package com.suonk.oc_project9.domain.real_estate.filter

import com.suonk.oc_project9.domain.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SortFilterRealEstateUseCase @Inject constructor(private val searchRepository: SearchRepository) {

    fun invoke(itemId: Int): Flow<Int> {
        searchRepository.setCurrentSearchParametersFlow(search)

        return searchRepository.getCurrentSearchParametersFlow()
    }
}