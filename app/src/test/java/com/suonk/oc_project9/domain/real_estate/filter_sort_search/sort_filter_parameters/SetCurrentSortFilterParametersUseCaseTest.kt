package com.suonk.oc_project9.domain.real_estate.filter_sort_search.sort_filter_parameters

import com.suonk.oc_project9.domain.SearchRepository
import io.mockk.confirmVerified
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class SetCurrentSortFilterParametersUseCaseTest {

    private companion object {
        private const val defaultItemId = 0
    }

    private val searchRepository: SearchRepository = mockk()
    private val setCurrentSortFilterParametersUseCase = SetCurrentSortFilterParametersUseCase(searchRepository)

    @Test
    fun `test set current sort filter parameters`() {
        // GIVEN
        justRun { searchRepository.setCurrentSortFilterParametersFlow(defaultItemId) }

        // WHEN
        setCurrentSortFilterParametersUseCase.invoke(defaultItemId)

        // THEN
        verify { searchRepository.setCurrentSortFilterParametersFlow(defaultItemId) }

        confirmVerified(searchRepository)
    }
}