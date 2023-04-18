package com.suonk.oc_project9.domain.real_estate.get

import com.suonk.oc_project9.domain.RealEstateRepository
import com.suonk.oc_project9.model.database.data.entities.real_estate.RealEstateEntityWithPhotos
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetRealEstateFlowByIdUseCase @Inject constructor(private val realEstateRepository: RealEstateRepository) {

    fun invoke(id: Long): Flow<RealEstateEntityWithPhotos?> = realEstateRepository.getRealEstateById(id)
}