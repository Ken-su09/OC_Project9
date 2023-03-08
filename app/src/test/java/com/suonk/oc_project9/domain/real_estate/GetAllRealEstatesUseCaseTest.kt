package com.suonk.oc_project9.domain.real_estate

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.suonk.oc_project9.domain.RealEstateRepository
import com.suonk.oc_project9.model.database.data.entities.real_estate.RealEstateEntityWithPhotos
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test

class GetAllRealEstatesUseCaseTest {

    companion object {
        private val REAL_ESTATE_WITH_PHOTOS_FLOW: Flow<List<RealEstateEntityWithPhotos>> = flowOf()
    }

    private val realEstateRepository: RealEstateRepository = mockk()

    private val getAllRealEstatesUseCase = GetAllRealEstatesUseCase(realEstateRepository)

    @Before
    fun setup() {
        every {
            realEstateRepository.getAllRealEstatesWithPhotos()
        } returns REAL_ESTATE_WITH_PHOTOS_FLOW
    }

    @Test
    fun `test invoke get all real estates use case`() {
        // WHEN
        val getAllRealEstatesFlow = getAllRealEstatesUseCase.invoke()

        // THEN
        assertThat(getAllRealEstatesFlow).isEqualTo(REAL_ESTATE_WITH_PHOTOS_FLOW)
        verify(exactly = 1) { realEstateRepository.getAllRealEstatesWithPhotos() }
        confirmVerified(realEstateRepository)
    }
}