package com.suonk.oc_project9.ui.real_estates.list

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.suonk.oc_project9.R
import com.suonk.oc_project9.domain.SearchRepository
import com.suonk.oc_project9.domain.real_estate.get.GetAllRealEstatesUseCase
import com.suonk.oc_project9.model.database.data.entities.real_estate.PhotoEntity
import com.suonk.oc_project9.model.database.data.entities.real_estate.RealEstateEntity
import com.suonk.oc_project9.model.database.data.entities.real_estate.RealEstateEntityWithPhotos
import com.suonk.oc_project9.ui.filter.Filter
import com.suonk.oc_project9.utils.*
import io.mockk.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runCurrent
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class RealEstatesListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val getAllRealEstatesUseCase: GetAllRealEstatesUseCase = mockk()
    private val searchRepository: SearchRepository = mockk()
    private val context: Context = mockk()

    private val realEstatesListViewModel = RealEstatesListViewModel(
        getAllRealEstatesUseCase = getAllRealEstatesUseCase,
        searchRepository = searchRepository,
        coroutineDispatcherProvider = testCoroutineRule.getTestCoroutineDispatcherProvider(),
        context = context
    )

    @Before
    fun setup() {
        every { getAllRealEstatesUseCase.invoke() } returns flowOf(getAllDefaultRealEstatesWithPhotos())
        every { searchRepository.getCurrentFilterParametersFlow() } returns emptyListOfFiltersFlow.asStateFlow()

        every {
            context.getString(R.string.real_estate_price, FIRST_DEFAULT_PRICE)
        } returns FIRST_DEFAULT_PRICE_STRING
        every {
            context.getString(R.string.real_estate_price, SECOND_DEFAULT_PRICE)
        } returns SECOND_DEFAULT_PRICE_STRING

        every {
            context.getString(R.string.number_rooms, FIRST_DEFAULT_NUMBER_ROOM, FIRST_DEFAULT_NUMBER_BEDROOM, FIRST_DEFAULT_NUMBER_BATHROOM)
        } returns FIRST_DEFAULT_NUMBER_ALL_ROOMS_STRING
        every {
            context.getString(
                R.string.number_rooms, SECOND_DEFAULT_NUMBER_ROOM, SECOND_DEFAULT_NUMBER_BEDROOM, SECOND_DEFAULT_NUMBER_BATHROOM
            )
        } returns SECOND_DEFAULT_NUMBER_ALL_ROOMS_STRING

        every {
            context.getString(R.string.square_meter, FIRST_DEFAULT_LIVING_SPACE)
        } returns FIRST_DEFAULT_LIVING_SPACE_STRING
        every {
            context.getString(R.string.square_meter, SECOND_DEFAULT_LIVING_SPACE)
        } returns SECOND_DEFAULT_LIVING_SPACE_STRING

        every {
            context.getString(
                R.string.full_address,
                FIRST_DEFAULT_GRID_ZONE,
                FIRST_DEFAULT_STREET_NAME,
                FIRST_DEFAULT_CITY,
                FIRST_DEFAULT_STATE,
                FIRST_DEFAULT_POSTAL_CODE
            )
        } returns FIRST_DEFAULT_FULL_ADDRESS
        every {
            context.getString(
                R.string.full_address,
                SECOND_DEFAULT_GRID_ZONE,
                SECOND_DEFAULT_STREET_NAME,
                SECOND_DEFAULT_CITY,
                SECOND_DEFAULT_STATE,
                SECOND_DEFAULT_POSTAL_CODE
            )
        } returns SECOND_DEFAULT_FULL_ADDRESS
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // WHEN
        realEstatesListViewModel.realEstateLiveData.observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(getAllDefaultRealEstatesListViewStates())

            verify {
                getAllRealEstatesUseCase.invoke()
                searchRepository.getCurrentFilterParametersFlow()

                context.getString(R.string.real_estate_price, FIRST_DEFAULT_PRICE)
                context.getString(R.string.real_estate_price, SECOND_DEFAULT_PRICE)
                context.getString(
                    R.string.number_rooms, FIRST_DEFAULT_NUMBER_ROOM, FIRST_DEFAULT_NUMBER_BEDROOM, FIRST_DEFAULT_NUMBER_BATHROOM
                )
                context.getString(
                    R.string.number_rooms, SECOND_DEFAULT_NUMBER_ROOM, SECOND_DEFAULT_NUMBER_BEDROOM, SECOND_DEFAULT_NUMBER_BATHROOM
                )

                context.getString(R.string.square_meter, FIRST_DEFAULT_LIVING_SPACE)
                context.getString(R.string.square_meter, SECOND_DEFAULT_LIVING_SPACE)

                context.getString(
                    R.string.full_address,
                    FIRST_DEFAULT_GRID_ZONE,
                    FIRST_DEFAULT_STREET_NAME,
                    FIRST_DEFAULT_CITY,
                    FIRST_DEFAULT_STATE,
                    FIRST_DEFAULT_POSTAL_CODE
                )
                context.getString(
                    R.string.full_address,
                    SECOND_DEFAULT_GRID_ZONE,
                    SECOND_DEFAULT_STREET_NAME,
                    SECOND_DEFAULT_CITY,
                    SECOND_DEFAULT_STATE,
                    SECOND_DEFAULT_POSTAL_CODE
                )
            }
            confirmVerified(
                getAllRealEstatesUseCase, searchRepository, testCoroutineRule.getTestCoroutineDispatcherProvider(), context
            )
        }
    }

    @Test
    fun `initial case`() = testCoroutineRule.runTest {
        // GIVEN
        every { getAllRealEstatesUseCase.invoke() } returns flowOf(emptyList())

        // WHEN
        realEstatesListViewModel.realEstateLiveData.observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(getEmptyDefaultRealEstatesListViewStates())

            verify {
                getAllRealEstatesUseCase.invoke()
                searchRepository.getCurrentFilterParametersFlow()
            }
            confirmVerified(
                getAllRealEstatesUseCase, searchRepository, testCoroutineRule.getTestCoroutineDispatcherProvider(), context
            )
        }
    }

    @Test
    fun `get all real estates with filter by type penthouse`() = testCoroutineRule.runTest {
        // GIVEN
        realEstatesListViewModel.onSortedOrFilterClicked(R.id.penthouse_filter)

        // WHEN
        realEstatesListViewModel.realEstateLiveData.observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(getDefaultRealEstatesListViewStatesFilterByPenthouse())

            verify {
                getAllRealEstatesUseCase.invoke()
                searchRepository.getCurrentFilterParametersFlow()

                context.getString(R.string.real_estate_price, FIRST_DEFAULT_PRICE)
                context.getString(R.string.real_estate_price, SECOND_DEFAULT_PRICE)
                context.getString(
                    R.string.number_rooms, FIRST_DEFAULT_NUMBER_ROOM, FIRST_DEFAULT_NUMBER_BEDROOM, FIRST_DEFAULT_NUMBER_BATHROOM
                )
                context.getString(
                    R.string.number_rooms, SECOND_DEFAULT_NUMBER_ROOM, SECOND_DEFAULT_NUMBER_BEDROOM, SECOND_DEFAULT_NUMBER_BATHROOM
                )

                context.getString(R.string.square_meter, FIRST_DEFAULT_LIVING_SPACE)
                context.getString(R.string.square_meter, SECOND_DEFAULT_LIVING_SPACE)

                context.getString(
                    R.string.full_address,
                    FIRST_DEFAULT_GRID_ZONE,
                    FIRST_DEFAULT_STREET_NAME,
                    FIRST_DEFAULT_CITY,
                    FIRST_DEFAULT_STATE,
                    FIRST_DEFAULT_POSTAL_CODE
                )
                context.getString(
                    R.string.full_address,
                    SECOND_DEFAULT_GRID_ZONE,
                    SECOND_DEFAULT_STREET_NAME,
                    SECOND_DEFAULT_CITY,
                    SECOND_DEFAULT_STATE,
                    SECOND_DEFAULT_POSTAL_CODE
                )
            }
            confirmVerified(
                getAllRealEstatesUseCase, searchRepository, testCoroutineRule.getTestCoroutineDispatcherProvider(), context
            )
        }
    }

    @Test
    fun `get all real estates with filter by type penthouse and then remove filter`() = testCoroutineRule.runTest {
        // GIVEN
        justRun { searchRepository.reset() }
        realEstatesListViewModel.onSortedOrFilterClicked(R.id.penthouse_filter)

        // WHEN
        realEstatesListViewModel.realEstateLiveData.observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(getDefaultRealEstatesListViewStatesFilterByPenthouse())

            // GIVEN II
            realEstatesListViewModel.onSortedOrFilterClicked(R.id.remove_filter)
            runCurrent()

            // THEN II
            assertThat(it.value).isEqualTo(getAllDefaultRealEstatesListViewStates())

            verify {
                getAllRealEstatesUseCase.invoke()
                searchRepository.getCurrentFilterParametersFlow()

                context.getString(R.string.real_estate_price, FIRST_DEFAULT_PRICE)
                context.getString(R.string.real_estate_price, SECOND_DEFAULT_PRICE)
                context.getString(
                    R.string.number_rooms, FIRST_DEFAULT_NUMBER_ROOM, FIRST_DEFAULT_NUMBER_BEDROOM, FIRST_DEFAULT_NUMBER_BATHROOM
                )
                context.getString(
                    R.string.number_rooms, SECOND_DEFAULT_NUMBER_ROOM, SECOND_DEFAULT_NUMBER_BEDROOM, SECOND_DEFAULT_NUMBER_BATHROOM
                )

                context.getString(R.string.square_meter, FIRST_DEFAULT_LIVING_SPACE)
                context.getString(R.string.square_meter, SECOND_DEFAULT_LIVING_SPACE)

                context.getString(
                    R.string.full_address,
                    FIRST_DEFAULT_GRID_ZONE,
                    FIRST_DEFAULT_STREET_NAME,
                    FIRST_DEFAULT_CITY,
                    FIRST_DEFAULT_STATE,
                    FIRST_DEFAULT_POSTAL_CODE
                )
                context.getString(
                    R.string.full_address,
                    SECOND_DEFAULT_GRID_ZONE,
                    SECOND_DEFAULT_STREET_NAME,
                    SECOND_DEFAULT_CITY,
                    SECOND_DEFAULT_STATE,
                    SECOND_DEFAULT_POSTAL_CODE
                )
                searchRepository.reset()
            }
            confirmVerified(
                getAllRealEstatesUseCase, searchRepository, testCoroutineRule.getTestCoroutineDispatcherProvider(), context
            )
        }
    }

    @Test
    fun `get all real estates with filter by type loft`() = testCoroutineRule.runTest {
        // GIVEN
        realEstatesListViewModel.onSortedOrFilterClicked(R.id.loft_filter)

        // WHEN
        realEstatesListViewModel.realEstateLiveData.observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(getEmptyDefaultRealEstatesListViewStates())

            verify {
                getAllRealEstatesUseCase.invoke()
                searchRepository.getCurrentFilterParametersFlow()

                context.getString(R.string.real_estate_price, FIRST_DEFAULT_PRICE)
                context.getString(R.string.real_estate_price, SECOND_DEFAULT_PRICE)
                context.getString(
                    R.string.number_rooms, FIRST_DEFAULT_NUMBER_ROOM, FIRST_DEFAULT_NUMBER_BEDROOM, FIRST_DEFAULT_NUMBER_BATHROOM
                )
                context.getString(
                    R.string.number_rooms, SECOND_DEFAULT_NUMBER_ROOM, SECOND_DEFAULT_NUMBER_BEDROOM, SECOND_DEFAULT_NUMBER_BATHROOM
                )

                context.getString(R.string.square_meter, FIRST_DEFAULT_LIVING_SPACE)
                context.getString(R.string.square_meter, SECOND_DEFAULT_LIVING_SPACE)

                context.getString(
                    R.string.full_address,
                    FIRST_DEFAULT_GRID_ZONE,
                    FIRST_DEFAULT_STREET_NAME,
                    FIRST_DEFAULT_CITY,
                    FIRST_DEFAULT_STATE,
                    FIRST_DEFAULT_POSTAL_CODE
                )
                context.getString(
                    R.string.full_address,
                    SECOND_DEFAULT_GRID_ZONE,
                    SECOND_DEFAULT_STREET_NAME,
                    SECOND_DEFAULT_CITY,
                    SECOND_DEFAULT_STATE,
                    SECOND_DEFAULT_POSTAL_CODE
                )
            }
            confirmVerified(
                getAllRealEstatesUseCase, searchRepository, testCoroutineRule.getTestCoroutineDispatcherProvider(), context
            )
        }
    }

    @Test
    fun `get all real estates with filter by nonsense filter`() = testCoroutineRule.runTest {
        // GIVEN
        realEstatesListViewModel.onSortedOrFilterClicked(-10000)

        // WHEN
        realEstatesListViewModel.realEstateLiveData.observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(getAllDefaultRealEstatesListViewStates())

            verify {
                getAllRealEstatesUseCase.invoke()
                searchRepository.getCurrentFilterParametersFlow()

                context.getString(R.string.real_estate_price, FIRST_DEFAULT_PRICE)
                context.getString(R.string.real_estate_price, SECOND_DEFAULT_PRICE)
                context.getString(
                    R.string.number_rooms, FIRST_DEFAULT_NUMBER_ROOM, FIRST_DEFAULT_NUMBER_BEDROOM, FIRST_DEFAULT_NUMBER_BATHROOM
                )
                context.getString(
                    R.string.number_rooms, SECOND_DEFAULT_NUMBER_ROOM, SECOND_DEFAULT_NUMBER_BEDROOM, SECOND_DEFAULT_NUMBER_BATHROOM
                )

                context.getString(R.string.square_meter, FIRST_DEFAULT_LIVING_SPACE)
                context.getString(R.string.square_meter, SECOND_DEFAULT_LIVING_SPACE)

                context.getString(
                    R.string.full_address,
                    FIRST_DEFAULT_GRID_ZONE,
                    FIRST_DEFAULT_STREET_NAME,
                    FIRST_DEFAULT_CITY,
                    FIRST_DEFAULT_STATE,
                    FIRST_DEFAULT_POSTAL_CODE
                )
                context.getString(
                    R.string.full_address,
                    SECOND_DEFAULT_GRID_ZONE,
                    SECOND_DEFAULT_STREET_NAME,
                    SECOND_DEFAULT_CITY,
                    SECOND_DEFAULT_STATE,
                    SECOND_DEFAULT_POSTAL_CODE
                )
            }
            confirmVerified(
                getAllRealEstatesUseCase, searchRepository, testCoroutineRule.getTestCoroutineDispatcherProvider(), context
            )
        }
    }

    //region ================================================================ DEFAULT ===============================================================

    private fun getAllDefaultRealEstatesWithPhotos(): List<RealEstateEntityWithPhotos> {
        return listOf(getDefaultFirstRealEstateEntityWithPhotos(), getDefaultSecondRealEstateEntityWithPhotos())
    }

    private fun getAllDefaultRealEstatesListViewStates(): List<RealEstatesListViewState> {
        return listOf(getDefaultFirstRealEstatesListViewState(), getDefaultSecondRealEstatesListViewState())
    }

    private fun getEmptyDefaultRealEstatesListViewStates(): List<RealEstatesListViewState> {
        return emptyList()
    }

    private fun getDefaultRealEstatesListViewStatesFilterByPenthouse(): List<RealEstatesListViewState> {
        return listOf(getDefaultFirstRealEstatesListViewState())
    }

    // FIRST

    private fun getDefaultFirstRealEstateEntityWithPhotos(): RealEstateEntityWithPhotos {
        return RealEstateEntityWithPhotos(getDefaultFirstRealEstateEntity(), getDefaultFirstPhotoEntities())
    }

    private fun getDefaultFirstRealEstateEntity(): RealEstateEntity {
        return RealEstateEntity(
            id = FIRST_DEFAULT_REAL_ESTATE_ID,
            type = FIRST_DEFAULT_TYPE,
            price = FIRST_DEFAULT_PRICE,
            livingSpace = FIRST_DEFAULT_LIVING_SPACE,
            numberRooms = FIRST_DEFAULT_NUMBER_ROOM,
            numberBedroom = FIRST_DEFAULT_NUMBER_BEDROOM,
            numberBathroom = FIRST_DEFAULT_NUMBER_BATHROOM,
            description = FIRST_DEFAULT_DESCRIPTION,
            postalCode = FIRST_DEFAULT_POSTAL_CODE,
            state = FIRST_DEFAULT_STATE,
            city = FIRST_DEFAULT_CITY,
            streetName = FIRST_DEFAULT_STREET_NAME,
            gridZone = FIRST_DEFAULT_GRID_ZONE,
            status = FIRST_DEFAULT_STATUS_AVAILABLE,
            entryDate = FIRST_DEFAULT_ENTRY_DATE,
            saleDate = FIRST_DEFAULT_SALE_DATE,
            latitude = FIRST_DEFAULT_LAT,
            longitude = FIRST_DEFAULT_LONG,
            agentInChargeId = AGENT_ID_1,
        )
    }

    private fun getDefaultFirstPhotoEntities(): List<PhotoEntity> {
        return listOf(
            PhotoEntity(PHOTO_ENTITY_DEFAULT_ID, FIRST_DEFAULT_REAL_ESTATE_ID, FIRST_PHOTO_1),
            PhotoEntity(PHOTO_ENTITY_DEFAULT_ID, FIRST_DEFAULT_REAL_ESTATE_ID, FIRST_PHOTO_2),
            PhotoEntity(PHOTO_ENTITY_DEFAULT_ID, FIRST_DEFAULT_REAL_ESTATE_ID, FIRST_PHOTO_3),
            PhotoEntity(PHOTO_ENTITY_DEFAULT_ID, FIRST_DEFAULT_REAL_ESTATE_ID, FIRST_PHOTO_4)
        )
    }

    private fun getDefaultFirstRealEstatesListViewState(): RealEstatesListViewState {
        return RealEstatesListViewState(
            id = getDefaultFirstRealEstateEntity().id,
            type = getDefaultFirstRealEstateEntity().type,
            price = FIRST_DEFAULT_PRICE_STRING,
            priceValue = getDefaultFirstRealEstateEntity().price,
            numberRooms = FIRST_DEFAULT_NUMBER_ALL_ROOMS_STRING,
            numberRoomsValue = getDefaultFirstRealEstateEntity().numberRooms,
            livingSpace = FIRST_DEFAULT_LIVING_SPACE_STRING,
            livingSpaceValue = getDefaultFirstRealEstateEntity().livingSpace,
            description = getDefaultFirstRealEstateEntity().description,
            photos = getDefaultFirstDetailsPhotoViewState(),
            address = FIRST_DEFAULT_FULL_ADDRESS,
            entryDate = FIRST_DEFAULT_ENTRY_DATE_STRING,
            saleDate = "",
            isSold = false,
            onClickedCallback = EquatableCallback { },
        )
    }

    private fun getDefaultFirstDetailsPhotoViewState(): List<ListPhotoViewState> {
        return getDefaultFirstPhotoEntities().map { ListPhotoViewState(it.photo) }
    }

    // SECOND

    private fun getDefaultSecondRealEstateEntityWithPhotos(): RealEstateEntityWithPhotos {
        return RealEstateEntityWithPhotos(getDefaultSecondRealEstateEntity(), getDefaultSecondPhotoEntities())
    }

    private fun getDefaultSecondRealEstateEntity(): RealEstateEntity {
        return RealEstateEntity(
            id = SECOND_DEFAULT_REAL_ESTATE_ID,
            type = SECOND_DEFAULT_TYPE,
            price = SECOND_DEFAULT_PRICE,
            livingSpace = SECOND_DEFAULT_LIVING_SPACE,
            numberRooms = SECOND_DEFAULT_NUMBER_ROOM,
            numberBedroom = SECOND_DEFAULT_NUMBER_BEDROOM,
            numberBathroom = SECOND_DEFAULT_NUMBER_BATHROOM,
            description = SECOND_DEFAULT_DESCRIPTION,
            postalCode = SECOND_DEFAULT_POSTAL_CODE,
            state = SECOND_DEFAULT_STATE,
            city = SECOND_DEFAULT_CITY,
            streetName = SECOND_DEFAULT_STREET_NAME,
            gridZone = SECOND_DEFAULT_GRID_ZONE,
            status = SECOND_DEFAULT_STATUS_AVAILABLE,
            entryDate = SECOND_DEFAULT_ENTRY_DATE,
            saleDate = SECOND_DEFAULT_SALE_DATE,
            latitude = SECOND_DEFAULT_LAT,
            longitude = SECOND_DEFAULT_LONG,
            agentInChargeId = AGENT_ID_2,
        )
    }


    private fun getDefaultSecondPhotoEntities(): List<PhotoEntity> {
        return listOf(
            PhotoEntity(PHOTO_ENTITY_DEFAULT_ID, SECOND_DEFAULT_REAL_ESTATE_ID, FIRST_PHOTO_1),
            PhotoEntity(PHOTO_ENTITY_DEFAULT_ID, SECOND_DEFAULT_REAL_ESTATE_ID, FIRST_PHOTO_2),
            PhotoEntity(PHOTO_ENTITY_DEFAULT_ID, SECOND_DEFAULT_REAL_ESTATE_ID, FIRST_PHOTO_3),
            PhotoEntity(PHOTO_ENTITY_DEFAULT_ID, SECOND_DEFAULT_REAL_ESTATE_ID, FIRST_PHOTO_4)
        )
    }


    private fun getDefaultSecondRealEstatesListViewState(): RealEstatesListViewState {
        return RealEstatesListViewState(
            id = getDefaultSecondRealEstateEntity().id,
            type = getDefaultSecondRealEstateEntity().type,
            price = SECOND_DEFAULT_PRICE_STRING,
            priceValue = getDefaultSecondRealEstateEntity().price,
            numberRooms = SECOND_DEFAULT_NUMBER_ALL_ROOMS_STRING,
            numberRoomsValue = getDefaultSecondRealEstateEntity().numberRooms,
            livingSpace = SECOND_DEFAULT_LIVING_SPACE_STRING,
            livingSpaceValue = getDefaultSecondRealEstateEntity().livingSpace,
            description = getDefaultSecondRealEstateEntity().description,
            photos = getDefaultSecondDetailsPhotoViewState(),
            address = SECOND_DEFAULT_FULL_ADDRESS,
            entryDate = SECOND_DEFAULT_ENTRY_DATE_STRING,
            saleDate = "",
            isSold = false,
            onClickedCallback = EquatableCallback { },
        )
    }

    private fun getDefaultSecondDetailsPhotoViewState(): List<ListPhotoViewState> {
        return getDefaultFirstPhotoEntities().map { ListPhotoViewState(it.photo) }
    }

    // THIRD

    private fun getDefaultThirdRealEstateEntity(): RealEstateEntityWithPhotos {
        return RealEstateEntityWithPhotos(
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
    }

    //endregion

    companion object {
        private val emptyListOfFiltersFlow = MutableStateFlow<List<Filter>>(emptyList())
        private const val PHOTO_ENTITY_DEFAULT_ID = 0L
        private const val AGENT_ID_1 = 1L
        private const val AGENT_ID_2 = 2L

        private const val DEFAULT_TIMESTAMP_LONG = 1_000_000_000L // 09/09/2001 - 01:46:40
        private val fixedClock = Clock.fixed(Instant.ofEpochSecond(DEFAULT_TIMESTAMP_LONG), ZoneOffset.UTC)

        // FIRST
        private const val FIRST_DEFAULT_REAL_ESTATE_ID = 1L

        private const val FIRST_DEFAULT_TYPE = "Penthouse"

        private val FIRST_DEFAULT_PRICE = BigDecimal(29872000)

        //        private const val FIRST_DEFAULT_PRICE_STRING = "29 872 000$"
        private const val FIRST_DEFAULT_PRICE_STRING = "29872000"
        private const val FIRST_DEFAULT_PRICE_STRING_TO_ADDED = "29872000"
        private const val FIRST_DEFAULT_LIVING_SPACE = 8072.9
        private const val FIRST_DEFAULT_LIVING_SPACE_STRING = "8072.9 m²"
        private const val FIRST_DEFAULT_NUMBER_ROOM = 8
        private const val FIRST_DEFAULT_NUMBER_BEDROOM = 4
        private const val FIRST_DEFAULT_NUMBER_BATHROOM = 2

        private const val FIRST_DEFAULT_NUMBER_ALL_ROOMS_STRING =
            "$FIRST_DEFAULT_NUMBER_ROOM Rooms • $FIRST_DEFAULT_NUMBER_BEDROOM Bedrooms •$FIRST_DEFAULT_NUMBER_BATHROOM Bathrooms"

        private const val FIRST_DEFAULT_DESCRIPTION = "Anchored by a vast marble gallery with sweeping staircase, "
        private const val FIRST_DEFAULT_POSTAL_CODE = "10010"
        private const val FIRST_DEFAULT_STATE = "NY"
        private const val FIRST_DEFAULT_CITY = "New York"
        private const val FIRST_DEFAULT_STREET_NAME = "25th Street"
        private const val FIRST_DEFAULT_GRID_ZONE = "55 West"
        private const val FIRST_DEFAULT_FULL_ADDRESS = "55 West 25th Street, New York, NY, 10010"
        private const val FIRST_DEFAULT_STATUS_AVAILABLE = "Available"
        private val FIRST_DEFAULT_ENTRY_DATE = LocalDateTime.now(fixedClock)
        private const val FIRST_DEFAULT_ENTRY_DATE_STRING = "09/09/2001 01:46"
        private val FIRST_DEFAULT_SALE_DATE = null

        private const val FIRST_DEFAULT_LAT = 40.744081
        private const val FIRST_DEFAULT_LONG = -73.991302

        private const val FIRST_PHOTO_1 = "FIRST_PHOTO_1"
        private const val FIRST_PHOTO_2 = "FIRST_PHOTO_2"
        private const val FIRST_PHOTO_3 = "FIRST_PHOTO_3"
        private const val FIRST_PHOTO_4 = "FIRST_PHOTO_4"

        // SECOND
        private const val SECOND_DEFAULT_REAL_ESTATE_ID = 2L

        private const val SECOND_DEFAULT_TYPE = "Apartment"

        private val SECOND_DEFAULT_PRICE = BigDecimal(10495.05)

        //        private const val SECOND_DEFAULT_PRICE_STRING = "10 495.05$"
        private const val SECOND_DEFAULT_PRICE_STRING = "10495.05"
        private const val SECOND_DEFAULT_PRICE_STRING_TO_ADDED = "10495.05"
        private const val SECOND_DEFAULT_LIVING_SPACE = 1410.0
        private const val SECOND_DEFAULT_LIVING_SPACE_STRING = "1410.0 m²"
        private const val SECOND_DEFAULT_NUMBER_ROOM = 4
        private const val SECOND_DEFAULT_NUMBER_BEDROOM = 2
        private const val SECOND_DEFAULT_NUMBER_BATHROOM = 1

        private const val SECOND_DEFAULT_NUMBER_ALL_ROOMS_STRING =
            "$SECOND_DEFAULT_NUMBER_ROOM Rooms • $SECOND_DEFAULT_NUMBER_BEDROOM Bedrooms •$SECOND_DEFAULT_NUMBER_BATHROOM Bathrooms"

        private const val SECOND_DEFAULT_DESCRIPTION =
            "Welcome to this two bedroom Park Avenue residence on the luxurious Upper East Side. The apartment features arched windows, stainless steel appliances and custom white hardwood floors"
        private const val SECOND_DEFAULT_POSTAL_CODE = "10028"
        private const val SECOND_DEFAULT_STATE = "NY"
        private const val SECOND_DEFAULT_CITY = "New York"
        private const val SECOND_DEFAULT_STREET_NAME = "920 Park Avenue"
        private const val SECOND_DEFAULT_GRID_ZONE = "920 Park"
        private const val SECOND_DEFAULT_FULL_ADDRESS = "55 West 25th Street, New York, NY, 10010"
        private const val SECOND_DEFAULT_STATUS_AVAILABLE = "Available"
        private val SECOND_DEFAULT_ENTRY_DATE = LocalDateTime.now(fixedClock)
        private const val SECOND_DEFAULT_ENTRY_DATE_STRING = "09/09/2001 01:46"
        private val SECOND_DEFAULT_SALE_DATE = null

        private const val SECOND_DEFAULT_LAT = 40.776670
        private const val SECOND_DEFAULT_LONG = -73.960240

        private const val SECOND_PHOTO_1 = "SECOND_PHOTO_1"
        private const val SECOND_PHOTO_2 = "SECOND_PHOTO_2"
        private const val SECOND_PHOTO_3 = "SECOND_PHOTO_3"
        private const val SECOND_PHOTO_4 = "SECOND_PHOTO_4"

        private fun realEstateTypeToSpinnerPosition(type: String): Int {
            val types = arrayListOf("House", "Penthouse", "Duplex", "Flat", "Loft")
            return types.indexOf(type)
        }

        private fun spinnerPositionToType(position: Int): String {
            val types = arrayListOf("House", "Penthouse", "Duplex", "Flat", "Loft")
            return types[position]
        }
    }
}