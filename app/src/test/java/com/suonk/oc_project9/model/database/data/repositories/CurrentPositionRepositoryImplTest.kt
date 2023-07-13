package com.suonk.oc_project9.model.database.data.repositories

import android.os.Looper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.tasks.Task
import com.suonk.oc_project9.model.database.data.entities.places.Position
import com.suonk.oc_project9.utils.TestCoroutineRule
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test

class CurrentPositionRepositoryImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val locationProviderClient: FusedLocationProviderClient = mockk()
    private val looper: Looper = mockk()
    private val taskVoid: Task<Void> = mockk()

    private val currentPositionRepositoryImpl = CurrentPositionRepositoryImpl(locationProviderClient, looper)

    @Test
    fun `get position flow without start location update`() = testCoroutineRule.runTest {
        // GIVEN
//        val locationCallbackSlot = slot<LocationCallback>()
//        every { locationProviderClient.requestLocationUpdates(any(), capture(locationCallbackSlot), looper) } returns taskVoid

        // WHEN
//        currentPositionRepositoryImpl.startLocationUpdates()
        currentPositionRepositoryImpl.getCurrentPositionFlow().test {
            // THEN
            assertEquals(getDefaultPosition(), awaitItem())
//            awaitComplete()

//            verify { locationProviderClient.requestLocationUpdates(any(), capture(locationCallbackSlot), any()) }
            confirmVerified(locationProviderClient)
        }
    }

    @Test
    fun `get position flow with start location update`() = testCoroutineRule.runTest {
        // GIVEN
        val locationCallbackSlot = slot<LocationCallback>()
        every { locationProviderClient.requestLocationUpdates(any(), capture(locationCallbackSlot), looper) } returns taskVoid

        // WHEN
        currentPositionRepositoryImpl.startLocationUpdates()
        currentPositionRepositoryImpl.getCurrentPositionFlow().test {
            // THEN
            assertEquals(getDefaultPosition(), awaitItem())
//            awaitComplete()

            verify { locationProviderClient.requestLocationUpdates(any(), capture(locationCallbackSlot), any()) }
            confirmVerified(locationProviderClient)
        }
    }

    @Test
    fun `start location updates test with location call back null`() = testCoroutineRule.runTest {
        // GIVEN
        val locationCallbackSlot = slot<LocationCallback>()
        every { locationProviderClient.requestLocationUpdates(any(), capture(locationCallbackSlot), looper) } returns taskVoid

        // WHEN
        currentPositionRepositoryImpl.startLocationUpdates()

        // THEN
        verify { locationProviderClient.requestLocationUpdates(any(), capture(locationCallbackSlot), any()) }
        confirmVerified(locationProviderClient)
    }

    @Test
    fun `start then stop location updates test with location call back null`() = testCoroutineRule.runTest {
        // GIVEN
        val locationCallbackSlot = slot<LocationCallback>()
        every { locationProviderClient.requestLocationUpdates(any(), capture(locationCallbackSlot), looper) } returns taskVoid
        every { locationProviderClient.removeLocationUpdates(capture(locationCallbackSlot)) } returns taskVoid

        // WHEN
        currentPositionRepositoryImpl.startLocationUpdates()

        // THEN
        verify { locationProviderClient.requestLocationUpdates(any(), capture(locationCallbackSlot), any()) }

        // WHEN
        currentPositionRepositoryImpl.stopLocationUpdates()

        // THEN
        verify {
            locationProviderClient.removeLocationUpdates(capture(locationCallbackSlot))
        }
        confirmVerified(locationProviderClient)
    }

    @Test
    fun `stop location updates test with location call back null`() = testCoroutineRule.runTest {
        // GIVEN
        val locationCallbackSlot = slot<LocationCallback>()
        every { locationProviderClient.removeLocationUpdates(capture(locationCallbackSlot)) } returns taskVoid

        // WHEN
        currentPositionRepositoryImpl.stopLocationUpdates()

        // THEN
        confirmVerified(locationProviderClient)
    }

    private fun getDefaultPosition(): Position {
        return Position(0.0, 0.0)
    }
}