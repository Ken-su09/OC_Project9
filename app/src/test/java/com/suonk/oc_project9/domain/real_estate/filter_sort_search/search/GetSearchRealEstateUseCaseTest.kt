package com.suonk.oc_project9.domain.real_estate.filter_sort_search.search

import app.cash.turbine.test
import com.suonk.oc_project9.domain.SearchRepository
import com.suonk.oc_project9.utils.TestCoroutineRule
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class GetSearchRealEstateUseCaseTest {

    private companion object {
        private const val defaultSearchInitialCase = ""
        private const val defaultSearchNominalCase = "defaultSearchNominalCase"
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val searchRepository: SearchRepository = mockk()

    private val getSearchRealEstateUseCase = GetSearchRealEstateUseCase(searchRepository)

    @Test
    fun `test get search real estate use case invoke - initial case`() = testCoroutineRule.runTest {
        // GIVEN
        val currentSearchFlow = MutableStateFlow(defaultSearchInitialCase)
        every { searchRepository.getCurrentSearchParametersFlow() } returns currentSearchFlow

        // WHEN
        getSearchRealEstateUseCase.invoke().test {
            // THEN
            assertEquals(defaultSearchInitialCase, awaitItem())

            verify { searchRepository.getCurrentSearchParametersFlow() }

            confirmVerified(searchRepository)
        }
    }

    @Test
    fun `test get search real estate use case invoke - nominal case`() = testCoroutineRule.runTest {
        // GIVEN
        val currentSearchFlow = MutableStateFlow(defaultSearchNominalCase)
        every { searchRepository.getCurrentSearchParametersFlow() } returns currentSearchFlow

        // WHEN
        getSearchRealEstateUseCase.invoke().test {
            // THEN
            assertEquals(defaultSearchNominalCase, awaitItem())

            verify { searchRepository.getCurrentSearchParametersFlow() }

            confirmVerified(searchRepository)
        }
    }
}