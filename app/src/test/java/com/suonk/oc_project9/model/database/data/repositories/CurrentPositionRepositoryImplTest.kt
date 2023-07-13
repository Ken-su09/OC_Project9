package com.suonk.oc_project9.model.database.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.suonk.oc_project9.model.database.data.entities.places.Position
import com.suonk.oc_project9.utils.TestCoroutineRule
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runCurrent
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration.Companion.minutes

class CurrentPositionRepositoryImplTest {

    companion object {
        private const val DEFAULT_LATITUDE = 26.443
        private const val DEFAULT_LONGITUDE = 4.976
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val locationProviderClient: FusedLocationProviderClient = mockk()

    private val locationRequestSlot = slot<LocationRequest>()
    private val locationCallbackSlot = slot<LocationCallback>()

    private val currentPositionRepositoryImpl = CurrentPositionRepositoryImpl(
        locationProviderClient,
        testCoroutineRule.getTestCoroutineDispatcherProvider(),
    )

    @Before
    fun setUp() {
        every {
            locationProviderClient.requestLocationUpdates(
                capture(locationRequestSlot),
                any(),
                capture(locationCallbackSlot),
            )
        } returns mockk()
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // GIVEN
        val locationResult = mockk<LocationResult>()
        every { locationResult.lastLocation?.latitude } returns DEFAULT_LATITUDE
        every { locationResult.lastLocation?.longitude } returns DEFAULT_LONGITUDE
        currentPositionRepositoryImpl.startLocationUpdates()

        // WHEN
        currentPositionRepositoryImpl.getCurrentPositionFlow().test {
            locationCallbackSlot.captured.onLocationResult(locationResult)
            runCurrent()

            // THEN
            assertEquals(getDefaultPosition(), awaitItem())
            assertEquals(2.minutes.inWholeMilliseconds, locationRequestSlot.captured.intervalMillis)

            verify(exactly = 1) {
                locationProviderClient.requestLocationUpdates(any(), any(), any<LocationCallback>())
            }
            confirmVerified(locationProviderClient)
        }
    }

    @Test
    fun `edge case - startLocationUpdates is idempotent`() = testCoroutineRule.runTest {
        // GIVEN
        val locationResult = mockk<LocationResult>()
        every { locationResult.lastLocation?.latitude } returns DEFAULT_LATITUDE
        every { locationResult.lastLocation?.longitude } returns DEFAULT_LONGITUDE
        currentPositionRepositoryImpl.startLocationUpdates()

        // WHEN
        currentPositionRepositoryImpl.getCurrentPositionFlow().test {
            locationCallbackSlot.captured.onLocationResult(locationResult)
            runCurrent()

            currentPositionRepositoryImpl.startLocationUpdates()

            // THEN
            assertEquals(getDefaultPosition(), awaitItem())

            verify(exactly = 1) {
                locationProviderClient.requestLocationUpdates(any(), any(), any<LocationCallback>())
            }
            confirmVerified(locationProviderClient)
        }
    }

    @Test
    fun `initial case - no value published`() = testCoroutineRule.runTest {
        // WHEN
        currentPositionRepositoryImpl.getCurrentPositionFlow().test {
            // THEN
            expectNoEvents()

            confirmVerified(locationProviderClient)
        }
    }

    @Test
    fun `nominal case - stop location updates without starting before`() = testCoroutineRule.runTest {
        // WHEN
        currentPositionRepositoryImpl.stopLocationUpdates()

        // THEN
        confirmVerified(locationProviderClient)
    }

    @Test
    fun `nominal case - stop location updates with starting before`() = testCoroutineRule.runTest {
        // GIVEN
        val locationCallbackSlot = slot<LocationCallback>()
        every { locationProviderClient.requestLocationUpdates(any(), any(), capture(locationCallbackSlot)) } returns mockk()
        every { locationProviderClient.removeLocationUpdates(any<LocationCallback>()) } returns mockk()
        currentPositionRepositoryImpl.startLocationUpdates()

        // WHEN
        currentPositionRepositoryImpl.stopLocationUpdates()

        // THEN
        verify(exactly = 1) {
            locationProviderClient.requestLocationUpdates(any(), any(), locationCallbackSlot.captured)
            locationProviderClient.removeLocationUpdates(locationCallbackSlot.captured)
        }
        confirmVerified(locationProviderClient)
    }

    private fun getDefaultPosition() = Position(
        lat = DEFAULT_LATITUDE,
        long = DEFAULT_LONGITUDE,
    )
}