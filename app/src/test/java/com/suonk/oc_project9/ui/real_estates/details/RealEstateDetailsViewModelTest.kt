package com.suonk.oc_project9.ui.real_estates.details

import android.app.Application
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.suonk.oc_project9.R
import com.suonk.oc_project9.domain.places.GetNearbyPointsOfInterestUseCase
import com.suonk.oc_project9.domain.real_estate.get.GetPositionFromFullAddressUseCase
import com.suonk.oc_project9.domain.real_estate.get.GetRealEstateFlowByIdUseCase
import com.suonk.oc_project9.domain.real_estate.upsert.UpsertNewRealEstateUseCase
import com.suonk.oc_project9.model.database.data.entities.places.Position
import com.suonk.oc_project9.model.database.data.entities.real_estate.PhotoEntity
import com.suonk.oc_project9.model.database.data.entities.real_estate.RealEstateEntity
import com.suonk.oc_project9.model.database.data.entities.real_estate.RealEstateEntityWithPhotos
import com.suonk.oc_project9.utils.EquatableCallback
import com.suonk.oc_project9.utils.NavArgProducer
import com.suonk.oc_project9.utils.TestCoroutineRule
import com.suonk.oc_project9.utils.observeForTesting
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import java.math.BigDecimal
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class RealEstateDetailsViewModelTest {

    companion object {
        const val DEFAULT_ID = 1L
        const val AGENT_ID = 1L

        const val DEFAULT_GRID_ZONE = "55 West"
        const val DEFAULT_STREET_NAME = "25th Street"
        const val DEFAULT_CITY = "New York"
        const val DEFAULT_STATE = "NY"
        const val DEFAULT_POSTAL_CODE = "10010"
        const val DEFAULT_FULL_ADDRESS = "55 West 25th Street, New York, NY, 10010"

        const val ADD_SUCCESSFUL = "A Real Estate was successfully added"

        const val DEFAULT_STATUS_AVAILABLE = "Available"
        const val DEFAULT_STATUS_NOT_AVAILABLE = "Not Available"
        const val DEFAULT_DESCRIPTION = "Anchored by a vast marble gallery with sweeping staircase, "

        const val DEFAULT_LAT = 40.744081
        const val DEFAULT_LONG = -73.991302
        private val DEFAULT_POSITION = Position(lat = DEFAULT_LAT, long = DEFAULT_LONG)

        const val DEFAULT_TYPE = "Penthouse"
        val DEFAULT_TYPE_POSITION = realEstateTypeToSpinnerPosition(DEFAULT_TYPE)

        val DEFAULT_PRICE = BigDecimal(29872000)
        const val DEFAULT_PRICE_STRING = "29 872 000"
        const val DEFAULT_PRICE_STRING_TO_ADDED = "29872000"
        const val DEFAULT_LIVING_SPACE = 8072.9
        const val DEFAULT_LIVING_SPACE_STRING = "8072.9"
        const val DEFAULT_NUMBER_ROOM = 8
        const val DEFAULT_NUMBER_ROOM_STRING = "8"
        const val DEFAULT_NUMBER_BEDROOM = 4
        const val DEFAULT_NUMBER_BEDROOM_STRING = "4"
        const val DEFAULT_NUMBER_BATHROOM = 2
        const val DEFAULT_NUMBER_BATHROOM_STRING = "2"
        const val DEFAULT_TIMESTAMP_LONG = 1_000_000_000L // 09/09/2001 - 01:46:40

        private const val DEFAULT_ID_TO_ADD = 0L
        private const val DEFAULT_EMPTY_TYPE_POSITION = 0
        private val DEFAULT_EMPTY_TYPE = spinnerPositionToType(DEFAULT_EMPTY_TYPE_POSITION)
        private val DEFAULT_NEW_TYPE = spinnerPositionToType(2)

        val DEFAULT_EMPTY_PRICE = BigDecimal(0.0)
        private const val DEFAULT_EMPTY_PRICE_STRING = "0.0"

        private const val DEFAULT_EMPTY_LIVING_SPACE = 0.0
        private const val DEFAULT_EMPTY_LIVING_SPACE_STRING = "0.0"

        private const val DEFAULT_EMPTY_ROOMS = 0
        private const val DEFAULT_EMPTY_ROOMS_STRING = "0"

        private const val DEFAULT_EMPTY_BEDROOMS = 0
        private const val DEFAULT_EMPTY_BEDROOMS_STRING = "0"

        private const val DEFAULT_EMPTY_BATHROOMS = 0
        private const val DEFAULT_EMPTY_BATHROOMS_STRING = "0"

        private const val DEFAULT_EMPTY_DESCRIPTION = ""
        private const val DEFAULT_EMPTY_CITY = ""
        private const val DEFAULT_EMPTY_POSTAL_CODE = ""
        private const val DEFAULT_EMPTY_STATE = ""
        private const val DEFAULT_EMPTY_STREET_NAME = ""
        private const val DEFAULT_EMPTY_GRID_ZONE = ""
        private const val DEFAULT_EMPTY_LATITUDE = 0.0
        private const val DEFAULT_EMPTY_LONGITUDE = 0.0

        private const val NEW_TYPE = "Duplex"
        private val NEW_TYPE_POSITION = realEstateTypeToSpinnerPosition(NEW_TYPE)
        private const val NEW_DESCRIPTION = "a recommandé l'album Biting the Apple et l'a ajouté à sa liste High on a cloud"
        private const val NEW_LIVING_SPACE = 6530.1
        private const val NEW_LIVING_SPACE_STRING = "6530.1"
        private const val NEW_NUMBER_ROOM = 5
        private const val NEW_NUMBER_ROOM_STRING = "5"
        private const val NEW_NUMBER_BEDROOM = 2
        private const val NEW_NUMBER_BEDROOM_STRING = "2"
        private const val NEW_NUMBER_BATHROOM = 1
        private const val NEW_NUMBER_BATHROOM_STRING = "1"

        private val NEW_PRICE = BigDecimal(1872000)
        private const val NEW_PRICE_STRING = "1 872 000"
        private const val NEW_PRICE_STRING_TO_UPDATE = "1872000"

        private const val FIELD_EMPTY_TOAST_MSG = "Fields can not be empty"
        private const val FIELD_ARE_NOT_DIGITS_MSG = "Fields should be digits"

        private const val WRONG_PRICE_STRING = "WRONG_PRICE_STRING"
        private const val WRONG_LIVING_SPACE_STRING = "WRONG_LIVING_SPACE_STRING"
        private const val WRONG_NUMBER_ROOM_STRING = "WRONG_NUMBER_ROOM_STRING"
        private const val WRONG_NUMBER_BEDROOM_STRING = "WRONG_NUMBER_BEDROOM_STRING"
        private const val WRONG_NUMBER_BATHROOM_STRING = "WRONG_NUMBER_BATHROOM_STRING"

        private fun realEstateTypeToSpinnerPosition(type: String): Int {
            val types = arrayListOf("House", "Penthouse", "Duplex", "Flat", "Loft")
            return types.indexOf(type)
        }

        private fun spinnerPositionToType(position: Int): String {
            val types = arrayListOf("House", "Penthouse", "Duplex", "Flat", "Loft")
            return types[position]
        }
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val upsertNewRealEstateUseCase: UpsertNewRealEstateUseCase = mockk()
    private val getRealEstateFlowByIdUseCase: GetRealEstateFlowByIdUseCase = mockk()
    private val getPositionFromFullAddressUseCase: GetPositionFromFullAddressUseCase = mockk()
    private val getNearbyPointsOfInterestUseCase: GetNearbyPointsOfInterestUseCase = mockk()

    private val application: Application = mockk()
    private val navArgProducer: NavArgProducer = mockk()

    private val firstMockUriPhoto = mock(Uri::class.java)

    private val fixedClock = Clock.fixed(Instant.ofEpochSecond(DEFAULT_TIMESTAMP_LONG), ZoneOffset.UTC)

    private val realEstateDetailsViewModel = RealEstateDetailsViewModel(
        upsertNewRealEstateUseCase = upsertNewRealEstateUseCase,
        getRealEstateFlowByIdUseCase = getRealEstateFlowByIdUseCase,
        getPositionFromFullAddressUseCase = getPositionFromFullAddressUseCase,
        getNearbyPointsOfInterestUseCase = getNearbyPointsOfInterestUseCase,
        coroutineDispatcherProvider = testCoroutineRule.getTestCoroutineDispatcherProvider(),
        application = application,
        navArgProducer = navArgProducer,
        clock = fixedClock
    )

    @Before
    fun setup() {
        every { navArgProducer.getNavArgs(RealEstateDetailsFragmentArgs::class).realEstateId } returns DEFAULT_ID
        every { getRealEstateFlowByIdUseCase.invoke(DEFAULT_ID) } returns flowOf(getDefaultRealEstateEntityWithPhotos())
        coEvery { getNearbyPointsOfInterestUseCase.invoke(lat = DEFAULT_LAT, long = DEFAULT_LONG) } returns arrayListOf()
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // WHEN
        realEstateDetailsViewModel.realEstateDetailsViewStateLiveData.observeForTesting(this) {

            // THEN
            assertThat(it.value).isEqualTo(getDefaultRealEstateDetailsViewState())

            coVerify(exactly = 1) {
                navArgProducer.getNavArgs(RealEstateDetailsFragmentArgs::class).realEstateId
                getRealEstateFlowByIdUseCase.invoke(DEFAULT_ID)
                getNearbyPointsOfInterestUseCase.invoke(lat = DEFAULT_LAT, long = DEFAULT_LONG)
            }
            confirmVerified(
                upsertNewRealEstateUseCase,
                getRealEstateFlowByIdUseCase,
                getPositionFromFullAddressUseCase,
                getNearbyPointsOfInterestUseCase,
                application,
                navArgProducer
            )
        }
    }

    @Test
    fun `initial case`() = testCoroutineRule.runTest {
        // GIVEN
        every { navArgProducer.getNavArgs(RealEstateDetailsFragmentArgs::class).realEstateId } returns DEFAULT_ID
        every { getRealEstateFlowByIdUseCase.invoke(DEFAULT_ID) } returns flowOf(null)

        // WHEN
        realEstateDetailsViewModel.realEstateDetailsViewStateLiveData.observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(getDefaultEmptyRealEstateDetailsViewState())

            verify(exactly = 1) {
                navArgProducer.getNavArgs(RealEstateDetailsFragmentArgs::class).realEstateId
                getRealEstateFlowByIdUseCase.invoke(DEFAULT_ID)
            }
            confirmVerified(
                upsertNewRealEstateUseCase,
                getRealEstateFlowByIdUseCase,
                getPositionFromFullAddressUseCase,
                getNearbyPointsOfInterestUseCase,
                application,
                navArgProducer
            )
        }
    }

    @Test
    fun `get real estate details with is sold as true`() = testCoroutineRule.runTest {
        // WHEN
        realEstateDetailsViewModel.realEstateDetailsViewStateLiveData.observeForTesting(this) {

            // THEN
            assertThat(it.value).isEqualTo(getDefaultRealEstateDetailsViewState())

            coVerify(exactly = 1) {
                navArgProducer.getNavArgs(RealEstateDetailsFragmentArgs::class).realEstateId
                getRealEstateFlowByIdUseCase.invoke(DEFAULT_ID)
                getNearbyPointsOfInterestUseCase.invoke(lat = DEFAULT_LAT, long = DEFAULT_LONG)
            }
            confirmVerified(
                upsertNewRealEstateUseCase,
                getRealEstateFlowByIdUseCase,
                getPositionFromFullAddressUseCase,
                getNearbyPointsOfInterestUseCase,
                application,
                navArgProducer
            )
        }
    }

    // TESTS INSERT

    @Test
    fun `try to insert a new real estate`() = testCoroutineRule.runTest {
        // GIVEN
        every { navArgProducer.getNavArgs(RealEstateDetailsFragmentArgs::class).realEstateId } returns DEFAULT_ID_TO_ADD
        every { getRealEstateFlowByIdUseCase.invoke(DEFAULT_ID_TO_ADD) } returns flowOf(null)
        coEvery { getNearbyPointsOfInterestUseCase.invoke(lat = DEFAULT_LAT, long = DEFAULT_LONG) } returns arrayListOf()
        every {
            application.getString(
                R.string.full_address, DEFAULT_GRID_ZONE, DEFAULT_STREET_NAME, DEFAULT_CITY, DEFAULT_STATE, DEFAULT_POSTAL_CODE
            )
        } returns DEFAULT_FULL_ADDRESS
        every { application.getString(R.string.real_estate_status_available) } returns DEFAULT_STATUS_AVAILABLE
        every { application.getString(R.string.new_real_estate_is_added) } returns ADD_SUCCESSFUL
        coEvery { getPositionFromFullAddressUseCase.invoke(DEFAULT_FULL_ADDRESS, application) } returns DEFAULT_POSITION

        coJustRun {
            upsertNewRealEstateUseCase.invoke(
                realEstate = any(), photos = getDefaultAggregatedPhotosToAdd()
            )
        }

        realEstateDetailsViewModel.onSaveRealEstateButtonClicked(
            type = getDefaultRealEstateDetailsViewStateToAdd().typePosition,
            price = getDefaultRealEstateDetailsViewStateToAdd().price,
            livingSpace = getDefaultRealEstateDetailsViewStateToAdd().livingSpace,
            numberRooms = getDefaultRealEstateDetailsViewStateToAdd().numberRooms,
            numberBedroom = getDefaultRealEstateDetailsViewStateToAdd().numberBedroom,
            numberBathroom = getDefaultRealEstateDetailsViewStateToAdd().numberBathroom,
            description = getDefaultRealEstateDetailsViewStateToAdd().description,
            postalCode = getDefaultRealEstateDetailsViewStateToAdd().postalCode,
            state = getDefaultRealEstateDetailsViewStateToAdd().state,
            city = getDefaultRealEstateDetailsViewStateToAdd().city,
            streetName = getDefaultRealEstateDetailsViewStateToAdd().streetName,
            gridZone = getDefaultRealEstateDetailsViewStateToAdd().gridZone
        )

        // WHEN
        realEstateDetailsViewModel.realEstateDetailsViewStateLiveData.observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(getDefaultEmptyRealEstateDetailsViewState())

            // Call 3 times, ask El Nino
            verify(atLeast = 1) {
                navArgProducer.getNavArgs(RealEstateDetailsFragmentArgs::class).realEstateId
                getRealEstateFlowByIdUseCase.invoke(DEFAULT_ID_TO_ADD)
                application.getString(
                    R.string.full_address, DEFAULT_GRID_ZONE, DEFAULT_STREET_NAME, DEFAULT_CITY, DEFAULT_STATE, DEFAULT_POSTAL_CODE
                )
                application.getString(R.string.real_estate_status_available)
                application.getString(R.string.new_real_estate_is_added)
            }
            coVerify {
                getPositionFromFullAddressUseCase.invoke(DEFAULT_FULL_ADDRESS, application)
                upsertNewRealEstateUseCase.invoke(
                    getDefaultRealEstateEntityToAdd(), getDefaultAggregatedPhotosToAdd()
                )
            }
            confirmVerified(
                upsertNewRealEstateUseCase,
                getRealEstateFlowByIdUseCase,
                getPositionFromFullAddressUseCase,
                getNearbyPointsOfInterestUseCase,
                application,
                navArgProducer
            )
        }
    }

    @Test
    fun `try to insert a real estate with empty fields with initial case`() = testCoroutineRule.runTest {
        // GIVEN
        every { navArgProducer.getNavArgs(RealEstateDetailsFragmentArgs::class).realEstateId } returns DEFAULT_ID
        every { getRealEstateFlowByIdUseCase.invoke(DEFAULT_ID) } returns flowOf(null)
        every { application.getString(R.string.field_empty_toast_msg) } returns FIELD_EMPTY_TOAST_MSG

        realEstateDetailsViewModel.onSaveRealEstateButtonClicked(
            type = getDefaultEmptyRealEstateDetailsViewState().typePosition,
            price = getDefaultEmptyRealEstateDetailsViewState().price,
            livingSpace = getDefaultEmptyRealEstateDetailsViewState().livingSpace,
            numberRooms = getDefaultEmptyRealEstateDetailsViewState().numberRooms,
            numberBedroom = getDefaultEmptyRealEstateDetailsViewState().numberBedroom,
            numberBathroom = getDefaultEmptyRealEstateDetailsViewState().numberBathroom,
            description = getDefaultEmptyRealEstateDetailsViewState().description,
            postalCode = getDefaultEmptyRealEstateDetailsViewState().postalCode,
            state = getDefaultEmptyRealEstateDetailsViewState().state,
            city = getDefaultEmptyRealEstateDetailsViewState().city,
            streetName = getDefaultEmptyRealEstateDetailsViewState().streetName,
            gridZone = getDefaultEmptyRealEstateDetailsViewState().gridZone
        )

        // WHEN
        realEstateDetailsViewModel.realEstateDetailsViewStateLiveData.observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(getDefaultEmptyRealEstateDetailsViewState())

            // Call 3 times, ask El Nino
            verify(atLeast = 1) {
                navArgProducer.getNavArgs(RealEstateDetailsFragmentArgs::class).realEstateId
                getRealEstateFlowByIdUseCase.invoke(DEFAULT_ID)
                application.getString(R.string.field_empty_toast_msg)
            }
            confirmVerified(
                upsertNewRealEstateUseCase,
                getRealEstateFlowByIdUseCase,
                getPositionFromFullAddressUseCase,
                getNearbyPointsOfInterestUseCase,
                application,
                navArgProducer
            )
        }
    }

    @Test
    fun `try to insert a real estate with empty fields with nominal case`() = testCoroutineRule.runTest {
        // GIVEN
        every { application.getString(R.string.field_empty_toast_msg) } returns FIELD_EMPTY_TOAST_MSG

        realEstateDetailsViewModel.onSaveRealEstateButtonClicked(
            type = getDefaultEmptyRealEstateDetailsViewState().typePosition,
            price = getDefaultEmptyRealEstateDetailsViewState().price,
            livingSpace = getDefaultEmptyRealEstateDetailsViewState().livingSpace,
            numberRooms = getDefaultEmptyRealEstateDetailsViewState().numberRooms,
            numberBedroom = getDefaultEmptyRealEstateDetailsViewState().numberBedroom,
            numberBathroom = getDefaultEmptyRealEstateDetailsViewState().numberBathroom,
            description = getDefaultEmptyRealEstateDetailsViewState().description,
            postalCode = getDefaultEmptyRealEstateDetailsViewState().postalCode,
            state = getDefaultEmptyRealEstateDetailsViewState().state,
            city = getDefaultEmptyRealEstateDetailsViewState().city,
            streetName = getDefaultEmptyRealEstateDetailsViewState().streetName,
            gridZone = getDefaultEmptyRealEstateDetailsViewState().gridZone
        )

        // WHEN
        realEstateDetailsViewModel.realEstateDetailsViewStateLiveData.observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(getDefaultRealEstateDetailsViewState())

            // Call 3 times, ask El Nino
            coVerify(atLeast = 1) {
                navArgProducer.getNavArgs(RealEstateDetailsFragmentArgs::class).realEstateId
                getRealEstateFlowByIdUseCase.invoke(DEFAULT_ID)
                getNearbyPointsOfInterestUseCase.invoke(lat = DEFAULT_LAT, long = DEFAULT_LONG)
                application.getString(R.string.field_empty_toast_msg)
            }
            confirmVerified(
                upsertNewRealEstateUseCase,
                getRealEstateFlowByIdUseCase,
                getPositionFromFullAddressUseCase,
                getNearbyPointsOfInterestUseCase,
                application,
                navArgProducer
            )
        }
    }

    @Test
    fun `try to insert a real estate with wrong data`() = testCoroutineRule.runTest {
        // GIVEN
        every { navArgProducer.getNavArgs(RealEstateDetailsFragmentArgs::class).realEstateId } returns DEFAULT_ID_TO_ADD
        every { getRealEstateFlowByIdUseCase.invoke(DEFAULT_ID_TO_ADD) } returns flowOf(null)
        coEvery { getNearbyPointsOfInterestUseCase.invoke(lat = DEFAULT_LAT, long = DEFAULT_LONG) } returns arrayListOf()
        every {
            application.getString(R.string.fields_are_not_digits)
        } returns FIELD_ARE_NOT_DIGITS_MSG

        realEstateDetailsViewModel.onSaveRealEstateButtonClicked(
            type = getDefaultRealEstateDetailsViewStateToAdd().typePosition,
            price = getDefaultRealEstateDetailsViewStateToAddWithWrongData().price,
            livingSpace = getDefaultRealEstateDetailsViewStateToAddWithWrongData().livingSpace,
            numberRooms = getDefaultRealEstateDetailsViewStateToAddWithWrongData().numberRooms,
            numberBedroom = getDefaultRealEstateDetailsViewStateToAddWithWrongData().numberBedroom,
            numberBathroom = getDefaultRealEstateDetailsViewStateToAddWithWrongData().numberBathroom,
            description = getDefaultRealEstateDetailsViewStateToAdd().description,
            postalCode = getDefaultRealEstateDetailsViewStateToAdd().postalCode,
            state = getDefaultRealEstateDetailsViewStateToAdd().state,
            city = getDefaultRealEstateDetailsViewStateToAdd().city,
            streetName = getDefaultRealEstateDetailsViewStateToAdd().streetName,
            gridZone = getDefaultRealEstateDetailsViewStateToAdd().gridZone
        )

        // WHEN
        realEstateDetailsViewModel.realEstateDetailsViewStateLiveData.observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(getDefaultEmptyRealEstateDetailsViewState())

            // Call 3 times, ask El Nino
            verify(atLeast = 1) {
                navArgProducer.getNavArgs(RealEstateDetailsFragmentArgs::class).realEstateId
                getRealEstateFlowByIdUseCase.invoke(DEFAULT_ID_TO_ADD)
                application.getString(R.string.fields_are_not_digits)
            }
            confirmVerified(
                upsertNewRealEstateUseCase,
                getRealEstateFlowByIdUseCase,
                getPositionFromFullAddressUseCase,
                getNearbyPointsOfInterestUseCase,
                application,
                navArgProducer
            )
        }
    }

    // TESTS UPDATE

    @Test
    fun `try to update an existing real estate`() = testCoroutineRule.runTest {
        // GIVEN
        every { navArgProducer.getNavArgs(RealEstateDetailsFragmentArgs::class).realEstateId } returns DEFAULT_ID
        every { getRealEstateFlowByIdUseCase.invoke(DEFAULT_ID) } returns flowOf(getDefaultRealEstateEntityWithPhotos())
        coEvery { getNearbyPointsOfInterestUseCase.invoke(lat = DEFAULT_LAT, long = DEFAULT_LONG) } returns arrayListOf()
        every {
            application.getString(
                R.string.full_address, DEFAULT_GRID_ZONE, DEFAULT_STREET_NAME, DEFAULT_CITY, DEFAULT_STATE, DEFAULT_POSTAL_CODE
            )
        } returns DEFAULT_FULL_ADDRESS
        every { application.getString(R.string.real_estate_status_available) } returns DEFAULT_STATUS_AVAILABLE
        coEvery { getPositionFromFullAddressUseCase.invoke(DEFAULT_FULL_ADDRESS, application) } returns DEFAULT_POSITION

        coJustRun {
            upsertNewRealEstateUseCase.invoke(
                realEstate = getDefaultRealEstateEntityToUpdate(), photos = getDefaultAggregatedPhotos()
            )
        }

        realEstateDetailsViewModel.onSaveRealEstateButtonClicked(
            type = getDefaultRealEstateDetailsViewStateToUpdate().typePosition,
            price = getDefaultRealEstateDetailsViewStateToUpdate().price,
            livingSpace = getDefaultRealEstateDetailsViewStateToUpdate().livingSpace,
            numberRooms = getDefaultRealEstateDetailsViewStateToUpdate().numberRooms,
            numberBedroom = getDefaultRealEstateDetailsViewStateToUpdate().numberBedroom,
            numberBathroom = getDefaultRealEstateDetailsViewStateToUpdate().numberBathroom,
            description = getDefaultRealEstateDetailsViewStateToUpdate().description,
            postalCode = getDefaultRealEstateDetailsViewStateToUpdate().postalCode,
            state = getDefaultRealEstateDetailsViewStateToUpdate().state,
            city = getDefaultRealEstateDetailsViewStateToUpdate().city,
            streetName = getDefaultRealEstateDetailsViewStateToUpdate().streetName,
            gridZone = getDefaultRealEstateDetailsViewStateToUpdate().gridZone
        )

        // WHEN
        realEstateDetailsViewModel.realEstateDetailsViewStateLiveData.observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(getDefaultRealEstateDetailsViewState())

            verify(atLeast = 1) {
                navArgProducer.getNavArgs(RealEstateDetailsFragmentArgs::class).realEstateId
                getRealEstateFlowByIdUseCase.invoke(DEFAULT_ID)
                application.getString(
                    R.string.full_address, DEFAULT_GRID_ZONE, DEFAULT_STREET_NAME, DEFAULT_CITY, DEFAULT_STATE, DEFAULT_POSTAL_CODE
                )
                application.getString(R.string.real_estate_status_available)
            }
            coVerify {
                getPositionFromFullAddressUseCase.invoke(DEFAULT_FULL_ADDRESS, application)
                getNearbyPointsOfInterestUseCase.invoke(lat = DEFAULT_LAT, long = DEFAULT_LONG)
                upsertNewRealEstateUseCase.invoke(
                    getDefaultRealEstateEntityToUpdate(), getDefaultAggregatedPhotos()
                )
            }
            confirmVerified(
                upsertNewRealEstateUseCase,
                getRealEstateFlowByIdUseCase,
                getPositionFromFullAddressUseCase,
                getNearbyPointsOfInterestUseCase,
                application,
                navArgProducer
            )
        }
    }

    // TESTS PHOTO

