package com.suonk.oc_project9.domain.real_estate

import com.suonk.oc_project9.domain.RealEstateRepository
import com.suonk.oc_project9.model.database.data.entities.RealEstateEntityWithPhotos
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllRealEstatesUseCase @Inject constructor(private val realEstateRepository: RealEstateRepository) {

    fun invoke(): Flow<List<RealEstateEntityWithPhotos>> = realEstateRepository.getAllRealEstatesWithPhotos()
}