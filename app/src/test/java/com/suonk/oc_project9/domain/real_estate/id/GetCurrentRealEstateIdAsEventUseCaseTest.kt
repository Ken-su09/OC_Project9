package com.suonk.oc_project9.domain.real_estate.id

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.suonk.oc_project9.domain.CurrentRealEstateIdRepository
import com.suonk.oc_project9.utils.TestCoroutineRule
import com.suonk.oc_project9.utils.observeForTesting
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runCurrent
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetCurrentRealEstateIdAsEventUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val currentRealEstateIdRepository: CurrentRealEstateIdRepository = mockk()

    private val getCurrentRealEstateIdAsEventUseCase = GetCurrentRealEstateIdAsEventUseCase(currentRealEstateIdRepository)

    @Before
    fun setup() {

    }

    @Test
    fun `initial case`() = testCoroutineRule.runTest {
        // GIVEN
        val realEstateId = 6L
        val channel = Channel<Long?>(capacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
        every { currentRealEstateIdRepository.getCurrentRealEstateIdChannel() } returns channel

        // WHEN
        getCurrentRealEstateIdAsEventUseCase.invoke().test {
            channel.trySend(realEstateId)
            runCurrent()

            // THEN
            assertThat(awaitItem()).isEqualTo(realEstateId)
            assertThat(awaitComplete()).isEqualTo(realEstateId)

            verify { currentRealEstateIdRepository.getCurrentRealEstateIdChannel() }
            confirmVerified(currentRealEstateIdRepository)
        }
    }
}