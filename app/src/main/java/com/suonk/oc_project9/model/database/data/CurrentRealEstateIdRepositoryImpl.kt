package com.suonk.oc_project9.model.database.data

import com.suonk.oc_project9.domain.CurrentRealEstateIdRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentRealEstateIdRepositoryImpl @Inject constructor() : CurrentRealEstateIdRepository {

    private val currentRealEstateIdFlow = MutableSharedFlow<Long>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getCurrentRealEstateIdFlow(): Flow<Long> = currentRealEstateIdFlow

    override fun setCurrentRealEstateIdFlow(id: Long) {
        currentRealEstateIdFlow.tryEmit(id)
    }
}