package com.suonk.oc_project9.domain.real_estate

import com.suonk.oc_project9.domain.PhotoRepository
import com.suonk.oc_project9.domain.RealEstateRepository
import com.suonk.oc_project9.model.database.data.entities.PhotoEntity
import com.suonk.oc_project9.utils.Defaults.getDefaultPhotoViewStates
import com.suonk.oc_project9.utils.Defaults.getDefaultRealEstateEntity
import com.suonk.oc_project9.utils.TestCoroutineRule
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InsertNewRealEstateUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val realEstateRepository: RealEstateRepository = mockk()
    private val photoRepository: PhotoRepository = mockk()

    private val insertNewRealEstateUseCase =
        UpsertNewRealEstateUseCase(realEstateRepository, photoRepository)

    @Before
    fun setup() {
        coJustRun { realEstateRepository.upsertRealEstate(any()) }
    }

    @Test
    fun `test invoke insert a new real estate use case`() = testCoroutineRule.runTest {
        // WHEN
        insertNewRealEstateUseCase.invoke(getDefaultRealEstateEntity(), getDefaultPhotoViewStates())

        // THEN
        coVerify(exactly = 1) { realEstateRepository.upsertRealEstate(getDefaultRealEstateEntity()) }
        coVerify(exactly = 3) {
            getDefaultPhotoViewStates().map {
                photoRepository.insertPhoto(
                    photo = PhotoEntity(0L, 1L, it.photo, it.isUri)
                )
            }
        }
        confirmVerified(realEstateRepository, photoRepository)
    }
}