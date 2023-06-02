package com.suonk.oc_project9.domain.real_estate.id

import com.suonk.oc_project9.domain.CurrentRealEstateIdRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SetCurrentRealEstateIdUseCase @Inject constructor(private val currentRealEstateIdRepository: CurrentRealEstateIdRepository) {

    fun invoke(id: Long) {
        currentRealEstateIdRepository.setCurrentRealEstateIdFlow(id)
    }
}