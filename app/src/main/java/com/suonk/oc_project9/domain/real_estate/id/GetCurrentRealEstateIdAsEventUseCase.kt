package com.suonk.oc_project9.domain.real_estate.id

import com.suonk.oc_project9.domain.CurrentRealEstateIdRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCurrentRealEstateIdAsEventUseCase @Inject constructor(private val currentRealEstateIdRepository: CurrentRealEstateIdRepository) {

    fun invoke(): Flow<Long?> {
        return currentRealEstateIdRepository.getCurrentRealEstateIdChannel().receiveAsFlow()
    }
}