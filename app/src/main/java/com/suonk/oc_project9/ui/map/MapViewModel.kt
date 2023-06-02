package com.suonk.oc_project9.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.suonk.oc_project9.R
import com.suonk.oc_project9.domain.CurrentPositionRepository
import com.suonk.oc_project9.domain.real_estate.get.GetAllRealEstatesUseCase
import com.suonk.oc_project9.model.database.data.entities.places.Position
import com.suonk.oc_project9.utils.CoroutineDispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getAllRealEstatesUseCase: GetAllRealEstatesUseCase,
    private val currentPositionRepository: CurrentPositionRepository,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,

    ) : ViewModel() {

    val mapViewStateLiveData: LiveData<MapViewState> = liveData(coroutineDispatcherProvider.io) {
        combine(getAllRealEstatesUseCase.invoke(), currentPositionRepository.getCurrentPositionFlow()) { realEstate, position ->
            val list = realEstate.map {
                Position(it.realEstateEntity.latitude, it.realEstateEntity.longitude)
            }
            val mapViewState = MapViewState(0L, "", position.lat, position.long, R.drawable.ic_custom_google_marker_red, list)
            emit(mapViewState)
        }.collectLatest { }
    }
}