package com.suonk.oc_project9.ui.real_estates.details

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.suonk.oc_project9.R
import com.suonk.oc_project9.domain.places.GetNearbyPointsOfInterestUseCase
import com.suonk.oc_project9.domain.real_estate.GetPositionFromFullAddressUseCase
import com.suonk.oc_project9.domain.real_estate.GetRealEstateFlowByIdUseCase
import com.suonk.oc_project9.domain.real_estate.UpsertNewRealEstateUseCase
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
import java.math.BigDecimal
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
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

        const val ADD_SUCCESSFULL = "A Real Estate was successfully added"

        const val DEFAULT_STATUS_AVAILABLE = "Available"
        const val DEFAULT_STATUS_NOT_AVAILABLE = "Not Available"
        const val DEFAULT_DESCRIPTION = "Anchored by a vast marble gallery with sweeping staircase, "

        const val DEFAULT_LAT = 40.744081
        const val DEFAULT_LONG = -73.991302
        private val DEFAULT_POSITION = Position(lat = DEFAULT_LAT, long = DEFAULT_LONG)

        const val DEFAULT_TYPE = "Penthouse"
        val DEFAULT_TYPE_POSITION = realEstateTypeToSpinnerPosition(DEFAULT_TYPE)

        private const val NEW_TYPE = "Duplex"
        private val NEW_TYPE_POSITION = realEstateTypeToSpinnerPosition(NEW_TYPE)

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
        const val DEFAULT_TIMESTAMP_LONG = 1000000000L // 09/09/2001 - 01:46:40

        private const val DEFAULT_ID_TO_ADD = 0L
        private const val DEFAULT_EMPTY_TYPE_POSITION = 0
        private val DEFAULT_EMPTY_TYPE = spinnerPositionToType(DEFAULT_EMPTY_TYPE_POSITION)

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

        private const val FIELD_EMPTY_TOAST_MSG = "Fields can not be empty"

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
    private val context: Context = mockk()
    private val navArgProducer: NavArgProducer = mockk()

    private val fixedClock = Clock.fixed(Instant.EPOCH.plusMillis(DEFAULT_TIMESTAMP_LONG), ZoneId.systemDefault())

    private val realEstateDetailsViewModel = RealEstateDetailsViewModel(
        upsertNewRealEstateUseCase = upsertNewRealEstateUseCase,
        getRealEstateFlowByIdUseCase = getRealEstateFlowByIdUseCase,
        getPositionFromFullAddressUseCase = getPositionFromFullAddressUseCase,
        getNearbyPointsOfInterestUseCase = getNearbyPointsOfInterestUseCase,
        coroutineDispatcherProvider = testCoroutineRule.getTestCoroutineDispatcherProvider(),
        context = context,
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
                context,
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
                context,
                navArgProducer
            )
        }
    }

    @Test
    fun `try to insert a new real estate`() = testCoroutineRule.runTest {
        // GIVEN
        every { navArgProducer.getNavArgs(RealEstateDetailsFragmentArgs::class).realEstateId } returns DEFAULT_ID_TO_ADD
        every { getRealEstateFlowByIdUseCase.invoke(DEFAULT_ID_TO_ADD) } returns flowOf(null)
        coEvery { getNearbyPointsOfInterestUseCase.invoke(lat = DEFAULT_LAT, long = DEFAULT_LONG) } returns arrayListOf()
        every {
            context.getString(
                R.string.full_address, DEFAULT_GRID_ZONE, DEFAULT_STREET_NAME, DEFAULT_CITY, DEFAULT_STATE, DEFAULT_POSTAL_CODE
            )
        } returns DEFAULT_FULL_ADDRESS
        every { context.getString(R.string.real_estate_status_available) } returns DEFAULT_STATUS_AVAILABLE
        every { context.getString(R.string.new_real_estate_is_added) } returns ADD_SUCCESSFULL
        coEvery { getPositionFromFullAddressUseCase.invoke(DEFAULT_FULL_ADDRESS, context) } returns DEFAULT_POSITION

        coJustRun {
            upsertNewRealEstateUseCase.invoke(
                realEstate = getDefaultRealEstateEntityToAdd(),
                photos = getDefaultAggregatedPhotosToAdd()
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
                context.getString(
                    R.string.full_address, DEFAULT_GRID_ZONE, DEFAULT_STREET_NAME, DEFAULT_CITY, DEFAULT_STATE, DEFAULT_POSTAL_CODE
                )
                context.getString(R.string.real_estate_status_available)
                context.getString(R.string.new_real_estate_is_added)
            }
            coVerify {
                getNearbyPointsOfInterestUseCase.invoke(lat = DEFAULT_LAT, long = DEFAULT_LONG)
                getPositionFromFullAddressUseCase.invoke(DEFAULT_FULL_ADDRESS, context)
                upsertNewRealEstateUseCase.invoke(
                    getDefaultRealEstateEntityToAdd(), getDefaultAggregatedPhotosToAdd()
                )
            }
            confirmVerified(
                upsertNewRealEstateUseCase,
                getRealEstateFlowByIdUseCase,
                getPositionFromFullAddressUseCase,
                getNearbyPointsOfInterestUseCase,
                context,
                navArgProducer
            )
        }
    }

    @Test
    fun `try to insert a real estate with empty fields`() = testCoroutineRule.runTest {
        // GIVEN
        every {
            context.getString(R.string.field_empty_toast_msg)
        } returns FIELD_EMPTY_TOAST_MSG
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
            verify(atLeast = 1) {
                navArgProducer.getNavArgs(RealEstateDetailsFragmentArgs::class).realEstateId
                getRealEstateFlowByIdUseCase.invoke(DEFAULT_ID)
                context.getString(R.string.field_empty_toast_msg)
            }
            confirmVerified(
                upsertNewRealEstateUseCase, getRealEstateFlowByIdUseCase, context, navArgProducer
            )
        }
    }

    @Test
    fun `try to update a real estate`() = testCoroutineRule.runTest {
        // GIVEN
        every {
            context.getString(
                R.string.full_address, DEFAULT_GRID_ZONE, DEFAULT_STREET_NAME, DEFAULT_CITY, DEFAULT_STATE, DEFAULT_POSTAL_CODE
            )
        } returns DEFAULT_FULL_ADDRESS
        every {
            context.getString(R.string.real_estate_status_available)
        } returns DEFAULT_STATUS_AVAILABLE
        coEvery { getPositionFromFullAddressUseCase.invoke(DEFAULT_FULL_ADDRESS, context) } returns DEFAULT_POSITION

        coEvery { upsertNewRealEstateUseCase.invoke(getDefaultRealEstateEntityToAdd(), getDefaultAggregatedPhotosToAdd()) } returns Unit

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
            assertThat(it.value).isEqualTo(getDefaultRealEstateDetailsViewState())

            // Call 3 times, ask El Nino
            verify(atLeast = 1) {
                navArgProducer.getNavArgs(RealEstateDetailsFragmentArgs::class).realEstateId
                getRealEstateFlowByIdUseCase.invoke(DEFAULT_ID)
                context.getString(
                    R.string.full_address, DEFAULT_GRID_ZONE, DEFAULT_STREET_NAME, DEFAULT_CITY, DEFAULT_STATE, DEFAULT_POSTAL_CODE
                )
            }
            coVerify {
                getNearbyPointsOfInterestUseCase.invoke(lat = DEFAULT_LAT, long = DEFAULT_LONG)
                getPositionFromFullAddressUseCase.invoke(DEFAULT_FULL_ADDRESS, context)
                upsertNewRealEstateUseCase.invoke(
                    getDefaultRealEstateEntityToAdd(), getDefaultAggregatedPhotosToAdd()
                )
                context.getString(R.string.real_estate_status_available)
            }
            confirmVerified(
                upsertNewRealEstateUseCase,
                getRealEstateFlowByIdUseCase,
                getPositionFromFullAddressUseCase,
                getNearbyPointsOfInterestUseCase,
                context,
                navArgProducer
            )
        }
    }

    //region ================================================================ DETAILS ===============================================================

    private fun getAllDefaultRealEstatesWithPhotos(): List<RealEstateEntityWithPhotos> {
        return arrayListOf(
            getDefaultRealEstateEntityWithPhotos(), RealEstateEntityWithPhotos(
                RealEstateEntity(
                    id = 1L,
                    type = "Penthouse",
                    price = BigDecimal(29872000.0),
                    livingSpace = 8072.900,
                    numberRooms = 8,
                    numberBedroom = 4,
                    numberBathroom = 2,
                    description = "Anchored by a vast marble gallery with sweeping staircase, ",
                    postalCode = "10010",
                    state = "NY",
                    city = "New York",
                    streetName = "25th Street",
                    gridZone = "55 West",
                    status = "Available",
                    entryDate = LocalDateTime.now(),
                    saleDate = null,
                    latitude = 40.744080,
                    longitude = -73.991302,
                    agentInChargeId = 1L,
                ), arrayListOf(
                    PhotoEntity(
                        id = 0,
                        realEstateId = 2L,
                        "https://photos.zillowstatic.com/fp/cfef7cd3d01074fe7ac38e6fdfd0c657-se_extra_large_1500_800.webp"
                    ), PhotoEntity(
                        id = 0,
                        realEstateId = 2L,
                        "https://photos.zillowstatic.com/fp/f6def35d74b83ffa18e93d5bd56c6390-se_extra_large_1500_800.webp"
                    )
                )
            ), RealEstateEntityWithPhotos(
                RealEstateEntity(
                    3L,
                    "Duplex",
                    BigDecimal(15995000.0),
                    11756.9652,
                    11,
                    3,
                    3,
                    "",
                    "11357",
                    "NY",
                    "Whitestone",
                    "25th Ave",
                    "156-0-156-34",
                    "Available",
                    LocalDateTime.now(),
                    null,
                    40.775070,
                    -73.806640,
                    2L
                ), arrayListOf(
                    PhotoEntity(
                        0, 3L, "https://photonet.hotpads.com/search/listingPhoto/Postlets/1efkb7qsyr3d4/0002_1812003620_medium.jpg"
                    )
                )
            )
        )
    }

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
            arrayListOf(
                DetailsPhotoViewState(
                    "https://photos.zillowstatic.com/fp/390793abc077faf2df87690ad3f9940c-se_extra_large_1500_800.webp",
                    EquatableCallback {}),
                DetailsPhotoViewState("https://photos.zillowstatic.com/fp/344beadccb742f876c027673bfccccf2-se_extra_large_1500_800.webp",
                    EquatableCallback {}),
                DetailsPhotoViewState("https://photos.zillowstatic.com/fp/9d28f752e5f90e54d151a41114db6040-se_extra_large_1500_800.webp",
                    EquatableCallback {})
            ),
            city = DEFAULT_CITY,
            postalCode = DEFAULT_POSTAL_CODE,
            state = DEFAULT_STATE,
            streetName = DEFAULT_STREET_NAME,
            gridZone = DEFAULT_GRID_ZONE,
            latitude = DEFAULT_LAT,
            longitude = DEFAULT_LONG,
            noPhoto = false,
            entryDate = LocalDateTime.now(fixedClock).toEpochSecond(ZoneOffset.UTC),
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

    private fun getDefaultAggregatedPhotos(): List<RealEstateDetailsViewModel.AggregatedPhoto> {
        return listOf(
            RealEstateDetailsViewModel.AggregatedPhoto(
                uri = "https://photos.zillowstatic.com/fp/390793abc077faf2df87690ad3f9940c-se_extra_large_1500_800.webp"

            ), RealEstateDetailsViewModel.AggregatedPhoto(
                uri = "https://photos.zillowstatic.com/fp/344beadccb742f876c027673bfccccf2-se_extra_large_1500_800.webp"

            ), RealEstateDetailsViewModel.AggregatedPhoto(
                uri = "https://photos.zillowstatic.com/fp/9d28f752e5f90e54d151a41114db6040-se_extra_large_1500_800.webp"
            )
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
            entryDate = 0L,
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
            entryDate = LocalDateTime.now(fixedClock).toEpochSecond(ZoneOffset.UTC),
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

    private fun getDefaultAggregatedPhotosToAdd(): List<RealEstateDetailsViewModel.AggregatedPhoto> {
        return listOf()
    }

    //endregion
}