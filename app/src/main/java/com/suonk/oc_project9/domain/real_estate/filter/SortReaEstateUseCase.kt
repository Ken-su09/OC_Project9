package com.suonk.oc_project9.domain.real_estate.filter

import com.suonk.oc_project9.domain.SearchRepository
import com.suonk.oc_project9.utils.sort.Sorting
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SortReaEstateUseCase @Inject constructor(private val searchRepository: SearchRepository) {

    fun invoke(itemId: Int): Flow<Sorting> {
        searchRepository.setCurrentSortFilterParametersFlow(itemId)

        return searchRepository.getCurrentSortParameterFlow()
    }
}