//    @Test
//    fun `try to add a new photo then update`() = testCoroutineRule.runTest {
//        // GIVEN
//        every { navArgProducer.getNavArgs(RealEstateDetailsFragmentArgs::class).realEstateId } returns DEFAULT_ID
//        every { getRealEstateFlowByIdUseCase.invoke(DEFAULT_ID) } returns flowOf(getDefaultRealEstateEntityWithPhotos())
//        coEvery { getNearbyPointsOfInterestUseCase.invoke(lat = DEFAULT_LAT, long = DEFAULT_LONG) } returns arrayListOf()
//        every {
//            application.getString(
//                R.string.full_address, DEFAULT_GRID_ZONE, DEFAULT_STREET_NAME, DEFAULT_CITY, DEFAULT_STATE, DEFAULT_POSTAL_CODE
//            )
//        } returns DEFAULT_FULL_ADDRESS
//        every { application.getString(R.string.real_estate_status_available) } returns DEFAULT_STATUS_AVAILABLE
//        coEvery { getPositionFromFullAddressUseCase.invoke(DEFAULT_FULL_ADDRESS, application) } returns DEFAULT_POSITION
//
//        realEstateDetailsViewModel.onNewPhotoAdded(firstMockUriPhoto)
//
//        coJustRun {
//            upsertNewRealEstateUseCase.invoke(
//                realEstate = getDefaultRealEstateEntityToUpdate(),
//                photos = getDefaultAggregatedPhotosToUpdate()
//            )
//        }
//
//        realEstateDetailsViewModel.onSaveRealEstateButtonClicked(
//            type = getDefaultRealEstateDetailsViewStateToUpdate().typePosition,
//            price = getDefaultRealEstateDetailsViewStateToUpdate().price,
//            livingSpace = getDefaultRealEstateDetailsViewStateToUpdate().livingSpace,
//            numberRooms = getDefaultRealEstateDetailsViewStateToUpdate().numberRooms,
//            numberBedroom = getDefaultRealEstateDetailsViewStateToUpdate().numberBedroom,
//            numberBathroom = getDefaultRealEstateDetailsViewStateToUpdate().numberBathroom,
//            description = getDefaultRealEstateDetailsViewStateToUpdate().description,
//            postalCode = getDefaultRealEstateDetailsViewStateToUpdate().postalCode,
//            state = getDefaultRealEstateDetailsViewStateToUpdate().state,
//            city = getDefaultRealEstateDetailsViewStateToUpdate().city,
//            streetName = getDefaultRealEstateDetailsViewStateToUpdate().streetName,
//            gridZone = getDefaultRealEstateDetailsViewStateToUpdate().gridZone
//        )
//
//        // WHEN
//        realEstateDetailsViewModel.realEstateDetailsViewStateLiveData.observeForTesting(this) {
//            // THEN
//            assertThat(it.value).isEqualTo(getDefaultRealEstateDetailsViewState())
//
//            verify(atLeast = 1) {
//                navArgProducer.getNavArgs(RealEstateDetailsFragmentArgs::class).realEstateId
//                getRealEstateFlowByIdUseCase.invoke(DEFAULT_ID)
//                application.getString(
//                    R.string.full_address, DEFAULT_GRID_ZONE, DEFAULT_STREET_NAME, DEFAULT_CITY, DEFAULT_STATE, DEFAULT_POSTAL_CODE
//                )
//                application.getString(R.string.real_estate_status_available)
//            }
//            coVerify {
//                getPositionFromFullAddressUseCase.invoke(DEFAULT_FULL_ADDRESS, application)
//                getNearbyPointsOfInterestUseCase.invoke(lat = DEFAULT_LAT, long = DEFAULT_LONG)
//                upsertNewRealEstateUseCase.invoke(
//                    getDefaultRealEstateEntityToUpdate(), getDefaultAggregatedPhotosToUpdate()
//                )
//            }
//            confirmVerified(
//                upsertNewRealEstateUseCase,
//                getRealEstateFlowByIdUseCase,
//                getPositionFromFullAddressUseCase,
//                getNearbyPointsOfInterestUseCase,
//                application,
//                navArgProducer
//            )
//        }
//    }
//
//    @Test
//    fun `try to delete a photo`() = testCoroutineRule.runTest {
//        realEstateDetailsViewModel.onPhotoDeleted(getDefaultRealEstateDetailsViewState().photos[2].uri)
//
////        "https://photos.zillowstatic.com/fp/9d28f752e5f90e54d151a41114db6040-se_extra_large_1500_800.webp"
//
//        // WHEN
//        realEstateDetailsViewModel.realEstateDetailsViewStateLiveData.observeForTesting(this) {
//
//            // THEN
//            assertThat(it.value).isEqualTo(getDefaultRealEstateDetailsViewStateAfterPhotoDeleted())
//
//            coVerify(exactly = 1) {
//                navArgProducer.getNavArgs(RealEstateDetailsFragmentArgs::class).realEstateId
//                getRealEstateFlowByIdUseCase.invoke(DEFAULT_ID)
//                getNearbyPointsOfInterestUseCase.invoke(lat = DEFAULT_LAT, long = DEFAULT_LONG)
//            }
//            confirmVerified(
//                upsertNewRealEstateUseCase,
//                getRealEstateFlowByIdUseCase,
//                getPositionFromFullAddressUseCase,
//                getNearbyPointsOfInterestUseCase,
//                application,
//                navArgProducer
//            )
//        }
//    }


    //region ================================================================ DETAILS ===============================================================

    private fun getDefaultRealEstateDetailsViewState(): RealEstateDetailsViewState {
        return RealEstateDetailsViewState(
            id = DEFAULT_ID,
            type = DEFAULT_TYPE,
            typePosition = DEFAULT_TYPE_POSITION,
            price = DEFAULT_PRICE_STRING,
            livingSpace = DEFAULT_LIVING_SPACE_STRING,
            numberRooms = DEFAULT_NUMBER_ROOM_STRING,
            numberBedroom = DEFAULT_NUMBER_BEDROOM_STRING,
            numberBathroom = DEFAULT_NUMBER_BATHROOM_STRING,
            description = DEFAULT_DESCRIPTION,
            photos = getDefaultPhotoViewStates(),
            city = DEFAULT_CITY,
            postalCode = DEFAULT_POSTAL_CODE,
            state = DEFAULT_STATE,
            streetName = DEFAULT_STREET_NAME,
            gridZone = DEFAULT_GRID_ZONE,
            latitude = DEFAULT_LAT,
            longitude = DEFAULT_LONG,
            noPhoto = false,
            entryDate = Instant.now(fixedClock),
            saleDate = null,
            isSold = false,
            pointsOfInterest = arrayListOf(),
        )
    }

    private fun getDefaultRealEstateDetailsViewStateAfterPhotoDeleted(): RealEstateDetailsViewState {
        return RealEstateDetailsViewState(
            id = DEFAULT_ID,
            type = DEFAULT_TYPE,
            typePosition = DEFAULT_TYPE_POSITION,
            price = DEFAULT_PRICE_STRING,
            livingSpace = DEFAULT_LIVING_SPACE_STRING,
            numberRooms = DEFAULT_NUMBER_ROOM_STRING,
            numberBedroom = DEFAULT_NUMBER_BEDROOM_STRING,
            numberBathroom = DEFAULT_NUMBER_BATHROOM_STRING,
            description = DEFAULT_DESCRIPTION,
            photos = getDefaultPhotoViewStatesAfterDeletedPhoto(),
            city = DEFAULT_CITY,
            postalCode = DEFAULT_POSTAL_CODE,
            state = DEFAULT_STATE,
            streetName = DEFAULT_STREET_NAME,
            gridZone = DEFAULT_GRID_ZONE,
            latitude = DEFAULT_LAT,
            longitude = DEFAULT_LONG,
            noPhoto = false,
            entryDate = Instant.now(fixedClock),
            saleDate = null,
            isSold = false,
            pointsOfInterest = arrayListOf(),
        )
    }

    private fun getDefaultRealEstateEntityWithPhotos(): RealEstateEntityWithPhotos {
        return RealEstateEntityWithPhotos(getDefaultRealEstateEntity(), getDefaultPhotoEntities())
    }

    private fun getDefaultRealEstateEntity(): RealEstateEntity {
        return RealEstateEntity(
            id = DEFAULT_ID,
            type = DEFAULT_TYPE,
            price = DEFAULT_PRICE,
            livingSpace = DEFAULT_LIVING_SPACE,
            numberRooms = DEFAULT_NUMBER_ROOM,
            numberBedroom = DEFAULT_NUMBER_BEDROOM,
            numberBathroom = DEFAULT_NUMBER_BATHROOM,
            description = DEFAULT_DESCRIPTION,
            postalCode = DEFAULT_POSTAL_CODE,
            state = DEFAULT_STATE,
            city = DEFAULT_CITY,
            streetName = DEFAULT_STREET_NAME,
            gridZone = DEFAULT_GRID_ZONE,
            status = DEFAULT_STATUS_AVAILABLE,
            entryDate = LocalDateTime.now(fixedClock),
            saleDate = null,
            latitude = DEFAULT_LAT,
            longitude = DEFAULT_LONG,
            agentInChargeId = AGENT_ID,
        )
    }

    private fun getDefaultPhotoEntities(): List<PhotoEntity> {
        return listOf(
            PhotoEntity(
                0, 1L, "https://photos.zillowstatic.com/fp/390793abc077faf2df87690ad3f9940c-se_extra_large_1500_800.webp"
            ), PhotoEntity(
                0, 1L, "https://photos.zillowstatic.com/fp/344beadccb742f876c027673bfccccf2-se_extra_large_1500_800.webp"
            ), PhotoEntity(
                0, 1L, "https://photos.zillowstatic.com/fp/9d28f752e5f90e54d151a41114db6040-se_extra_large_1500_800.webp"
            )
        )
    }

    private fun getDefaultPhotoViewStates(): List<DetailsPhotoViewState> {
        return listOf(
            DetailsPhotoViewState(uri = "https://photos.zillowstatic.com/fp/390793abc077faf2df87690ad3f9940c-se_extra_large_1500_800.webp",
                EquatableCallback {}),
            DetailsPhotoViewState(uri = "https://photos.zillowstatic.com/fp/344beadccb742f876c027673bfccccf2-se_extra_large_1500_800.webp",
                EquatableCallback {}),
            DetailsPhotoViewState(uri = "https://photos.zillowstatic.com/fp/9d28f752e5f90e54d151a41114db6040-se_extra_large_1500_800.webp",
                EquatableCallback {})
        )
    }

    private fun getDefaultPhotoViewStatesAfterDeletedPhoto(): List<DetailsPhotoViewState> {
        return listOf(
            DetailsPhotoViewState(uri = "https://photos.zillowstatic.com/fp/390793abc077faf2df87690ad3f9940c-se_extra_large_1500_800.webp",
                EquatableCallback {}),
            DetailsPhotoViewState(uri = "https://photos.zillowstatic.com/fp/344beadccb742f876c027673bfccccf2-se_extra_large_1500_800.webp",
                EquatableCallback {})
        )
    }

    private fun getDefaultAggregatedPhotos(): List<AggregatedPhoto> {
        return listOf(
            AggregatedPhoto(uri = "https://photos.zillowstatic.com/fp/390793abc077faf2df87690ad3f9940c-se_extra_large_1500_800.webp"),
            AggregatedPhoto(uri = "https://photos.zillowstatic.com/fp/344beadccb742f876c027673bfccccf2-se_extra_large_1500_800.webp"),
            AggregatedPhoto(uri = "https://photos.zillowstatic.com/fp/9d28f752e5f90e54d151a41114db6040-se_extra_large_1500_800.webp")
        )
    }

    //endregion

    //region ================================================================= EMPTY ================================================================

    private fun getDefaultEmptyRealEstateEntity(): RealEstateEntity {
        return RealEstateEntity(
            id = DEFAULT_ID_TO_ADD,
            type = DEFAULT_EMPTY_TYPE,
            price = DEFAULT_EMPTY_PRICE,
            livingSpace = DEFAULT_EMPTY_LIVING_SPACE,
            numberRooms = DEFAULT_EMPTY_ROOMS,
            numberBedroom = DEFAULT_EMPTY_BEDROOMS,
            numberBathroom = DEFAULT_EMPTY_BATHROOMS,
            description = DEFAULT_EMPTY_DESCRIPTION,
            postalCode = DEFAULT_EMPTY_POSTAL_CODE,
            state = DEFAULT_EMPTY_STATE,
            city = DEFAULT_EMPTY_CITY,
            streetName = DEFAULT_EMPTY_STREET_NAME,
            gridZone = DEFAULT_EMPTY_GRID_ZONE,
            status = DEFAULT_STATUS_AVAILABLE,
            entryDate = LocalDateTime.now(fixedClock),
            saleDate = null,
            latitude = DEFAULT_EMPTY_LATITUDE,
            longitude = DEFAULT_EMPTY_LONGITUDE,
            agentInChargeId = 1L,
        )
    }

    private fun getDefaultEmptyRealEstateDetailsViewState(): RealEstateDetailsViewState {
        return RealEstateDetailsViewState(
            id = getDefaultEmptyRealEstateEntity().id,
            type = getDefaultEmptyRealEstateEntity().type,
            typePosition = 0,
            price = "0.0",
            livingSpace = "0.0",
            numberRooms = "0",
            numberBedroom = "0",
            numberBathroom = "0",
            description = "",
            arrayListOf(),
            city = "",
            postalCode = "",
            state = "",
            streetName = "",
            gridZone = "",
            latitude = 0.0,
            longitude = 0.0,
            noPhoto = true,
            entryDate = null,
            saleDate = null,
            isSold = false,
            pointsOfInterest = emptyList(),
        )
    }

    //endregion

    //region ================================================================= TO ADD ===============================================================

    private fun getDefaultRealEstateEntityToAdd(): RealEstateEntity {
        return RealEstateEntity(
            id = DEFAULT_ID_TO_ADD,
            type = DEFAULT_TYPE,
            price = DEFAULT_PRICE,
            livingSpace = DEFAULT_LIVING_SPACE,
            numberRooms = DEFAULT_NUMBER_ROOM,
            numberBedroom = DEFAULT_NUMBER_BEDROOM,
            numberBathroom = DEFAULT_NUMBER_BATHROOM,
            description = DEFAULT_DESCRIPTION,
            postalCode = DEFAULT_POSTAL_CODE,
            state = DEFAULT_STATE,
            city = DEFAULT_CITY,
            streetName = DEFAULT_STREET_NAME,
            gridZone = DEFAULT_GRID_ZONE,
            status = DEFAULT_STATUS_AVAILABLE,
            entryDate = LocalDateTime.now(fixedClock),
            saleDate = null,
            latitude = DEFAULT_LAT,
            longitude = DEFAULT_LONG,
            agentInChargeId = AGENT_ID,
        )
    }

    private fun getDefaultRealEstateDetailsViewStateToAdd(): RealEstateDetailsViewState {
        return RealEstateDetailsViewState(
            id = DEFAULT_ID_TO_ADD,
            type = DEFAULT_TYPE,
            typePosition = DEFAULT_TYPE_POSITION,
            price = DEFAULT_PRICE_STRING_TO_ADDED,
            livingSpace = DEFAULT_LIVING_SPACE_STRING,
            numberRooms = DEFAULT_NUMBER_ROOM_STRING,
            numberBedroom = DEFAULT_NUMBER_BEDROOM_STRING,
            numberBathroom = DEFAULT_NUMBER_BATHROOM_STRING,
            description = DEFAULT_DESCRIPTION,
            photos = getDefaultPhotoViewStatesToAdd(),
            city = DEFAULT_CITY,
            postalCode = DEFAULT_POSTAL_CODE,
            state = DEFAULT_STATE,
            streetName = DEFAULT_STREET_NAME,
            gridZone = DEFAULT_GRID_ZONE,
            latitude = DEFAULT_LAT,
            longitude = DEFAULT_LONG,
            noPhoto = false,
            entryDate = Instant.now(fixedClock),
            saleDate = null,
            isSold = false,
            pointsOfInterest = arrayListOf(),
        )
    }

    private fun getDefaultPhotoViewStatesToAdd(): List<DetailsPhotoViewState> {
        return getDefaultAggregatedPhotosToAdd().map {
            DetailsPhotoViewState(it.uri, EquatableCallback {})
        }
    }

    private fun getDefaultAggregatedPhotosToAdd(): List<AggregatedPhoto> {
        return listOf()
    }

    private fun getDefaultRealEstateDetailsViewStateToAddWithWrongData(): RealEstateDetailsViewState {
        return RealEstateDetailsViewState(
            id = DEFAULT_ID_TO_ADD,
            type = DEFAULT_TYPE,
            typePosition = DEFAULT_TYPE_POSITION,
            price = WRONG_PRICE_STRING,
            livingSpace = WRONG_LIVING_SPACE_STRING,
            numberRooms = WRONG_NUMBER_ROOM_STRING,
            numberBedroom = WRONG_NUMBER_BEDROOM_STRING,
            numberBathroom = WRONG_NUMBER_BATHROOM_STRING,
            description = DEFAULT_DESCRIPTION,
            photos = getDefaultPhotoViewStatesToAdd(),
            city = DEFAULT_CITY,
            postalCode = DEFAULT_POSTAL_CODE,
            state = DEFAULT_STATE,
            streetName = DEFAULT_STREET_NAME,
            gridZone = DEFAULT_GRID_ZONE,
            latitude = DEFAULT_LAT,
            longitude = DEFAULT_LONG,
            noPhoto = false,
            entryDate = Instant.now(fixedClock),
            saleDate = null,
            isSold = false,
            pointsOfInterest = arrayListOf(),
        )
    }

    //endregion

    //region ================================================================= TO UPDATE =================================================================

    private fun getDefaultRealEstateEntityToUpdate(): RealEstateEntity {
        return RealEstateEntity(
            id = DEFAULT_ID,
            type = NEW_TYPE,
            price = NEW_PRICE,
            livingSpace = NEW_LIVING_SPACE,
            numberRooms = NEW_NUMBER_ROOM,
            numberBedroom = NEW_NUMBER_BEDROOM,
            numberBathroom = NEW_NUMBER_BATHROOM,
            description = NEW_DESCRIPTION,
            postalCode = DEFAULT_POSTAL_CODE,
            state = DEFAULT_STATE,
            city = DEFAULT_CITY,
            streetName = DEFAULT_STREET_NAME,
            gridZone = DEFAULT_GRID_ZONE,
            status = DEFAULT_STATUS_AVAILABLE,
            entryDate = LocalDateTime.now(fixedClock),
            saleDate = null,
            latitude = DEFAULT_LAT,
            longitude = DEFAULT_LONG,
            agentInChargeId = AGENT_ID,
        )
    }

    private fun getDefaultRealEstateDetailsViewStateToUpdate(): RealEstateDetailsViewState {
        return RealEstateDetailsViewState(
            id = DEFAULT_ID,
            type = getDefaultRealEstateEntityToUpdate().type,
            typePosition = realEstateTypeToSpinnerPosition(getDefaultRealEstateEntityToUpdate().type),
            price = NEW_PRICE_STRING_TO_UPDATE,
            livingSpace = NEW_LIVING_SPACE_STRING,
            numberRooms = NEW_NUMBER_ROOM_STRING,
            numberBedroom = NEW_NUMBER_BEDROOM_STRING,
            numberBathroom = NEW_NUMBER_BATHROOM_STRING,
            description = NEW_DESCRIPTION,
            photos = getDefaultPhotoViewStatesToAdd(),
            city = DEFAULT_CITY,
            postalCode = DEFAULT_POSTAL_CODE,
            state = DEFAULT_STATE,
            streetName = DEFAULT_STREET_NAME,
            gridZone = DEFAULT_GRID_ZONE,
            latitude = DEFAULT_LAT,
            longitude = DEFAULT_LONG,
            noPhoto = false,
            entryDate = Instant.now(fixedClock),
            saleDate = null,
            isSold = false,
            pointsOfInterest = arrayListOf(),
        )
    }

    private fun getDefaultPhotoViewStatesToUpdate(): List<DetailsPhotoViewState> {
        return getDefaultAggregatedPhotosToUpdate().map {
            DetailsPhotoViewState(it.uri, EquatableCallback {})
        }
    }

    private fun getDefaultAggregatedPhotosToUpdate(): List<AggregatedPhoto> {
        return listOf(
            AggregatedPhoto(uri = "https://photos.zillowstatic.com/fp/390793abc077faf2df87690ad3f9940c-se_extra_large_1500_800.webp"),
            AggregatedPhoto(uri = "https://photos.zillowstatic.com/fp/344beadccb742f876c027673bfccccf2-se_extra_large_1500_800.webp"),
            AggregatedPhoto(uri = "https://photos.zillowstatic.com/fp/9d28f752e5f90e54d151a41114db6040-se_extra_large_1500_800.webp"),
            AggregatedPhoto(uri = firstMockUriPhoto.toString())
        )
    }

    //endregion
}