package com.suonk.oc_project9.domain.real_estate

import com.suonk.oc_project9.domain.RealEstateRepository
import com.suonk.oc_project9.model.database.data.entities.RealEstateEntityWithPhotos
import com.suonk.oc_project9.utils.sort.SortField
import com.suonk.oc_project9.utils.sort.SortType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllRealEstatesUseCase @Inject constructor(private val realEstateRepository: RealEstateRepository) {

    fun invoke(): Flow<List<RealEstateEntityWithPhotos>> {
        return realEstateRepository.getAllRealEstatesWithPhotos()
//        sortField.sortType.invoke(realEstates)
    }
}