package com.suonk.oc_project9.domain.real_estate.get

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.suonk.oc_project9.domain.RealEstateRepository
import com.suonk.oc_project9.model.database.data.entities.places.Position
import com.suonk.oc_project9.model.database.data.entities.real_estate.PhotoEntity
import com.suonk.oc_project9.model.database.data.entities.real_estate.RealEstateEntity
import com.suonk.oc_project9.model.database.data.entities.real_estate.RealEstateEntityWithPhotos
import com.suonk.oc_project9.ui.real_estates.details.AggregatedPhoto
import com.suonk.oc_project9.ui.real_estates.details.DetailsPhotoViewState
import com.suonk.oc_project9.utils.EquatableCallback
import com.suonk.oc_project9.utils.TestCoroutineRule
import com.suonk.oc_project9.utils.observeForTesting
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
import java.time.ZoneOffset

class GetRealEstateFlowByIdUseCaseTest {

    private companion object {
        const val INITIAL_DEFAULT_ID = 0L
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
        const val DEFAULT_PRICE_STRING_TO_ADDED_WITH_COMMA = "20321,135"
        const val DEFAULT_LIVING_SPACE = 8072.9
        const val DEFAULT_LIVING_SPACE_STRING = "8072.9"
        const val DEFAULT_LIVING_SPACE_STRING_TO_ADDED_WITH_COMMA = "3321,135"
        const val DEFAULT_LIVING_SPACE_WITH_COMMA = 3321135.0
        const val DEFAULT_NUMBER_ROOM = 8
        const val DEFAULT_NUMBER_ROOM_STRING = "8"
        const val DEFAULT_NUMBER_BEDROOM = 4
        const val DEFAULT_NUMBER_BEDROOM_STRING = "4"
        const val DEFAULT_NUMBER_BATHROOM = 2
        const val DEFAULT_NUMBER_BATHROOM_STRING = "2"
        const val DEFAULT_TIMESTAMP_LONG = 1_000_000_000L // 09/09/2001 - 01:46:40
        const val SALE_DATE_DEFAULT_TIMESTAMP_LONG = 2_000_000_000L // 09/09/2001 - 01:46:40

        private val fixedClock = Clock.fixed(Instant.ofEpochSecond(DEFAULT_TIMESTAMP_LONG), ZoneOffset.UTC)

        private fun realEstateTypeToSpinnerPosition(type: String): Int {
            val types = arrayListOf("House", "Penthouse", "Duplex", "Flat", "Loft")
            return types.indexOf(type)
        }

        private fun spinnerPositionToType(position: Int): String {
            val types = arrayListOf("House", "Penthouse", "Duplex", "Flat", "Loft")
            return types[position]
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

        fun getDefaultAggregatedPhotos(): List<AggregatedPhoto> {
            return listOf(
                AggregatedPhoto(
                    uri = "https://photos.zillowstatic.com/fp/390793abc077faf2df87690ad3f9940c-se_extra_large_1500_800.webp"

                ), AggregatedPhoto(
                    uri = "https://photos.zillowstatic.com/fp/344beadccb742f876c027673bfccccf2-se_extra_large_1500_800.webp"

                ), AggregatedPhoto(
                    uri = "https://photos.zillowstatic.com/fp/9d28f752e5f90e54d151a41114db6040-se_extra_large_1500_800.webp"
                )
            )
        }

        fun getDefaultListOfPhotoEntity(): List<PhotoEntity> {
            return listOf(
                PhotoEntity(
                    id = 1L,
                    realEstateId = DEFAULT_ID,
                    photo = "https://photos.zillowstatic.com/fp/390793abc077faf2df87690ad3f9940c-se_extra_large_1500_800.webp"

                ), PhotoEntity(
                    id = 2L,
                    realEstateId = DEFAULT_ID,
                    photo = "https://photos.zillowstatic.com/fp/344beadccb742f876c027673bfccccf2-se_extra_large_1500_800.webp"

                ), PhotoEntity(
                    id = 3L,
                    realEstateId = DEFAULT_ID,
                    photo = "https://photos.zillowstatic.com/fp/9d28f752e5f90e54d151a41114db6040-se_extra_large_1500_800.webp"
                )
            )
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

        private fun getDefaultEmptyRealEstateEntity(): RealEstateEntity {
            return RealEstateEntity(
                id = INITIAL_DEFAULT_ID,
                type = "",
                price = BigDecimal(0.0),
                livingSpace = 0.0,
                numberRooms = 0,
                numberBedroom = 0,
                numberBathroom = 0,
                description = "",
                postalCode = "",
                state = "",
                city = "",
                streetName = "",
                gridZone = "",
                status = "",
                entryDate = LocalDateTime.now(fixedClock),
                saleDate = null,
                latitude = 0.0,
                longitude = 0.0,
                agentInChargeId = 0L,
            )
        }

        private fun getDefaultRealEstateWithPhotos() =
            RealEstateEntityWithPhotos(getDefaultRealEstateEntity(), getDefaultListOfPhotoEntity())

        private fun getEmptyDefaultRealEstateWithPhotos() =
            RealEstateEntityWithPhotos(getDefaultEmptyRealEstateEntity(), listOf())
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val realEstateRepository: RealEstateRepository = mockk()

    private val getRealEstateFlowByIdUseCase = GetRealEstateFlowByIdUseCase(realEstateRepository)

    @Before
    fun setup() {

    }

    @Test
    fun `initial case`() = testCoroutineRule.runTest {
        // GIVEN
        every { realEstateRepository.getRealEstateById(INITIAL_DEFAULT_ID) } returns flowOf(getEmptyDefaultRealEstateWithPhotos())

        // WHEN
        getRealEstateFlowByIdUseCase.invoke(INITIAL_DEFAULT_ID).asLiveData().observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(getEmptyDefaultRealEstateWithPhotos())

            verify {
                realEstateRepository.getRealEstateById(INITIAL_DEFAULT_ID)
            }

            confirmVerified(realEstateRepository)
        }
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // GIVEN
        every { realEstateRepository.getRealEstateById(DEFAULT_ID) } returns flowOf(getDefaultRealEstateWithPhotos())

        // WHEN
        getRealEstateFlowByIdUseCase.invoke(DEFAULT_ID).asLiveData().observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(getDefaultRealEstateWithPhotos())

            verify {
                realEstateRepository.getRealEstateById(DEFAULT_ID)
            }

            confirmVerified(realEstateRepository)
        }
    }
}