package com.suonk.oc_project9.domain.real_estate

import com.suonk.oc_project9.domain.PhotoRepository
import com.suonk.oc_project9.domain.RealEstateRepository
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

class UpdateRealEstateUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val realEstateRepository: RealEstateRepository = mockk()
    private val photoRepository: PhotoRepository = mockk()

    private val updateRealEstateUseCase = UpdateRealEstateUseCase(realEstateRepository, photoRepository)

    @Before
    fun setup() {
        coJustRun { realEstateRepository.updateRealEstate(any()) }
    }

    @Test
    fun `test invoke update a real estate use case`() = testCoroutineRule.runTest {
        // WHEN
        updateRealEstateUseCase.invoke(getDefaultRealEstateEntity(), getDefaultPhotoViewStates())

        // THEN
        coVerify(exactly = 1) { realEstateRepository.updateRealEstate(getDefaultRealEstateEntity()) }
        confirmVerified(realEstateRepository)
    }
}