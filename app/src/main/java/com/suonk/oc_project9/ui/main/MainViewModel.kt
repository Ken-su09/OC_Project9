package com.suonk.oc_project9.ui.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.*
import com.suonk.oc_project9.domain.CurrentPositionRepository
import com.suonk.oc_project9.model.database.data.CurrentRealEstateIdRepositoryImpl
import com.suonk.oc_project9.model.database.data.permission_checker.PermissionChecker
import com.suonk.oc_project9.utils.Event
import com.suonk.oc_project9.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val currentPositionRepository: CurrentPositionRepository,
    private val currentRealEstateIdRepositoryImpl: CurrentRealEstateIdRepositoryImpl,
    private val permissionChecker: PermissionChecker,

    ) : ViewModel() {

    private val isPermissionEnabledFlow = MutableStateFlow(false)

    val mainViewAction: LiveData<Event<MainViewAction>> = liveData {
        currentRealEstateIdRepositoryImpl.getCurrentRealEstateIdFlow().collect { id ->
            if (isTablet) {
                emit(Event(MainViewAction.Navigate.Detail(id)))
            }
        }
    }
    private var isTablet = false

    sealed class MainViewAction {
        sealed class Navigate : MainViewAction() {
            data class Detail(val realEstateId: Long) : Navigate()
        }
    }

    @SuppressLint("MissingPermission")
    fun onResume(isTablet: Boolean) {
        this.isTablet = isTablet
        if (permissionChecker.hasFineLocationPermission() || permissionChecker.hasCoarseLocationPermission()) {
            currentPositionRepository.startLocationUpdates()
            isPermissionEnabledFlow.update {
                true
            }
        } else {
            currentPositionRepository.stopLocationUpdates()
            isPermissionEnabledFlow.update {
                false
            }
        }
    }

    fun onStop() {
        currentPositionRepository.stopLocationUpdates()
        isPermissionEnabledFlow.update {
            false
        }
    }

    fun getPermissionsLiveData(): LiveData<Boolean> {
        return isPermissionEnabledFlow.asLiveData()
    }
}