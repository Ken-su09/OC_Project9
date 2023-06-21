package com.suonk.oc_project9.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.suonk.oc_project9.domain.CurrentPositionRepository
import com.suonk.oc_project9.domain.real_estate.id.GetCurrentRealEstateIdAsEventUseCase
import com.suonk.oc_project9.model.database.data.permission_checker.PermissionChecker
import com.suonk.oc_project9.utils.Event
import com.suonk.oc_project9.utils.TestCoroutineRule
import com.suonk.oc_project9.utils.observeForTesting
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    private companion object {
        private const val IS_TABLET_FALSE = false
        private const val IS_TABLET_TRUE = true
        private const val DEFAULT_ID = 1L
        private val DEFAULT_EVENT = Event(content = MainViewAction.Navigate.Detail(realEstateId = DEFAULT_ID))
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val currentPositionRepository: CurrentPositionRepository = mockk()
    private val getCurrentRealEstateIdChannelUseCase: GetCurrentRealEstateIdAsEventUseCase = mockk()
    private val permissionChecker: PermissionChecker = mockk()

    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setup() {
        mainViewModel = MainViewModel(
            currentPositionRepository = currentPositionRepository,
            getCurrentRealEstateIdChannelUseCase = getCurrentRealEstateIdChannelUseCase,
            permissionChecker = permissionChecker,
            dispatcherProvider = testCoroutineRule.getTestCoroutineDispatcherProvider()
        )
    }

    @Test
    fun `initial case`() = testCoroutineRule.runTest {
        // WHEN
        every { permissionChecker.hasFineLocationPermission() } returns false
        every { permissionChecker.hasCoarseLocationPermission() } returns false
        justRun { currentPositionRepository.stopLocationUpdates() }

        mainViewModel.onResume(IS_TABLET_FALSE)

        mainViewModel.getPermissionsLiveData().observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(false)

            verify {
                permissionChecker.hasFineLocationPermission()
                permissionChecker.hasCoarseLocationPermission()
                currentPositionRepository.stopLocationUpdates()
            }

            confirmVerified(currentPositionRepository, getCurrentRealEstateIdChannelUseCase, permissionChecker)
        }
    }

    @Test
    fun `test permissions if location permissions are true after on resume`() = testCoroutineRule.runTest {
        // WHEN
        every { permissionChecker.hasFineLocationPermission() } returns true
        justRun { currentPositionRepository.startLocationUpdates() }

        mainViewModel.onResume(IS_TABLET_FALSE)

        mainViewModel.getPermissionsLiveData().observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(true)

            verify {
                permissionChecker.hasFineLocationPermission()
                currentPositionRepository.startLocationUpdates()
            }

            confirmVerified(currentPositionRepository, getCurrentRealEstateIdChannelUseCase, permissionChecker)
        }
    }

    @Test
    fun `test permissions if fine location false but coarse location true after on resume`() = testCoroutineRule.runTest {
        // WHEN
        every { permissionChecker.hasFineLocationPermission() } returns false
        every { permissionChecker.hasCoarseLocationPermission() } returns true
        justRun { currentPositionRepository.startLocationUpdates() }

        mainViewModel.onResume(IS_TABLET_FALSE)

        mainViewModel.getPermissionsLiveData().observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(true)

            verify {
                permissionChecker.hasFineLocationPermission()
                permissionChecker.hasCoarseLocationPermission()
                currentPositionRepository.startLocationUpdates()
            }

            confirmVerified(currentPositionRepository, getCurrentRealEstateIdChannelUseCase, permissionChecker)
        }
    }

    @Test
    fun `test permissions after on stop`() = testCoroutineRule.runTest {
        // WHEN
        justRun { currentPositionRepository.stopLocationUpdates() }

        mainViewModel.onStop()

        mainViewModel.getPermissionsLiveData().observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(false)

            verify {
                currentPositionRepository.stopLocationUpdates()
            }

            confirmVerified(currentPositionRepository, getCurrentRealEstateIdChannelUseCase, permissionChecker)
        }
    }

    @Test
    fun `test main view action with is tablet false and id null`() = testCoroutineRule.runTest {
        // WHEN
        every { permissionChecker.hasFineLocationPermission() } returns false
        every { permissionChecker.hasCoarseLocationPermission() } returns false
        justRun { currentPositionRepository.stopLocationUpdates() }
        every { getCurrentRealEstateIdChannelUseCase.invoke() } returns flowOf(null)

        mainViewModel.onResume(IS_TABLET_FALSE)

        mainViewModel.mainViewAction.observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(null)

            verify {
                permissionChecker.hasFineLocationPermission()
                permissionChecker.hasCoarseLocationPermission()
                currentPositionRepository.stopLocationUpdates()
                getCurrentRealEstateIdChannelUseCase.invoke()
            }

            confirmVerified(currentPositionRepository, getCurrentRealEstateIdChannelUseCase, permissionChecker)
        }
    }

    @Test
    fun `test main view action with is tablet true and id null`() = testCoroutineRule.runTest {
        // WHEN
        every { permissionChecker.hasFineLocationPermission() } returns false
        every { permissionChecker.hasCoarseLocationPermission() } returns false
        justRun { currentPositionRepository.stopLocationUpdates() }
        every { getCurrentRealEstateIdChannelUseCase.invoke() } returns flowOf(null)

        mainViewModel.onResume(IS_TABLET_TRUE)

        mainViewModel.mainViewAction.observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(null)

            verify {
                permissionChecker.hasFineLocationPermission()
                permissionChecker.hasCoarseLocationPermission()
                currentPositionRepository.stopLocationUpdates()
                getCurrentRealEstateIdChannelUseCase.invoke()
            }

            confirmVerified(currentPositionRepository, getCurrentRealEstateIdChannelUseCase, permissionChecker)
        }
    }

    @Test
    fun `test main view action with is tablet false and id 1L`() = testCoroutineRule.runTest {
        // WHEN
        every { permissionChecker.hasFineLocationPermission() } returns false
        every { permissionChecker.hasCoarseLocationPermission() } returns false
        justRun { currentPositionRepository.stopLocationUpdates() }
        every { getCurrentRealEstateIdChannelUseCase.invoke() } returns flowOf(DEFAULT_ID)

        mainViewModel.onResume(IS_TABLET_FALSE)

        mainViewModel.mainViewAction.observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(DEFAULT_EVENT)

            verify {
                permissionChecker.hasFineLocationPermission()
                permissionChecker.hasCoarseLocationPermission()
                currentPositionRepository.stopLocationUpdates()
                getCurrentRealEstateIdChannelUseCase.invoke()
            }

            confirmVerified(currentPositionRepository, getCurrentRealEstateIdChannelUseCase, permissionChecker)
        }
    }

    @Test
    fun `test main view action with is tablet true and id 1L`() = testCoroutineRule.runTest {
        // WHEN
        every { permissionChecker.hasFineLocationPermission() } returns false
        every { permissionChecker.hasCoarseLocationPermission() } returns false
        justRun { currentPositionRepository.stopLocationUpdates() }
        every { getCurrentRealEstateIdChannelUseCase.invoke() } returns flowOf(DEFAULT_ID)

        mainViewModel.onResume(IS_TABLET_TRUE)

        mainViewModel.mainViewAction.observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(null)

            verify {
                permissionChecker.hasFineLocationPermission()
                permissionChecker.hasCoarseLocationPermission()
                currentPositionRepository.stopLocationUpdates()
                getCurrentRealEstateIdChannelUseCase.invoke()
            }

            confirmVerified(currentPositionRepository, getCurrentRealEstateIdChannelUseCase, permissionChecker)
        }
    }
}