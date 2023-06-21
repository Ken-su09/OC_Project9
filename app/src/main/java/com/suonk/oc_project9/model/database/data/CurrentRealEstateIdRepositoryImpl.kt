package com.suonk.oc_project9.model.database.data

import com.suonk.oc_project9.domain.CurrentRealEstateIdRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentRealEstateIdRepositoryImpl @Inject constructor() : CurrentRealEstateIdRepository {

    private val currentRealEstateIdFlow = MutableStateFlow<Long?>(null)
    private val currentRealEstateIdChannel = Channel<Long?>(
        capacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override fun getCurrentRealEstateIdFlow(): StateFlow<Long?> = currentRealEstateIdFlow
    override fun getCurrentRealEstateIdChannel(): Channel<Long?> = currentRealEstateIdChannel

    override fun setCurrentRealEstateIdFlow(id: Long?) {
        currentRealEstateIdFlow.value = id
        currentRealEstateIdChannel.trySend(id)
    }
}