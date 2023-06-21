package com.suonk.oc_project9.domain

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

interface CurrentRealEstateIdRepository {

    fun getCurrentRealEstateIdFlow(): StateFlow<Long?>
    fun getCurrentRealEstateIdChannel(): Channel<Long?>

    fun setCurrentRealEstateIdFlow(id: Long?)
}