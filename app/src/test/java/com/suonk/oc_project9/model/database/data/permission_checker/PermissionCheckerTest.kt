package com.suonk.oc_project9.model.database.data.permission_checker

import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.Test

class PermissionCheckerTest {

    private val application: Application = mockk()

    private val permissionChecker = PermissionChecker(application)

//    @Test
//    fun `test permission checker has fine location permission - false`() {
//        // GIVEN
//        every {
//            ContextCompat.checkSelfPermission(application, android.Manifest.permission.ACCESS_FINE_LOCATION)
//        } returns PackageManager.PERMISSION_DENIED
//
//        // WHEN
//        val result = permissionChecker.hasFineLocationPermission()
//        assertEquals(false, result)
//
//        // THEN
//        verify {
//            ContextCompat.checkSelfPermission(application, android.Manifest.permission.ACCESS_FINE_LOCATION)
//        }
//    }
//
//    @Test
//    fun `test permission checker has fine location permission - true`() {
//        // GIVEN
//        every {
//            ContextCompat.checkSelfPermission(application, android.Manifest.permission.ACCESS_FINE_LOCATION)
//        } returns PackageManager.PERMISSION_GRANTED
//
//        // WHEN
//        val result = permissionChecker.hasFineLocationPermission()
//        assertEquals(true, result)
//
//        // THEN
//        verify {
//            ContextCompat.checkSelfPermission(application, android.Manifest.permission.ACCESS_FINE_LOCATION)
//        }
//    }
}