package com.suonk.oc_project9.model.database.data

import app.cash.turbine.test
import com.suonk.oc_project9.model.database.dao.RealEstateDao
import com.suonk.oc_project9.model.database.data.entities.real_estate.PhotoEntity
import com.suonk.oc_project9.model.database.data.entities.real_estate.RealEstateEntity
import com.suonk.oc_project9.model.database.data.entities.real_estate.RealEstateEntityWithPhotos
import com.suonk.oc_project9.model.database.data.repositories.RealEstateRepositoryImpl
import com.suonk.oc_project9.utils.TestCoroutineRule
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset


class RealEstateRepositoryImplTest {

    private companion object {
        private const val PHOTO_ENTITY_DEFAULT_ID = 0L
        private const val AGENT_ID_1 = 1L
        private const val AGENT_ID_2 = 2L

        private const val DEFAULT_TIMESTAMP_LONG = 1_000_000_000L // 09/09/2001 - 01:46:40
        private val fixedClock = Clock.fixed(Instant.ofEpochSecond(DEFAULT_TIMESTAMP_LONG), ZoneOffset.UTC)

        private const val INITIAL_CASE_REAL_ESTATE_ID = 0L

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
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val realEstateDao: RealEstateDao = mockk()

    private val realEstateRepositoryImpl = RealEstateRepositoryImpl(realEstateDao)

    @Test
    fun `get all real estates with photos`() = testCoroutineRule.runTest {
        // GIVEN
        every { realEstateDao.getAllRealEstatesWithPhotos() } returns flowOf(getAllDefaultRealEstatesWithPhotos())

        // WHEN
        realEstateRepositoryImpl.getAllRealEstatesWithPhotos().test {
            // THEN
            assertEquals(getAllDefaultRealEstatesWithPhotos(), awaitItem())
            awaitComplete()

            verify { realEstateDao.getAllRealEstatesWithPhotos() }
            confirmVerified(realEstateDao)
        }
    }

    @Test
    fun `get real estate by id`() = testCoroutineRule.runTest {
        // GIVEN
        every { realEstateDao.getRealEstateById(FIRST_DEFAULT_REAL_ESTATE_ID) } returns flowOf(getDefaultFirstRealEstateEntityWithPhotos())

        // WHEN
        realEstateRepositoryImpl.getRealEstateById(FIRST_DEFAULT_REAL_ESTATE_ID).test {
            // THEN
            assertEquals(getDefaultFirstRealEstateEntityWithPhotos(), awaitItem())
            awaitComplete()

            verify { realEstateDao.getRealEstateById(FIRST_DEFAULT_REAL_ESTATE_ID) }
            confirmVerified(realEstateDao)
        }
    }

    @Test
    fun `upsert real estate`() = testCoroutineRule.runTest {
        // GIVEN
        coJustRun { realEstateDao.upsertRealEstate(getDefaultFirstRealEstateEntity()) }

        // WHEN
        realEstateRepositoryImpl.upsertRealEstate(getDefaultFirstRealEstateEntity())

        // THEN
        coVerify { realEstateDao.upsertRealEstate(getDefaultFirstRealEstateEntity()) }
        confirmVerified(realEstateDao)
    }

    @Test
    fun `update real estate`() = testCoroutineRule.runTest {
        // GIVEN
        coJustRun { realEstateDao.updateRealEstate(getDefaultFirstRealEstateEntity()) }

        // WHEN
        realEstateRepositoryImpl.updateRealEstate(getDefaultFirstRealEstateEntity())

        // THEN
        coVerify { realEstateDao.updateRealEstate(getDefaultFirstRealEstateEntity()) }
        confirmVerified(realEstateDao)
    }

    @Test
    fun `delete real estate`() = testCoroutineRule.runTest {
        // GIVEN
        coJustRun { realEstateDao.deleteRealEstate(getDefaultFirstRealEstateEntity()) }

        // WHEN
        realEstateRepositoryImpl.deleteRealEstate(getDefaultFirstRealEstateEntity())

        // THEN
        coVerify { realEstateDao.deleteRealEstate(getDefaultFirstRealEstateEntity()) }
        confirmVerified(realEstateDao)
    }



    @Test
    fun `delete real estate by id`() = testCoroutineRule.runTest {
        // GIVEN
        coJustRun { realEstateDao.deleteRealEstateById(FIRST_DEFAULT_REAL_ESTATE_ID) }

        // WHEN
        realEstateRepositoryImpl.deleteRealEstateById(FIRST_DEFAULT_REAL_ESTATE_ID)

        // THEN
        coVerify { realEstateDao.deleteRealEstateById(FIRST_DEFAULT_REAL_ESTATE_ID) }
        confirmVerified(realEstateDao)
    }

    //region ==================================================================== DEFAULTS ====================================================================

    // LIST
    private fun getAllDefaultRealEstatesWithPhotos(): List<RealEstateEntityWithPhotos> {
        return listOf(getDefaultFirstRealEstateEntityWithPhotos(), getDefaultSecondRealEstateEntityWithPhotos())
    }


    private fun getDefaultEmptyRealEstateEntity(): RealEstateEntity {
        return RealEstateEntity(
            id = 0L,
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
            entryDate = FIRST_DEFAULT_ENTRY_DATE,
            saleDate = null,
            latitude = 0.0,
            longitude = 0.0,
            agentInChargeId = null,
        )
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

    //endregion
}