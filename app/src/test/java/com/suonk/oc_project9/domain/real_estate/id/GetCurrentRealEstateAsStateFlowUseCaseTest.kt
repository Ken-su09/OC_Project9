package com.suonk.oc_project9.domain.real_estate.id

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.suonk.oc_project9.domain.CurrentRealEstateIdRepository
import com.suonk.oc_project9.utils.TestCoroutineRule
import com.suonk.oc_project9.utils.observeForTesting
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetCurrentRealEstateAsStateFlowUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val currentRealEstateIdRepository: CurrentRealEstateIdRepository = mockk()

    private val getCurrentRealEstateAsStateFlowUseCase = GetCurrentRealEstateAsStateFlowUseCase(currentRealEstateIdRepository)

    @Before
    fun setup() {

    }

    @Test
    fun `initial case`() = testCoroutineRule.runTest {
        // GIVEN
        every { currentRealEstateIdRepository.getCurrentRealEstateIdFlow() } returns MutableStateFlow<Long?>(null)

        // WHEN
        getCurrentRealEstateAsStateFlowUseCase.invoke().asLiveData().observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(null)

            verify { currentRealEstateIdRepository.getCurrentRealEstateIdFlow() }
            confirmVerified(currentRealEstateIdRepository)
        }
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // GIVEN
        every { currentRealEstateIdRepository.getCurrentRealEstateIdFlow() } returns MutableStateFlow(1L)

        // WHEN
        getCurrentRealEstateAsStateFlowUseCase.invoke().asLiveData().observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(1L)

            verify { currentRealEstateIdRepository.getCurrentRealEstateIdFlow() }
            confirmVerified(currentRealEstateIdRepository)
        }
    }
}