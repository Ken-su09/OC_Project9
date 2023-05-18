package com.suonk.oc_project9.ui.main

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.suonk.oc_project9.domain.CurrentPositionRepository
import com.suonk.oc_project9.model.database.data.permission_checker.PermissionChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val currentPositionRepository: CurrentPositionRepository,
    private val permissionChecker: PermissionChecker,

    ) : ViewModel() {

    private val isPermissionEnabledFlow = MutableStateFlow(false)

    @SuppressLint("MissingPermission")
    fun onStart() {
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

    @SuppressLint("MissingPermission")
    fun onResume() {
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