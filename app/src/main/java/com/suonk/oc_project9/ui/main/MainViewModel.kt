package com.suonk.oc_project9.ui.main

import android.annotation.SuppressLint
import androidx.lifecycle.*
import com.suonk.oc_project9.domain.CurrentPositionRepository
import com.suonk.oc_project9.domain.real_estate.id.GetCurrentRealEstateIdAsEventUseCase
import com.suonk.oc_project9.model.database.data.permission_checker.PermissionChecker
import com.suonk.oc_project9.utils.CoroutineDispatcherProvider
import com.suonk.oc_project9.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val currentPositionRepository: CurrentPositionRepository,
    private val getCurrentRealEstateIdChannelUseCase: GetCurrentRealEstateIdAsEventUseCase,
    private val permissionChecker: PermissionChecker,
    private val dispatcherProvider: CoroutineDispatcherProvider
) : ViewModel() {

    private val isPermissionEnabledFlow = MutableStateFlow(false)
    private var isTablet = false

    val mainViewAction: LiveData<Event<MainViewAction>> = liveData() {
        getCurrentRealEstateIdChannelUseCase.invoke().collect { id ->
            if (!isTablet && id != null) {
                emit(Event(MainViewAction.Navigate.Detail(id)))
            }
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