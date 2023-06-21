package com.suonk.oc_project9.domain.real_estate.get

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.suonk.oc_project9.domain.RealEstateRepository
import com.suonk.oc_project9.model.database.data.entities.real_estate.PhotoEntity
import com.suonk.oc_project9.model.database.data.entities.real_estate.RealEstateEntity
import com.suonk.oc_project9.model.database.data.entities.real_estate.RealEstateEntityWithPhotos
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

class GetAllRealEstatesUseCaseTest {

    private companion object {
        private const val PHOTO_ENTITY_DEFAULT_ID = 0L
        private const val AGENT_ID_1 = 1L
        private const val AGENT_ID_2 = 2L

        private const val DEFAULT_TIMESTAMP_LONG = 1_000_000_000L // 09/09/2001 - 01:46:40
        private val fixedClock = Clock.fixed(Instant.ofEpochSecond(DEFAULT_TIMESTAMP_LONG), ZoneOffset.UTC)

        // FIRST
        private const val FIRST_DEFAULT_REAL_ESTATE_ID = 1L

        private const val FIRST_DEFAULT_TYPE = "Penthouse"

        private val FIRST_DEFAULT_PRICE = BigDecimal(29872000)

        private const val FIRST_DEFAULT_LIVING_SPACE = 8072.9
        private const val FIRST_DEFAULT_NUMBER_ROOM = 8
        private const val FIRST_DEFAULT_NUMBER_BEDROOM = 4
        private const val FIRST_DEFAULT_NUMBER_BATHROOM = 2

        private const val FIRST_DEFAULT_DESCRIPTION = "Anchored by a vast marble gallery with sweeping staircase, "
        private const val FIRST_DEFAULT_POSTAL_CODE = "10010"
        private const val FIRST_DEFAULT_STATE = "NY"
        private const val FIRST_DEFAULT_CITY = "New York"
        private const val FIRST_DEFAULT_STREET_NAME = "25th Street"
        private const val FIRST_DEFAULT_GRID_ZONE = "55 West"
        private const val FIRST_DEFAULT_STATUS_AVAILABLE = "Available"
        private val FIRST_DEFAULT_ENTRY_DATE = LocalDateTime.now(fixedClock)
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

        private const val SECOND_DEFAULT_LIVING_SPACE = 1410.0
        private const val SECOND_DEFAULT_NUMBER_ROOM = 4
        private const val SECOND_DEFAULT_NUMBER_BEDROOM = 2
        private const val SECOND_DEFAULT_NUMBER_BATHROOM = 1

        private const val SECOND_DEFAULT_DESCRIPTION =
            "Welcome to this two bedroom Park Avenue residence on the luxurious Upper East Side. The apartment features arched windows, stainless steel appliances and custom white hardwood floors"
        private const val SECOND_DEFAULT_POSTAL_CODE = "10028"
        private const val SECOND_DEFAULT_STATE = "NY"
        private const val SECOND_DEFAULT_CITY = "New York"
        private const val SECOND_DEFAULT_STREET_NAME = "920 Park Avenue"
        private const val SECOND_DEFAULT_GRID_ZONE = "920 Park"
        private const val SECOND_DEFAULT_STATUS_AVAILABLE = "Available"
        private val SECOND_DEFAULT_ENTRY_DATE = LocalDateTime.now(fixedClock)
        private val SECOND_DEFAULT_SALE_DATE = null

        private const val SECOND_DEFAULT_LAT = 40.776670
        private const val SECOND_DEFAULT_LONG = -73.960240

        private fun getAllDefaultRealEstatesWithPhotos(): List<RealEstateEntityWithPhotos> {
            return listOf(getDefaultFirstRealEstateEntityWithPhotos(), getDefaultSecondRealEstateEntityWithPhotos())
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
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val realEstateRepository: RealEstateRepository = mockk()

    private val getAllRealEstatesUseCase = GetAllRealEstatesUseCase(realEstateRepository)

    @Before
    fun setup() {

    }

    @Test
    fun `initial case`() = testCoroutineRule.runTest {
        // GIVEN
        every { realEstateRepository.getAllRealEstatesWithPhotos() } returns flowOf(listOf())

        // WHEN
        getAllRealEstatesUseCase.invoke().asLiveData().observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(listOf())

            verify {
                realEstateRepository.getAllRealEstatesWithPhotos()
            }

            confirmVerified(realEstateRepository)
        }
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // GIVEN
        every { realEstateRepository.getAllRealEstatesWithPhotos() } returns flowOf(getAllDefaultRealEstatesWithPhotos())

        // WHEN
        getAllRealEstatesUseCase.invoke().asLiveData().observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(getAllDefaultRealEstatesWithPhotos())

            verify {
                realEstateRepository.getAllRealEstatesWithPhotos()
            }

            confirmVerified(realEstateRepository)
        }
    }
}