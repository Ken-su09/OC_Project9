package com.suonk.oc_project9.domain.real_estate.filter_sort_search.sort_filter_parameters

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.suonk.oc_project9.R
import com.suonk.oc_project9.domain.SearchRepository
import com.suonk.oc_project9.utils.TestCoroutineRule
import com.suonk.oc_project9.utils.sort.Sorting
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class GetSortingParametersUseCaseTest {

    private companion object {
        private val defaultSortingParameters = Sorting.DATE_ASC
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val searchRepository: SearchRepository = mockk()
    private val getSortingParametersUseCase = GetSortingParametersUseCase(searchRepository)

    @Test
    fun `test set current sort filter parameters`() = testCoroutineRule.runTest {
        val expected = MutableStateFlow(defaultSortingParameters)

        // GIVEN
        every { searchRepository.getCurrentSortParameterFlow() } returns expected

        // WHEN
        getSortingParametersUseCase.invoke().test {
            assertEquals(defaultSortingParameters, awaitItem())
        }

        // THEN
        verify { searchRepository.getCurrentSortParameterFlow() }

        confirmVerified(searchRepository)
    }
}