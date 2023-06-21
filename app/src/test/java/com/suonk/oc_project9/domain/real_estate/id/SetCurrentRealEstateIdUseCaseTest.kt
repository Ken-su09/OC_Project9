package com.suonk.oc_project9.domain.real_estate.id

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.suonk.oc_project9.domain.CurrentRealEstateIdRepository
import com.suonk.oc_project9.utils.TestCoroutineRule
import io.mockk.confirmVerified
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SetCurrentRealEstateIdUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val currentRealEstateIdRepository: CurrentRealEstateIdRepository = mockk()

    private val setCurrentRealEstateIdUseCase = SetCurrentRealEstateIdUseCase(currentRealEstateIdRepository)

    @Before
    fun setup() {

    }

    @Test
    fun `initial case`() = testCoroutineRule.runTest {
        // GIVEN
        justRun { currentRealEstateIdRepository.setCurrentRealEstateIdFlow(null) }

        setCurrentRealEstateIdUseCase.invoke(null)

        // WHEN
        verify { currentRealEstateIdRepository.setCurrentRealEstateIdFlow(null) }
        confirmVerified(currentRealEstateIdRepository)
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // GIVEN
        justRun { currentRealEstateIdRepository.setCurrentRealEstateIdFlow(1L) }

        setCurrentRealEstateIdUseCase.invoke(1L)

        // WHEN
        verify { currentRealEstateIdRepository.setCurrentRealEstateIdFlow(1L) }
        confirmVerified(currentRealEstateIdRepository)
    }
}