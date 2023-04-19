package com.suonk.oc_project9.domain.real_estate.filter

import com.suonk.oc_project9.domain.SearchRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRealEstateUseCase @Inject constructor(private val searchRepository: SearchRepository) {

    fun invoke(search: String) {
        searchRepository.setCurrentSearchParameters(search)
    }
}