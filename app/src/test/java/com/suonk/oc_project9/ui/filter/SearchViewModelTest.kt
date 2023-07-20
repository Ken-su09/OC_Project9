package com.suonk.oc_project9.ui.filter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.suonk.oc_project9.domain.SearchRepository
import com.suonk.oc_project9.domain.more_criteria.ToggleFilterUseCase
import com.suonk.oc_project9.domain.more_criteria.model.FilterQuery
import io.mockk.confirmVerified
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class SearchViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private companion object {
        private const val emptyField = ""

        // Living Space
        private val livingSpaceMinNull = null
        private const val livingSpaceMinString = "1000"
        private const val livingSpaceMinDouble = 1000.0

        private val livingSpaceMaxNull = null
        private const val livingSpaceMaxString = "10000"
        private const val livingSpaceMaxDouble = 10000.0

        // Price
        private val priceMinNull = null
        private const val priceMinString = "15000"
        private val priceMinBigDecimal = BigDecimal(15000.0)

        private val priceMaxNull = null
        private const val priceMaxString = "55000"
        private val priceMaxBigDecimal = BigDecimal(55000.0)

        // Nb Rooms
        private val nbRoomsMinNull = null
        private const val nbRoomsMinString = "7"
        private const val nbRoomsMinInt = 7

        private val nbRoomsMaxNull = null
        private const val nbRoomsMaxString = "10"
        private const val nbRoomsMaxInt = 10

        // Nb Bedrooms
        private val nbBedroomsMinNull = null
        private const val nbBedroomsMinString = "3"
        private const val nbBedroomsMinInt = 3

        private val nbBedroomsMaxNull = null
        private const val nbBedroomsMaxString = "5"
        private const val nbBedroomsMaxInt = 5

        // Nb Bathrooms
        private val nbBathroomsMinNull = null
        private const val nbBathroomsMinString = "2"
        private const val nbBathroomsMinInt = 2

        private val nbBathroomsMaxNull = null
        private const val nbBathroomsMaxString = "3"
        private const val nbBathroomsMaxInt = 3

        // Entry Date
        private val entryDateFromNull = null
        private const val entryDateFromString = "19.07.2023"
        private const val entryDateFromDayInt = 19
        private const val entryDateFromMonthInt = 7
        private const val entryDateFromYearInt = 2023
        private val entryDateFromLocalDateTime = LocalDateTime.of(entryDateFromYearInt, entryDateFromMonthInt, entryDateFromDayInt, 0, 0, 0)

        private val entryDateToNull = null
        private const val entryDateToString = "25.07.2023"
        private const val entryDateToDayInt = 25
        private const val entryDateToMonthInt = 7
        private const val entryDateToYearInt = 2023
        private val entryDateToLocalDateTime = LocalDateTime.of(entryDateToYearInt, entryDateToMonthInt, entryDateToDayInt, 0, 0, 0)

        // Non Sense
        private const val entryDateFromNonSenseString = "19072023"
        private const val entryDateToNonSenseString = "25072023"

        private const val entryDateFromWithTextInsideString = "test.with.text"

        // Sale Date
        private val saleDateFromNull = null
        private const val saleDateFromString = "04.09.2023"
        private const val saleDateFromDayInt = 4
        private const val saleDateFromMonthInt = 9
        private const val saleDateFromYearInt = 2023
        private val saleDateFromLocalDateTime = LocalDateTime.of(saleDateFromYearInt, saleDateFromMonthInt, saleDateFromDayInt, 0, 0, 0)

        private val saleDateToNull = null
        private const val saleDateToString = "21.09.2023"
        private const val saleDateToDayInt = 21
        private const val saleDateToMonthInt = 9
        private const val saleDateToYearInt = 2023
        private val saleDateToLocalDateTime = LocalDateTime.of(saleDateToYearInt, saleDateToMonthInt, saleDateToDayInt, 0, 0, 0)
    }

    private val toggleFilterUseCase: ToggleFilterUseCase = mockk()
    private val searchRepository: SearchRepository = mockk()

    private val searchViewModel = SearchViewModel(toggleFilterUseCase = toggleFilterUseCase, searchRepository = searchRepository)

    @Test
    fun `test onResetFiltersClicked`() {
        // GIVEN
        justRun { searchRepository.reset() }

        // WHEN
        searchViewModel.onResetFiltersClicked()

        // GIVEN
        verify { searchRepository.reset() }

        confirmVerified(toggleFilterUseCase, searchRepository)
    }

    @Test
    fun `test onValidateClicked - initial case - null`() {
        // GIVEN
        justRun { toggleFilterUseCase.invoke(defaultLivingSpaceQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultPriceQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultNbRoomsQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultNbBedroomsQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultNbBathroomsQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultEntryDateQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultSaleDateQueryWithNullParams()) }

        // WHEN
        searchViewModel.onValidateClicked(
            livingSpaceMin = livingSpaceMinNull,
            livingSpaceMax = livingSpaceMaxNull,
            priceMin = priceMinNull,
            priceMax = priceMaxNull,
            nbRoomsMin = nbRoomsMinNull,
            nbRoomsMax = nbRoomsMaxNull,
            nbBedroomsMin = nbBedroomsMinNull,
            nbBedroomsMax = nbBedroomsMaxNull,
            nbBathroomsMin = nbBathroomsMinNull,
            nbBathroomsMax = nbBathroomsMaxNull,
            entryDateFromValue = entryDateFromNull,
            entryDateToValue = entryDateToNull,
            saleDateFromValue = saleDateFromNull,
            saleDateToValue = saleDateToNull,
        )

        // GIVEN
        verify {
            toggleFilterUseCase.invoke(defaultLivingSpaceQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultPriceQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultNbRoomsQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultNbBedroomsQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultNbBathroomsQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultEntryDateQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultSaleDateQueryWithNullParams())
        }

        confirmVerified(toggleFilterUseCase, searchRepository)
    }

    @Test
    fun `test onValidateClicked - initial case - empty`() {
        // GIVEN
        justRun { toggleFilterUseCase.invoke(defaultLivingSpaceQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultPriceQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultNbRoomsQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultNbBedroomsQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultNbBathroomsQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultEntryDateQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultSaleDateQueryWithNullParams()) }

        // WHEN
        searchViewModel.onValidateClicked(
            livingSpaceMin = emptyField,
            livingSpaceMax = emptyField,
            priceMin = emptyField,
            priceMax = emptyField,
            nbRoomsMin = emptyField,
            nbRoomsMax = emptyField,
            nbBedroomsMin = emptyField,
            nbBedroomsMax = emptyField,
            nbBathroomsMin = emptyField,
            nbBathroomsMax = emptyField,
            entryDateFromValue = emptyField,
            entryDateToValue = emptyField,
            saleDateFromValue = emptyField,
            saleDateToValue = emptyField,
        )

        // GIVEN
        verify {
            toggleFilterUseCase.invoke(defaultLivingSpaceQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultPriceQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultNbRoomsQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultNbBedroomsQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultNbBathroomsQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultEntryDateQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultSaleDateQueryWithNullParams())
        }

        confirmVerified(toggleFilterUseCase, searchRepository)
    }

    @Test
    fun `test onValidateClicked - nominal case - living space and price - both params`() {
        // GIVEN
        justRun { toggleFilterUseCase.invoke(defaultLivingSpaceQueryWithBothParams()) }
        justRun { toggleFilterUseCase.invoke(defaultPriceQueryWithBothParams()) }
        justRun { toggleFilterUseCase.invoke(defaultNbRoomsQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultNbBedroomsQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultNbBathroomsQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultEntryDateQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultSaleDateQueryWithNullParams()) }

        // WHEN
        searchViewModel.onValidateClicked(
            livingSpaceMin = livingSpaceMinString,
            livingSpaceMax = livingSpaceMaxString,
            priceMin = priceMinString,
            priceMax = priceMaxString,
            nbRoomsMin = nbRoomsMinNull,
            nbRoomsMax = nbRoomsMaxNull,
            nbBedroomsMin = nbBedroomsMinNull,
            nbBedroomsMax = nbBedroomsMaxNull,
            nbBathroomsMin = nbBathroomsMinNull,
            nbBathroomsMax = nbBathroomsMaxNull,
            entryDateFromValue = entryDateFromNull,
            entryDateToValue = entryDateToNull,
            saleDateFromValue = saleDateFromNull,
            saleDateToValue = saleDateToNull,
        )

        // GIVEN
        verify {
            toggleFilterUseCase.invoke(defaultLivingSpaceQueryWithBothParams())
            toggleFilterUseCase.invoke(defaultPriceQueryWithBothParams())
            toggleFilterUseCase.invoke(defaultNbRoomsQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultNbBedroomsQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultNbBathroomsQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultEntryDateQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultSaleDateQueryWithNullParams())
        }

        confirmVerified(toggleFilterUseCase, searchRepository)
    }

    @Test
    fun `test onValidateClicked - nominal case - living space and price - one params`() {
        // GIVEN
        justRun { toggleFilterUseCase.invoke(defaultLivingSpaceQueryWithOneParams()) }
        justRun { toggleFilterUseCase.invoke(defaultPriceQueryWithOneParams()) }
        justRun { toggleFilterUseCase.invoke(defaultNbRoomsQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultNbBedroomsQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultNbBathroomsQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultEntryDateQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultSaleDateQueryWithNullParams()) }

        // WHEN
        searchViewModel.onValidateClicked(
            livingSpaceMin = livingSpaceMinString,
            livingSpaceMax = livingSpaceMaxNull,
            priceMin = priceMinString,
            priceMax = priceMaxNull,
            nbRoomsMin = nbRoomsMinNull,
            nbRoomsMax = nbRoomsMaxNull,
            nbBedroomsMin = nbBedroomsMinNull,
            nbBedroomsMax = nbBedroomsMaxNull,
            nbBathroomsMin = nbBathroomsMinNull,
            nbBathroomsMax = nbBathroomsMaxNull,
            entryDateFromValue = entryDateFromNull,
            entryDateToValue = entryDateToNull,
            saleDateFromValue = saleDateFromNull,
            saleDateToValue = saleDateToNull,
        )

        // GIVEN
        verify {
            toggleFilterUseCase.invoke(defaultLivingSpaceQueryWithOneParams())
            toggleFilterUseCase.invoke(defaultPriceQueryWithOneParams())
            toggleFilterUseCase.invoke(defaultNbRoomsQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultNbBedroomsQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultNbBathroomsQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultEntryDateQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultSaleDateQueryWithNullParams())
        }

        confirmVerified(toggleFilterUseCase, searchRepository)
    }

    @Test
    fun `test onValidateClicked - nominal case - filter by rooms, bedrooms and bathrooms`() {
        // GIVEN
        justRun { toggleFilterUseCase.invoke(defaultLivingSpaceQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultPriceQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultNbRoomsQueryWithBothParams()) }
        justRun { toggleFilterUseCase.invoke(defaultNbBedroomsQueryWithBothParams()) }
        justRun { toggleFilterUseCase.invoke(defaultNbBathroomsQueryWithBothParams()) }
        justRun { toggleFilterUseCase.invoke(defaultEntryDateQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultSaleDateQueryWithNullParams()) }

        // WHEN
        searchViewModel.onValidateClicked(
            livingSpaceMin = livingSpaceMinNull,
            livingSpaceMax = livingSpaceMaxNull,
            priceMin = priceMinNull,
            priceMax = priceMaxNull,
            nbRoomsMin = nbRoomsMinString,
            nbRoomsMax = nbRoomsMaxString,
            nbBedroomsMin = nbBedroomsMinString,
            nbBedroomsMax = nbBedroomsMaxString,
            nbBathroomsMin = nbBathroomsMinString,
            nbBathroomsMax = nbBathroomsMaxString,
            entryDateFromValue = entryDateFromNull,
            entryDateToValue = entryDateToNull,
            saleDateFromValue = saleDateFromNull,
            saleDateToValue = saleDateToNull,
        )

        // GIVEN
        verify {
            toggleFilterUseCase.invoke(defaultLivingSpaceQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultPriceQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultNbRoomsQueryWithBothParams())
            toggleFilterUseCase.invoke(defaultNbBedroomsQueryWithBothParams())
            toggleFilterUseCase.invoke(defaultNbBathroomsQueryWithBothParams())
            toggleFilterUseCase.invoke(defaultEntryDateQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultSaleDateQueryWithNullParams())
        }

        confirmVerified(toggleFilterUseCase, searchRepository)
    }

    @Test
    fun `test onValidateClicked - nominal case - entry and sale date - both params`() {
        // GIVEN
        justRun { toggleFilterUseCase.invoke(defaultLivingSpaceQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultPriceQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultNbRoomsQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultNbBedroomsQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultNbBathroomsQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultEntryDateQueryWithBothParams()) }
        justRun { toggleFilterUseCase.invoke(defaultSaleDateQueryWithBothParams()) }

        // WHEN
        searchViewModel.onValidateClicked(
            livingSpaceMin = livingSpaceMinNull,
            livingSpaceMax = livingSpaceMaxNull,
            priceMin = priceMinNull,
            priceMax = priceMaxNull,
            nbRoomsMin = nbRoomsMinNull,
            nbRoomsMax = nbRoomsMaxNull,
            nbBedroomsMin = nbBedroomsMinNull,
            nbBedroomsMax = nbBedroomsMaxNull,
            nbBathroomsMin = nbBathroomsMinNull,
            nbBathroomsMax = nbBathroomsMaxNull,
            entryDateFromValue = entryDateFromString,
            entryDateToValue = entryDateToString,
            saleDateFromValue = saleDateFromString,
            saleDateToValue = saleDateToString,
        )

        // GIVEN
        verify {
            toggleFilterUseCase.invoke(defaultLivingSpaceQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultPriceQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultNbRoomsQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultNbBedroomsQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultNbBathroomsQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultEntryDateQueryWithBothParams())
            toggleFilterUseCase.invoke(defaultSaleDateQueryWithBothParams())
        }

        confirmVerified(toggleFilterUseCase, searchRepository)
    }

    @Test
    fun `test onValidateClicked - nominal case - entry and sale date - both params - but non sense entry date`() {
        // GIVEN
        justRun { toggleFilterUseCase.invoke(defaultLivingSpaceQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultPriceQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultNbRoomsQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultNbBedroomsQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultNbBathroomsQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultEntryDateQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultSaleDateQueryWithBothParams()) }

        // WHEN
        searchViewModel.onValidateClicked(
            livingSpaceMin = livingSpaceMinNull,
            livingSpaceMax = livingSpaceMaxNull,
            priceMin = priceMinNull,
            priceMax = priceMaxNull,
            nbRoomsMin = nbRoomsMinNull,
            nbRoomsMax = nbRoomsMaxNull,
            nbBedroomsMin = nbBedroomsMinNull,
            nbBedroomsMax = nbBedroomsMaxNull,
            nbBathroomsMin = nbBathroomsMinNull,
            nbBathroomsMax = nbBathroomsMaxNull,
            entryDateFromValue = entryDateFromNonSenseString,
            entryDateToValue = entryDateToNonSenseString,
            saleDateFromValue = saleDateFromString,
            saleDateToValue = saleDateToString,
        )

        // GIVEN
        verify {
            toggleFilterUseCase.invoke(defaultLivingSpaceQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultPriceQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultNbRoomsQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultNbBedroomsQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultNbBathroomsQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultEntryDateQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultSaleDateQueryWithBothParams())
        }

        confirmVerified(toggleFilterUseCase, searchRepository)
    }

    @Test
    fun `test onValidateClicked - nominal case - entry and sale date - both params - but only one point entry date to`() {
        // GIVEN
        justRun { toggleFilterUseCase.invoke(defaultLivingSpaceQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultPriceQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultNbRoomsQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultNbBedroomsQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultNbBathroomsQueryWithNullParams()) }
        justRun { toggleFilterUseCase.invoke(defaultEntryDateQueryWithOneParams()) }
        justRun { toggleFilterUseCase.invoke(defaultSaleDateQueryWithBothParams()) }

        // WHEN
        searchViewModel.onValidateClicked(
            livingSpaceMin = livingSpaceMinNull,
            livingSpaceMax = livingSpaceMaxNull,
            priceMin = priceMinNull,
            priceMax = priceMaxNull,
            nbRoomsMin = nbRoomsMinNull,
            nbRoomsMax = nbRoomsMaxNull,
            nbBedroomsMin = nbBedroomsMinNull,
            nbBedroomsMax = nbBedroomsMaxNull,
            nbBathroomsMin = nbBathroomsMinNull,
            nbBathroomsMax = nbBathroomsMaxNull,
            entryDateFromValue = entryDateFromWithTextInsideString,
            entryDateToValue = entryDateToString,
            saleDateFromValue = saleDateFromString,
            saleDateToValue = saleDateToString,
        )

        // GIVEN
        verify {
            toggleFilterUseCase.invoke(defaultLivingSpaceQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultPriceQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultNbRoomsQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultNbBedroomsQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultNbBathroomsQueryWithNullParams())
            toggleFilterUseCase.invoke(defaultEntryDateQueryWithOneParams())
            toggleFilterUseCase.invoke(defaultSaleDateQueryWithBothParams())
        }

        confirmVerified(toggleFilterUseCase, searchRepository)
    }

    //region ============================================================= DEFAULT VALUES =============================================================

    //region LIVING SPACE

    private fun defaultLivingSpaceQueryWithNullParams(): FilterQuery {
        return FilterQuery.LivingSpaceFilter(
            FilterQuery.SearchParam.Update(livingSpaceMinNull),
            FilterQuery.SearchParam.Update(livingSpaceMaxNull)
        )
    }

    private fun defaultLivingSpaceQueryWithBothParams(): FilterQuery {
        return FilterQuery.LivingSpaceFilter(
            FilterQuery.SearchParam.Update(livingSpaceMinDouble),
            FilterQuery.SearchParam.Update(livingSpaceMaxDouble)
        )
    }

    private fun defaultLivingSpaceQueryWithOneParams(): FilterQuery {
        return FilterQuery.LivingSpaceFilter(
            FilterQuery.SearchParam.Update(livingSpaceMinDouble),
            FilterQuery.SearchParam.Update(livingSpaceMaxNull)
        )
    }

    //endregion

    //region PRICE

    private fun defaultPriceQueryWithNullParams(): FilterQuery {
        return FilterQuery.PriceFilter(
            FilterQuery.SearchParam.Update(priceMinNull),
            FilterQuery.SearchParam.Update(priceMaxNull)
        )
    }

    private fun defaultPriceQueryWithBothParams(): FilterQuery {
        return FilterQuery.PriceFilter(
            FilterQuery.SearchParam.Update(priceMinBigDecimal),
            FilterQuery.SearchParam.Update(priceMaxBigDecimal)
        )
    }

    private fun defaultPriceQueryWithOneParams(): FilterQuery {
        return FilterQuery.PriceFilter(
            FilterQuery.SearchParam.Update(priceMinBigDecimal),
            FilterQuery.SearchParam.Update(priceMaxNull)
        )
    }

    //endregion

    //region NB ROOMS

    private fun defaultNbRoomsQueryWithNullParams(): FilterQuery {
        return FilterQuery.NbRoomsFilter(
            FilterQuery.SearchParam.Update(nbRoomsMinNull),
            FilterQuery.SearchParam.Update(nbRoomsMaxNull)
        )
    }

    private fun defaultNbRoomsQueryWithBothParams(): FilterQuery {
        return FilterQuery.NbRoomsFilter(
            FilterQuery.SearchParam.Update(nbRoomsMinInt),
            FilterQuery.SearchParam.Update(nbRoomsMaxInt)
        )
    }

    private fun defaultNbRoomsQueryWithOneParams(): FilterQuery {
        return FilterQuery.NbRoomsFilter(
            FilterQuery.SearchParam.Update(nbRoomsMinInt),
            FilterQuery.SearchParam.Update(nbRoomsMaxNull)
        )
    }

    //endregion

    //region NB BEDROOMS

    private fun defaultNbBedroomsQueryWithNullParams(): FilterQuery {
        return FilterQuery.NbBedroomsFilter(
            FilterQuery.SearchParam.Update(nbBedroomsMinNull),
            FilterQuery.SearchParam.Update(nbBedroomsMaxNull)
        )
    }

    private fun defaultNbBedroomsQueryWithBothParams(): FilterQuery {
        return FilterQuery.NbBedroomsFilter(
            FilterQuery.SearchParam.Update(nbBedroomsMinInt),
            FilterQuery.SearchParam.Update(nbBedroomsMaxInt)
        )
    }

    private fun defaultNbBedroomsQueryWithOneParams(): FilterQuery {
        return FilterQuery.NbBedroomsFilter(
            FilterQuery.SearchParam.Update(nbBedroomsMinInt),
            FilterQuery.SearchParam.Update(nbBedroomsMaxNull)
        )
    }

    //endregion

    //region NB BATHROOMS

    private fun defaultNbBathroomsQueryWithNullParams(): FilterQuery {
        return FilterQuery.NbBathroomsFilter(
            FilterQuery.SearchParam.Update(nbBathroomsMinNull),
            FilterQuery.SearchParam.Update(nbBathroomsMaxNull)
        )
    }

    private fun defaultNbBathroomsQueryWithBothParams(): FilterQuery {
        return FilterQuery.NbBathroomsFilter(
            FilterQuery.SearchParam.Update(nbBathroomsMinInt),
            FilterQuery.SearchParam.Update(nbBathroomsMaxInt)
        )
    }

    private fun defaultNbBathroomsQueryWithOneParams(): FilterQuery {
        return FilterQuery.NbBathroomsFilter(
            FilterQuery.SearchParam.Update(nbBathroomsMinInt),
            FilterQuery.SearchParam.Update(nbBathroomsMaxNull)
        )
    }

    //endregion

    //region ENTRY DATE

    private fun defaultEntryDateQueryWithNullParams(): FilterQuery {
        return FilterQuery.EntryDateFilter(
            FilterQuery.SearchParam.Update(entryDateFromNull),
            FilterQuery.SearchParam.Update(entryDateToNull)
        )
    }

    private fun defaultEntryDateQueryWithBothParams(): FilterQuery {
        return FilterQuery.EntryDateFilter(
            FilterQuery.SearchParam.Update(entryDateFromLocalDateTime),
            FilterQuery.SearchParam.Update(entryDateToLocalDateTime)
        )
    }

    private fun defaultEntryDateQueryWithOneParams(): FilterQuery {
        return FilterQuery.EntryDateFilter(
            FilterQuery.SearchParam.Update(entryDateFromNull),
            FilterQuery.SearchParam.Update(entryDateToLocalDateTime)
        )
    }

    //endregion

    //region SALE DATE

    private fun defaultSaleDateQueryWithNullParams(): FilterQuery {
        return FilterQuery.SaleDateFilter(
            FilterQuery.SearchParam.Update(saleDateFromNull),
            FilterQuery.SearchParam.Update(saleDateToNull)
        )
    }

    private fun defaultSaleDateQueryWithBothParams(): FilterQuery {
        return FilterQuery.SaleDateFilter(
            FilterQuery.SearchParam.Update(saleDateFromLocalDateTime),
            FilterQuery.SearchParam.Update(saleDateToLocalDateTime)
        )
    }

    private fun defaultSaleDateQueryWithOneParams(): FilterQuery {
        return FilterQuery.SaleDateFilter(
            FilterQuery.SearchParam.Update(saleDateFromLocalDateTime),
            FilterQuery.SearchParam.Update(saleDateToNull)
        )
    }

    //endregion

    //endregion
}