package com.suonk.oc_project9.ui.real_estates.details

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.content.Context
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.suonk.oc_project9.R
import com.suonk.oc_project9.domain.real_estate.GetPositionFromFullAddressUseCase
import com.suonk.oc_project9.domain.real_estate.GetRealEstateFlowByIdUseCase
import com.suonk.oc_project9.domain.real_estate.UpsertNewRealEstateUseCase
import com.suonk.oc_project9.domain.real_estate.UpdateRealEstateUseCase
import com.suonk.oc_project9.model.database.data.entities.Position
import com.suonk.oc_project9.model.database.data.entities.RealEstateEntity
import com.suonk.oc_project9.utils.*
import com.suonk.oc_project9.utils.Defaults.getDefaultEmptyRealEstateDetailsViewState
import com.suonk.oc_project9.utils.Defaults.getDefaultPhotoViewStates
import com.suonk.oc_project9.utils.Defaults.getDefaultRealEstateDetailsViewState
import io.mockk.*
import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RealEstateDetailsViewModelTest {

    companion object {
        private const val DEFAULT_ID = 1L

        private const val DEFAULT_GRID_ZONE = "55 West"
        private const val DEFAULT_STREET_NAME = "25th Street"
        private const val DEFAULT_CITY = "New York"
        private const val DEFAULT_STATE = "NY"
        private const val DEFAULT_POSTAL_CODE = "10010"
        private const val DEFAULT_FULL_ADDRESS = "55 West 25th Street, New York, NY, 10010"

        private const val DEFAULT_STATUS = "Available"

        private const val DEFAULT_LAT = 40.744080
        private const val DEFAULT_LONG = -73.991302
        private val DEFAULT_POSITION = Position(lat = DEFAULT_LAT, long = DEFAULT_LONG)

        private const val DEFAULT_TYPE = "Penthouse"
        private const val NEW_TYPE = "Duplex"

        private const val DEFAULT_PRICE = 29872000.0
        private const val DEFAULT_LIVING_SPACE = 8072.900
        private const val DEFAULT_NUMBER_ROOM = 8
        private const val DEFAULT_NUMBER_BEDROOM = 4
        private const val DEFAULT_NUMBER_BATHROOM = 4
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val insertNewRealEstateUseCase: UpsertNewRealEstateUseCase = mockk()
    private val getRealEstateFlowByIdUseCase: GetRealEstateFlowByIdUseCase = mockk()
    private val getPositionFromFullAddressUseCase: GetPositionFromFullAddressUseCase = mockk()
    private val updateRealEstateUseCase: UpdateRealEstateUseCase = mockk()

    private val context: Context = mockk()
    private val navArgProducer: NavArgProducer = mockk()

    private val realEstateDetailsViewModel = RealEstateDetailsViewModel(
        insertNewRealEstateUseCase = insertNewRealEstateUseCase,
        getRealEstateFlowByIdUseCase = getRealEstateFlowByIdUseCase,
        getPositionFromFullAddressUseCase = getPositionFromFullAddressUseCase,
        updateRealEstateUseCase = updateRealEstateUseCase,
        coroutineDispatcherProvider = testCoroutineRule.getTestCoroutineDispatcherProvider(),
        context = context,
        navArgProducer = navArgProducer
    )

    @Before
    fun setup() {
        justRun {

        }
        coJustRun { insertNewRealEstateUseCase.invoke(any(), arrayListOf()) }
        coJustRun { updateRealEstateUseCase.invoke(any(), arrayListOf()) }
        every { navArgProducer.getNavArgs(RealEstateDetailsFragmentArgs::class).id } returns 1L

        every {
            getRealEstateFlowByIdUseCase.invoke(navArgProducer.getNavArgs(RealEstateDetailsFragmentArgs::class).id)
        }
    }

    @Test
    fun `initial case`() = testCoroutineRule.runTest {
        // GIVEN
        every { navArgProducer.getNavArgs(RealEstateDetailsFragmentArgs::class).id } returns 0L

        // WHEN
        realEstateDetailsViewModel.realEstateDetailsViewStateLiveData.observeForTesting(this) {

            // THEN
            assertThat(it.value).isEqualTo(getDefaultEmptyRealEstateDetailsViewState())
            assertEquals(getDefaultEmptyRealEstateDetailsViewState(), it.value)
        }
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // WHEN
        realEstateDetailsViewModel.realEstateDetailsViewStateLiveData.observeForTesting(this) {

            // THEN
            assertThat(it.value).isEqualTo(getDefaultRealEstateDetailsViewState())
            assertEquals(getDefaultRealEstateDetailsViewState(), it.value)
        }
    }

    @Test
    fun `try to insert a real estate`() = testCoroutineRule.runTest {
        // GIVEN
        realEstateDetailsViewModel.onSaveRealEstateButtonClicked()

        every {
            context.getString(
                R.string.full_address, DEFAULT_GRID_ZONE, DEFAULT_STREET_NAME, DEFAULT_CITY, DEFAULT_STATE, DEFAULT_POSTAL_CODE
            )
        } returns DEFAULT_FULL_ADDRESS
        every {
            getPositionFromFullAddressUseCase.invoke(
                context.getString(
                    R.string.full_address, DEFAULT_GRID_ZONE, DEFAULT_STREET_NAME, DEFAULT_CITY, DEFAULT_STATE, DEFAULT_POSTAL_CODE
                ), context
            )
        } returns DEFAULT_POSITION

        coEvery {
            insertNewRealEstateUseCase.invoke(
                RealEstateEntity(
                    id = 0L,
                    type = NEW_TYPE,
                    price = DEFAULT_PRICE,
                    livingSpace = DEFAULT_LIVING_SPACE,
                    numberRooms = DEFAULT_NUMBER_ROOM,
                    numberBedroom = DEFAULT_NUMBER_BEDROOM,
                    numberBathroom = DEFAULT_NUMBER_BATHROOM,
                    description = "Anchored by a vast marble gallery with sweeping staircase, ",
                    postalCode = "10010",
                    state = DEFAULT_STATE,
                    city = DEFAULT_CITY,
                    streetName = DEFAULT_STREET_NAME,
                    gridZone = DEFAULT_GRID_ZONE,
                    pointOfInterest = "",
                    status = DEFAULT_STATUS,
                    entryDate = System.currentTimeMillis(),
                    saleDate = null,
                    latitude = DEFAULT_LAT,
                    longitude = DEFAULT_LONG,
                    agentInChargeId = 1L,
                ), arrayListOf()
            )
        }

        confirmVerified(insertNewRealEstateUseCase)
    }

    @Test
    fun `try to update a real estate`() = testCoroutineRule.runTest {
        // GIVEN
        realEstateDetailsViewModel.onSaveRealEstateButtonClicked()

        every {
            context.getString(
                R.string.full_address, DEFAULT_GRID_ZONE, DEFAULT_STREET_NAME, DEFAULT_CITY, DEFAULT_STATE, DEFAULT_POSTAL_CODE
            )
        } returns DEFAULT_FULL_ADDRESS
        every {
            getPositionFromFullAddressUseCase.invoke(
                context.getString(
                    R.string.full_address, DEFAULT_GRID_ZONE, DEFAULT_STREET_NAME, DEFAULT_CITY, DEFAULT_STATE, DEFAULT_POSTAL_CODE
                ), context
            )
        } returns DEFAULT_POSITION

        coEvery {
            insertNewRealEstateUseCase.invoke(
                RealEstateEntity(
                    id = DEFAULT_ID,
                    type = NEW_TYPE,
                    price = DEFAULT_PRICE,
                    livingSpace = DEFAULT_LIVING_SPACE,
                    numberRooms = DEFAULT_NUMBER_ROOM,
                    numberBedroom = DEFAULT_NUMBER_BEDROOM,
                    numberBathroom = DEFAULT_NUMBER_BATHROOM,
                    description = "Anchored by a vast marble gallery with sweeping staircase, ",
                    postalCode = "10010",
                    state = DEFAULT_STATE,
                    city = DEFAULT_CITY,
                    streetName = DEFAULT_STREET_NAME,
                    gridZone = DEFAULT_GRID_ZONE,
                    pointOfInterest = "",
                    status = DEFAULT_STATUS,
                    entryDate = System.currentTimeMillis(),
                    saleDate = null,
                    latitude = DEFAULT_LAT,
                    longitude = DEFAULT_LONG,
                    agentInChargeId = 1L,
                ), getDefaultPhotoViewStates()
            )
        }

        confirmVerified(insertNewRealEstateUseCase)
    }

}