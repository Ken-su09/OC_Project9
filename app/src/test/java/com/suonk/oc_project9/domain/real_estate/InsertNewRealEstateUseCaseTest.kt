package com.suonk.oc_project9.domain.real_estate

import com.suonk.oc_project9.domain.PhotoRepository
import com.suonk.oc_project9.domain.RealEstateRepository
import com.suonk.oc_project9.domain.real_estate.upsert.UpsertNewRealEstateUseCase
import com.suonk.oc_project9.model.database.data.entities.real_estate.RealEstateEntity
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewModelTest
import com.suonk.oc_project9.utils.Defaults.getDefaultAggregatedPhotos
import com.suonk.oc_project9.utils.TestCoroutineRule
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class InsertNewRealEstateUseCaseTest {

    companion object {
        const val DEFAULT_TIMESTAMP_LONG = 1_000_000_000L // 09/09/2001 - 01:46:40
        private val fixedClock = Clock.fixed(Instant.ofEpochSecond(DEFAULT_TIMESTAMP_LONG), ZoneOffset.UTC)
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val realEstateRepository: RealEstateRepository = mockk()
    private val photoRepository: PhotoRepository = mockk()

    private val insertNewRealEstateUseCase = UpsertNewRealEstateUseCase(realEstateRepository, photoRepository)

    @Before
    fun setup() {
        coEvery { realEstateRepository.upsertRealEstate(any()) } returns 1L
        coEvery { photoRepository.insertPhoto(any()) } returns Unit
    }

    @Test
    fun `test invoke insert a new real estate use case`() = testCoroutineRule.runTest {
        // WHEN
        insertNewRealEstateUseCase.invoke(getDefaultRealEstateEntity(), getDefaultAggregatedPhotos())

        // THEN
        coVerify(atLeast = 1) {
            realEstateRepository.upsertRealEstate(getDefaultRealEstateEntity())
            photoRepository.insertPhoto(any())
        }
        confirmVerified(realEstateRepository, photoRepository)
    }


    private fun getDefaultRealEstateEntity(): RealEstateEntity {
        return RealEstateEntity(
            id = RealEstateDetailsViewModelTest.DEFAULT_ID,
            type = RealEstateDetailsViewModelTest.DEFAULT_TYPE,
            price = RealEstateDetailsViewModelTest.DEFAULT_PRICE,
            livingSpace = RealEstateDetailsViewModelTest.DEFAULT_LIVING_SPACE,
            numberRooms = RealEstateDetailsViewModelTest.DEFAULT_NUMBER_ROOM,
            numberBedroom = RealEstateDetailsViewModelTest.DEFAULT_NUMBER_BEDROOM,
            numberBathroom = RealEstateDetailsViewModelTest.DEFAULT_NUMBER_BATHROOM,
            description = RealEstateDetailsViewModelTest.DEFAULT_DESCRIPTION,
            postalCode = RealEstateDetailsViewModelTest.DEFAULT_POSTAL_CODE,
            state = RealEstateDetailsViewModelTest.DEFAULT_STATE,
            city = RealEstateDetailsViewModelTest.DEFAULT_CITY,
            streetName = RealEstateDetailsViewModelTest.DEFAULT_STREET_NAME,
            gridZone = RealEstateDetailsViewModelTest.DEFAULT_GRID_ZONE,
            status = RealEstateDetailsViewModelTest.DEFAULT_STATUS_AVAILABLE,
            entryDate = LocalDateTime.now(fixedClock),
            saleDate = null,
            latitude = RealEstateDetailsViewModelTest.DEFAULT_LAT,
            longitude = RealEstateDetailsViewModelTest.DEFAULT_LONG,
            agentInChargeId = 1L,
        )
    }

}