package com.suonk.oc_project9.domain.real_estate.filter_sort_search.search

import com.suonk.oc_project9.domain.SearchRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SetSearchRealEstateUseCase @Inject constructor(private val searchRepository: SearchRepository) {

    fun invoke(search: String) = searchRepository.setCurrentSearchParameters(search)
}