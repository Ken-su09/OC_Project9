package com.suonk.oc_project9.domain.real_estate.filter_sort_search.sort_filter_parameters

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.suonk.oc_project9.domain.SearchRepository
import com.suonk.oc_project9.utils.TestCoroutineRule
import com.suonk.oc_project9.R
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class GetCurrentSortFilterParametersUseCaseTest {

    private companion object {
        private const val defaultCurrentSortFilterParameters = R.id.sort_by_date_desc
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val searchRepository: SearchRepository = mockk()
    private val getCurrentSortFilterParametersUseCase = GetCurrentSortFilterParametersUseCase(searchRepository)

    @Test
    fun `test set current sort filter parameters`() = testCoroutineRule.runTest {
        val expected = MutableStateFlow(defaultCurrentSortFilterParameters)

        // GIVEN
        every { searchRepository.getCurrentSortFilterParametersFlow() } returns expected

        // WHEN
        getCurrentSortFilterParametersUseCase.invoke().test {
            TestCase.assertEquals(defaultCurrentSortFilterParameters, awaitItem())
        }

        // THEN
        verify { searchRepository.getCurrentSortFilterParametersFlow() }

        confirmVerified(searchRepository)
    }
}