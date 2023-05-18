package com.suonk.oc_project9.model.database.data.permission_checker

import android.Manifest.permission
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionChecker @Inject constructor(private val application: Application) {


    fun hasFineLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(application, permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    fun hasCoarseLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(application, permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}