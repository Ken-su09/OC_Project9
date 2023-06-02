package com.suonk.oc_project9.domain

import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

interface CurrentRealEstateIdRepository {

    fun getCurrentRealEstateIdFlow(): Flow<Long?>
    fun setCurrentRealEstateIdFlow(id: Long)
}