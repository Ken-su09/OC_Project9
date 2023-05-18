package com.suonk.oc_project9.model.database.data.permission_checker

import android.Manifest.permission
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class PermissionChecker(private val context: Context) {


    fun hasFineLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(context, permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    fun hasCoarseLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(context, permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}