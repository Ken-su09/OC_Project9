package com.suonk.oc_project9.domain.real_estate

import com.suonk.oc_project9.domain.RealEstateRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetRealEstateFlowByIdUseCase @Inject constructor(private val realEstateRepository: RealEstateRepository) {

    fun invoke(id: Long) = realEstateRepository.getRealEstateById(id)
}