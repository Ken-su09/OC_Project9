package com.suonk.oc_project9.domain.real_estate.filter_sort_search.search

import com.suonk.oc_project9.domain.SearchRepository
import com.suonk.oc_project9.utils.TestCoroutineRule
import io.mockk.*
import org.junit.Rule
import org.junit.Test

class SetSearchRealEstateUseCaseTest {
    private companion object {
        private const val defaultSearchInitialCase = ""
        private const val defaultSearchNominalCase = "defaultSearchNominalCase"
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val searchRepository: SearchRepository = mockk()

    private val setSearchRealEstateUseCase = SetSearchRealEstateUseCase(searchRepository)

    @Test
    fun `test set search real estate use case invoke - initial case`() = testCoroutineRule.runTest {
        // GIVEN
        justRun { searchRepository.setCurrentSearchParameters(defaultSearchInitialCase) }

        // WHEN
        setSearchRealEstateUseCase.invoke(defaultSearchInitialCase)

        // GIVEN
        verify { searchRepository.setCurrentSearchParameters(defaultSearchInitialCase) }

        confirmVerified(searchRepository)
    }

    @Test
    fun `test set search real estate use case invoke - nominal case`() = testCoroutineRule.runTest {
        // GIVEN
        justRun { searchRepository.setCurrentSearchParameters(defaultSearchNominalCase) }

        // WHEN
        setSearchRealEstateUseCase.invoke(defaultSearchNominalCase)

        // GIVEN
        verify { searchRepository.setCurrentSearchParameters(defaultSearchNominalCase) }

        confirmVerified(searchRepository)
    }
}