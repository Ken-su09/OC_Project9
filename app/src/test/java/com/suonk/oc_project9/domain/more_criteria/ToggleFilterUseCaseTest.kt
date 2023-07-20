package com.suonk.oc_project9.domain.more_criteria

import com.suonk.oc_project9.domain.SearchRepository
import com.suonk.oc_project9.domain.more_criteria.model.FilterQuery
import com.suonk.oc_project9.ui.filter.Filter
import com.suonk.oc_project9.utils.TestCoroutineRule
import io.mockk.*
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class ToggleFilterUseCaseTest {

    private companion object {
        private const val DEFAULT_TIMESTAMP_LONG = 1_000_000_000L // 09/09/2001 - 01:46:40
        private val fixedClock = Clock.fixed(Instant.ofEpochSecond(DEFAULT_TIMESTAMP_LONG), ZoneOffset.UTC)

        // LIVING SPACE
        private const val defaultLivingSpaceFilterDoubleMin = 100.0
        private const val defaultLivingSpaceFilterDoubleMax = 2150.50

        private val defaultLivingSpaceFilterQueryMin = FilterQuery.SearchParam.Update(defaultLivingSpaceFilterDoubleMin)
        private val defaultLivingSpaceFilterQueryMax = FilterQuery.SearchParam.Update(defaultLivingSpaceFilterDoubleMax)

        // PRICE
        private val defaultPriceFilterDoubleMin = BigDecimal(100.0)
        private val defaultPriceFilterDoubleMax = BigDecimal(2150.50)

        private val defaultPriceFilterQueryMin = FilterQuery.SearchParam.Update(defaultPriceFilterDoubleMin)
        private val defaultPriceFilterQueryMax = FilterQuery.SearchParam.Update(defaultPriceFilterDoubleMax)

        // NB ROOMS
        private const val defaultNbRoomsFilterIntMin = 5
        private const val defaultNbRoomsFilterIntMax = 10

        private val defaultNbRoomsFilterQueryMin = FilterQuery.SearchParam.Update(defaultNbRoomsFilterIntMin)
        private val defaultNbRoomsFilterQueryMax = FilterQuery.SearchParam.Update(defaultNbRoomsFilterIntMax)

        // NB BEDROOMS
        private const val defaultNbBedroomsFilterIntMin = 3
        private const val defaultNbBedroomsFilterIntMax = 5

        private val defaultNbBedroomsFilterQueryMin = FilterQuery.SearchParam.Update(defaultNbBedroomsFilterIntMin)
        private val defaultNbBedroomsFilterQueryMax = FilterQuery.SearchParam.Update(defaultNbBedroomsFilterIntMax)

        // NB BATHROOMS
        private const val defaultNbBathroomsFilterIntMin = 1
        private const val defaultNbBathroomsFilterIntMax = 3

        private val defaultNbBathroomsFilterQueryMin = FilterQuery.SearchParam.Update(defaultNbBathroomsFilterIntMin)
        private val defaultNbBathroomsFilterQueryMax = FilterQuery.SearchParam.Update(defaultNbBathroomsFilterIntMax)

        // ENTRY DATE
        private val defaultEntryDateFilterMin = LocalDateTime.now(fixedClock)
        private val defaultEntryDateFilterMax = LocalDateTime.now(fixedClock)

        private val defaultEntryDateFilterQueryMin = FilterQuery.SearchParam.Update(defaultEntryDateFilterMin)
        private val defaultEntryDateFilterQueryMax = FilterQuery.SearchParam.Update(defaultEntryDateFilterMax)

        // SALE DATE
        private val defaultSaleDateFilterMin = LocalDateTime.now(fixedClock)
        private val defaultSaleDateFilterMax = LocalDateTime.now(fixedClock)

        private val defaultSaleDateFilterQueryMin = FilterQuery.SearchParam.Update(defaultSaleDateFilterMin)
        private val defaultSaleDateFilterQueryMax = FilterQuery.SearchParam.Update(defaultSaleDateFilterMax)

        private val defaultFilterQueryDelete = FilterQuery.SearchParam.Delete
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val searchRepository: SearchRepository = mockk()

    private val toggleFilterUseCase = ToggleFilterUseCase(searchRepository)

    // LIVING SPACE FILTER

    @Test
    fun `living space filter add`() = testCoroutineRule.runTest {
        // GIVEN
        val stateFlowOfFilters = MutableStateFlow(listOf<Filter>())
        every { searchRepository.getCurrentFilterParametersFlow() } returns stateFlowOfFilters
        justRun { searchRepository.addFilter(getLivingSpaceFilterWithBothParams()) }

        // WHEN
        toggleFilterUseCase.invoke(getLivingSpaceQueryWithBothParams())

        // GIVEN
        verify {
            searchRepository.getCurrentFilterParametersFlow()
            searchRepository.addFilter(getLivingSpaceFilterWithBothParams())
        }

        confirmVerified(searchRepository)
    }

    @Test
    fun `living space filter update modify filters parameters`() = testCoroutineRule.runTest {
        // GIVEN
        val stateFlowOfFilters = MutableStateFlow(getDefaultListFilters())
        every { searchRepository.getCurrentFilterParametersFlow() } returns stateFlowOfFilters
        justRun { searchRepository.addFilter(getLivingSpaceFilterWithBothParams()) }

        // WHEN
        toggleFilterUseCase.invoke(getLivingSpaceQueryWithBothParams())

        // GIVEN
        verify {
            searchRepository.getCurrentFilterParametersFlow()
            searchRepository.addFilter(getLivingSpaceFilterWithBothParams())
        }

        confirmVerified(searchRepository)
    }

    @Test
    fun `living space filter update delete max filter parameters`() = testCoroutineRule.runTest {
        // GIVEN
        val stateFlowOfFilters = MutableStateFlow(getDefaultListFilters())
        every { searchRepository.getCurrentFilterParametersFlow() } returns stateFlowOfFilters
        justRun { searchRepository.addFilter(getLivingSpaceFilterWithOnlyMinParam()) }

        // WHEN
        toggleFilterUseCase.invoke(getLivingSpaceFilterQueryWithOnlyMinParam())

        // GIVEN
        verify {
            searchRepository.getCurrentFilterParametersFlow()
            searchRepository.addFilter(getLivingSpaceFilterWithOnlyMinParam())
        }

        confirmVerified(searchRepository)
    }

    @Test
    fun `living space filter delete both filters parameters`() = testCoroutineRule.runTest {
        // GIVEN
        val stateFlowOfFilters = MutableStateFlow(getDefaultListFilters())
        every { searchRepository.getCurrentFilterParametersFlow() } returns stateFlowOfFilters

        // WHEN
        toggleFilterUseCase.invoke(getLivingSpaceQueryWithNoneParam())

        // GIVEN
        verify {
            searchRepository.getCurrentFilterParametersFlow()
        }

        confirmVerified(searchRepository)
    }

    // PRICE FILTER

    @Test
    fun `price filter add`() = testCoroutineRule.runTest {
        // GIVEN
        val stateFlowOfFilters = MutableStateFlow(listOf<Filter>())
        every { searchRepository.getCurrentFilterParametersFlow() } returns stateFlowOfFilters
        justRun { searchRepository.addFilter(getPriceFilterWithBothParams()) }

        // WHEN
        toggleFilterUseCase.invoke(getPriceFilterQueryWithBothParams())

        // GIVEN
        verify {
            searchRepository.getCurrentFilterParametersFlow()
            searchRepository.addFilter(getPriceFilterWithBothParams())
        }

        confirmVerified(searchRepository)
    }

    @Test
    fun `price filter update modify filters parameters`() = testCoroutineRule.runTest {
        // GIVEN
        val stateFlowOfFilters = MutableStateFlow(getDefaultListFilters())
        every { searchRepository.getCurrentFilterParametersFlow() } returns stateFlowOfFilters
        justRun { searchRepository.addFilter(getPriceFilterWithBothParams()) }

        // WHEN
        toggleFilterUseCase.invoke(getPriceFilterQueryWithBothParams())

        // GIVEN
        verify {
            searchRepository.getCurrentFilterParametersFlow()
            searchRepository.addFilter(getPriceFilterWithBothParams())
        }

        confirmVerified(searchRepository)
    }

    @Test
    fun `price filter update delete max filter parameters`() = testCoroutineRule.runTest {
        // GIVEN
        val stateFlowOfFilters = MutableStateFlow(getDefaultListFilters())
        every { searchRepository.getCurrentFilterParametersFlow() } returns stateFlowOfFilters
        justRun { searchRepository.addFilter(getPriceFilterWithOnlyMinParam()) }

        // WHEN
        toggleFilterUseCase.invoke(getPriceFilterQueryWithOnlyMinParam())

        // GIVEN
        verify {
            searchRepository.getCurrentFilterParametersFlow()
            searchRepository.addFilter(getPriceFilterWithOnlyMinParam())
        }

        confirmVerified(searchRepository)
    }

    @Test
    fun `price filter delete both filters parameters`() = testCoroutineRule.runTest {
        // GIVEN
        val stateFlowOfFilters = MutableStateFlow(getDefaultListFilters())
        every { searchRepository.getCurrentFilterParametersFlow() } returns stateFlowOfFilters

        // WHEN
        toggleFilterUseCase.invoke(getPriceFilterQueryWithNoneParam())

        // GIVEN
        verify {
            searchRepository.getCurrentFilterParametersFlow()
        }

        confirmVerified(searchRepository)
    }

    // NB ROOMS FILTER

    @Test
    fun `nb rooms filter add`() = testCoroutineRule.runTest {
        // GIVEN
        val stateFlowOfFilters = MutableStateFlow(listOf<Filter>())
        every { searchRepository.getCurrentFilterParametersFlow() } returns stateFlowOfFilters
        justRun { searchRepository.addFilter(getNbRoomsFilterWithBothParams()) }

        // WHEN
        toggleFilterUseCase.invoke(getNbRoomsFilterQueryWithBothParams())

        // GIVEN
        verify {
            searchRepository.getCurrentFilterParametersFlow()
            searchRepository.addFilter(getNbRoomsFilterWithBothParams())
        }

        confirmVerified(searchRepository)
    }

    @Test
    fun `nb rooms filter update modify filters parameters`() = testCoroutineRule.runTest {
        // GIVEN
        val stateFlowOfFilters = MutableStateFlow(getDefaultListFilters())
        every { searchRepository.getCurrentFilterParametersFlow() } returns stateFlowOfFilters
        justRun { searchRepository.addFilter(getNbRoomsFilterWithBothParams()) }

        // WHEN
        toggleFilterUseCase.invoke(getNbRoomsFilterQueryWithBothParams())

        // GIVEN
        verify {
            searchRepository.getCurrentFilterParametersFlow()
            searchRepository.addFilter(getNbRoomsFilterWithBothParams())
        }

        confirmVerified(searchRepository)
    }

    @Test
    fun `nb rooms filter update delete max filter parameters`() = testCoroutineRule.runTest {
        // GIVEN
        val stateFlowOfFilters = MutableStateFlow(getDefaultListFilters())
        every { searchRepository.getCurrentFilterParametersFlow() } returns stateFlowOfFilters
        justRun { searchRepository.addFilter(getNbRoomsFilterWithOnlyMinParam()) }

        // WHEN
        toggleFilterUseCase.invoke(getNbRoomsFilterQueryWithOnlyMinParam())

        // GIVEN
        verify {
            searchRepository.getCurrentFilterParametersFlow()
            searchRepository.addFilter(getNbRoomsFilterWithOnlyMinParam())
        }

        confirmVerified(searchRepository)
    }

    @Test
    fun `nb rooms filter delete both filters parameters`() = testCoroutineRule.runTest {
        // GIVEN
        val stateFlowOfFilters = MutableStateFlow(getDefaultListFilters())
        every { searchRepository.getCurrentFilterParametersFlow() } returns stateFlowOfFilters

        // WHEN
        toggleFilterUseCase.invoke(getNbRoomsFilterQueryWithNoneParam())

        // GIVEN
        verify {
            searchRepository.getCurrentFilterParametersFlow()
        }

        confirmVerified(searchRepository)
    }

    // NB BEDROOMS FILTER

    @Test
    fun `nb bedrooms filter add`() = testCoroutineRule.runTest {
        // GIVEN
        val stateFlowOfFilters = MutableStateFlow(listOf<Filter>())
        every { searchRepository.getCurrentFilterParametersFlow() } returns stateFlowOfFilters
        justRun { searchRepository.addFilter(getNbBedroomsFilterWithBothParams()) }

        // WHEN
        toggleFilterUseCase.invoke(getNbBedroomsFilterQueryWithBothParams())

        // GIVEN
        verify {
            searchRepository.getCurrentFilterParametersFlow()
            searchRepository.addFilter(getNbBedroomsFilterWithBothParams())
        }

        confirmVerified(searchRepository)
    }

    @Test
    fun `nb bedrooms filter update modify filters parameters`() = testCoroutineRule.runTest {
        // GIVEN
        val stateFlowOfFilters = MutableStateFlow(getDefaultListFilters())
        every { searchRepository.getCurrentFilterParametersFlow() } returns stateFlowOfFilters
        justRun { searchRepository.addFilter(getNbBedroomsFilterWithBothParams()) }

        // WHEN
        toggleFilterUseCase.invoke(getNbBedroomsFilterQueryWithBothParams())

        // GIVEN
        verify {
            searchRepository.getCurrentFilterParametersFlow()
            searchRepository.addFilter(getNbBedroomsFilterWithBothParams())
        }

        confirmVerified(searchRepository)
    }

    @Test
    fun `nb bedrooms filter update delete max filter parameters`() = testCoroutineRule.runTest {
        // GIVEN
        val stateFlowOfFilters = MutableStateFlow(getDefaultListFilters())
        every { searchRepository.getCurrentFilterParametersFlow() } returns stateFlowOfFilters
        justRun { searchRepository.addFilter(getNbBedroomsFilterWithOnlyMinParam()) }

        // WHEN
        toggleFilterUseCase.invoke(getNbBedroomsFilterQueryWithOnlyMinParam())

        // GIVEN
        verify {
            searchRepository.getCurrentFilterParametersFlow()
            searchRepository.addFilter(getNbBedroomsFilterWithOnlyMinParam())
        }

        confirmVerified(searchRepository)
    }

    @Test
    fun `nb bedrooms filter delete both filters parameters`() = testCoroutineRule.runTest {
        // GIVEN
        val stateFlowOfFilters = MutableStateFlow(getDefaultListFilters())
        every { searchRepository.getCurrentFilterParametersFlow() } returns stateFlowOfFilters

        // WHEN
        toggleFilterUseCase.invoke(getNbBedroomsFilterQueryWithNoneParam())

        // GIVEN
        verify {
            searchRepository.getCurrentFilterParametersFlow()
        }

        confirmVerified(searchRepository)
    }

    // NB BATHROOMS FILTER

    @Test
    fun `nb bathrooms filter add`() = testCoroutineRule.runTest {
        // GIVEN
        val stateFlowOfFilters = MutableStateFlow(listOf<Filter>())
        every { searchRepository.getCurrentFilterParametersFlow() } returns stateFlowOfFilters
        justRun { searchRepository.addFilter(getNbBathroomsFilterWithBothParams()) }

        // WHEN
        toggleFilterUseCase.invoke(getNbBathroomsFilterQueryWithBothParams())

        // GIVEN
        verify {
            searchRepository.getCurrentFilterParametersFlow()
            searchRepository.addFilter(getNbBathroomsFilterWithBothParams())
        }

        confirmVerified(searchRepository)
    }

    @Test
    fun `nb bathrooms filter update modify filters parameters`() = testCoroutineRule.runTest {
        // GIVEN
        val stateFlowOfFilters = MutableStateFlow(getDefaultListFilters())
        every { searchRepository.getCurrentFilterParametersFlow() } returns stateFlowOfFilters
        justRun { searchRepository.addFilter(getNbBathroomsFilterWithBothParams()) }

        // WHEN
        toggleFilterUseCase.invoke(getNbBathroomsFilterQueryWithBothParams())

        // GIVEN
        verify {
            searchRepository.getCurrentFilterParametersFlow()
            searchRepository.addFilter(getNbBathroomsFilterWithBothParams())
        }

        confirmVerified(searchRepository)
    }

    @Test
    fun `nb bathrooms filter update delete max filter parameters`() = testCoroutineRule.runTest {
        // GIVEN
        val stateFlowOfFilters = MutableStateFlow(getDefaultListFilters())
        every { searchRepository.getCurrentFilterParametersFlow() } returns stateFlowOfFilters
        justRun { searchRepository.addFilter(getNbBathroomsFilterWithOnlyMinParam()) }

        // WHEN
        toggleFilterUseCase.invoke(getNbBathroomsFilterQueryWithOnlyMinParam())

        // GIVEN
        verify {
            searchRepository.getCurrentFilterParametersFlow()
            searchRepository.addFilter(getNbBathroomsFilterWithOnlyMinParam())
        }

        confirmVerified(searchRepository)
    }

    @Test
    fun `nb bathrooms filter delete both filters parameters`() = testCoroutineRule.runTest {
        // GIVEN
        val stateFlowOfFilters = MutableStateFlow(getDefaultListFilters())
        every { searchRepository.getCurrentFilterParametersFlow() } returns stateFlowOfFilters

        // WHEN
        toggleFilterUseCase.invoke(getNbBathroomsFilterQueryWithNoneParam())

        // GIVEN
        verify {
            searchRepository.getCurrentFilterParametersFlow()
        }

        confirmVerified(searchRepository)
    }

    // ENTRY DATE FILTER

    @Test
    fun `entry date filter add`() = testCoroutineRule.runTest {
        // GIVEN
        val stateFlowOfFilters = MutableStateFlow(listOf<Filter>())
        every { searchRepository.getCurrentFilterParametersFlow() } returns stateFlowOfFilters
        justRun { searchRepository.addFilter(getEntryDateFilterWithBothParams()) }

        // WHEN
        toggleFilterUseCase.invoke(getEntryDateFilterQueryWithBothParams())

        // GIVEN
        verify {
            searchRepository.getCurrentFilterParametersFlow()
            searchRepository.addFilter(getEntryDateFilterWithBothParams())
        }

        confirmVerified(searchRepository)
    }

    @Test
    fun `entry date filter update modify filters parameters`() = testCoroutineRule.runTest {
        // GIVEN
        val stateFlowOfFilters = MutableStateFlow(getDefaultListFilters())
        every { searchRepository.getCurrentFilterParametersFlow() } returns stateFlowOfFilters
        justRun { searchRepository.addFilter(getEntryDateFilterWithBothParams()) }

        // WHEN
        toggleFilterUseCase.invoke(getEntryDateFilterQueryWithBothParams())

        // GIVEN
        verify {
            searchRepository.getCurrentFilterParametersFlow()
            searchRepository.addFilter(getEntryDateFilterWithBothParams())
        }

        confirmVerified(searchRepository)
    }

    @Test
    fun `entry date filter update delete max filter parameters`() = testCoroutineRule.runTest {
        // GIVEN
        val stateFlowOfFilters = MutableStateFlow(getDefaultListFilters())
        every { searchRepository.getCurrentFilterParametersFlow() } returns stateFlowOfFilters
        justRun { searchRepository.addFilter(getEntryDateFilterWithOnlyMinParam()) }

        // WHEN
        toggleFilterUseCase.invoke(getEntryDateFilterQueryWithOnlyMinParam())

        // GIVEN
        verify {
            searchRepository.getCurrentFilterParametersFlow()
            searchRepository.addFilter(getEntryDateFilterWithOnlyMinParam())
        }

        confirmVerified(searchRepository)
    }

    @Test
    fun `entry date filter delete both filters parameters`() = testCoroutineRule.runTest {
        // GIVEN
        val stateFlowOfFilters = MutableStateFlow(getDefaultListFilters())
        every { searchRepository.getCurrentFilterParametersFlow() } returns stateFlowOfFilters

        // WHEN
        toggleFilterUseCase.invoke(getEntryDateFilterQueryWithNoneParam())

        // GIVEN
        verify {
            searchRepository.getCurrentFilterParametersFlow()
        }

        confirmVerified(searchRepository)
    }

    // SALE DATE FILTER

    @Test
    fun `sale date filter add`() = testCoroutineRule.runTest {
        // GIVEN
        val stateFlowOfFilters = MutableStateFlow(listOf<Filter>())
        every { searchRepository.getCurrentFilterParametersFlow() } returns stateFlowOfFilters
        justRun { searchRepository.addFilter(getSaleDateFilterWithBothParams()) }

        // WHEN
        toggleFilterUseCase.invoke(getSaleDateFilterQueryWithBothParams())

        // GIVEN
        verify {
            searchRepository.getCurrentFilterParametersFlow()
            searchRepository.addFilter(getSaleDateFilterWithBothParams())
        }

        confirmVerified(searchRepository)
    }

    @Test
    fun `sale date filter update modify filters parameters`() = testCoroutineRule.runTest {
        // GIVEN
        val stateFlowOfFilters = MutableStateFlow(getDefaultListFilters())
        every { searchRepository.getCurrentFilterParametersFlow() } returns stateFlowOfFilters
        justRun { searchRepository.addFilter(getSaleDateFilterWithBothParams()) }

        // WHEN
        toggleFilterUseCase.invoke(getSaleDateFilterQueryWithBothParams())

        // GIVEN
        verify {
            searchRepository.getCurrentFilterParametersFlow()
            searchRepository.addFilter(getSaleDateFilterWithBothParams())
        }

        confirmVerified(searchRepository)
    }

    @Test
    fun `sale date filter update delete max filter parameters`() = testCoroutineRule.runTest {
        // GIVEN
        val stateFlowOfFilters = MutableStateFlow(getDefaultListFilters())
        every { searchRepository.getCurrentFilterParametersFlow() } returns stateFlowOfFilters
        justRun { searchRepository.addFilter(getSaleDateFilterWithOnlyMinParam()) }

        // WHEN
        toggleFilterUseCase.invoke(getSaleDateFilterQueryWithOnlyMinParam())

        // GIVEN
        verify {
            searchRepository.getCurrentFilterParametersFlow()
            searchRepository.addFilter(getSaleDateFilterWithOnlyMinParam())
        }

        confirmVerified(searchRepository)
    }

    @Test
    fun `sale date filter delete both filters parameters`() = testCoroutineRule.runTest {
        // GIVEN
        val stateFlowOfFilters = MutableStateFlow(getDefaultListFilters())
        every { searchRepository.getCurrentFilterParametersFlow() } returns stateFlowOfFilters

        // WHEN
        toggleFilterUseCase.invoke(getSaleDateFilterQueryWithNoneParam())

        // GIVEN
        verify {
            searchRepository.getCurrentFilterParametersFlow()
        }

        confirmVerified(searchRepository)
    }

    //region ============================================================ DEFAULT VALUES ============================================================

    private fun getDefaultListFilters(): List<Filter> {
        return listOf(
            getPriceFilterWithBothParams(), getLivingSpaceFilterWithBothParams(), getNbRoomsFilterWithBothParams(),
            getNbBedroomsFilterWithBothParams(), getNbBathroomsFilterWithBothParams(), getEntryDateFilterWithBothParams(),
            getSaleDateFilterWithBothParams()
        )
    }

    //region Living space
    private fun getLivingSpaceFilterWithBothParams(): Filter {
        return Filter.LivingSpaceFilter(defaultLivingSpaceFilterDoubleMin, defaultLivingSpaceFilterDoubleMax)
    }

    private fun getLivingSpaceFilterWithOnlyMinParam(): Filter {
        return Filter.LivingSpaceFilter(defaultLivingSpaceFilterDoubleMin, null)
    }

    private fun getLivingSpaceQueryWithBothParams(): FilterQuery {
        return FilterQuery.LivingSpaceFilter(defaultLivingSpaceFilterQueryMin, defaultLivingSpaceFilterQueryMax)
    }

    private fun getLivingSpaceFilterQueryWithOnlyMinParam(): FilterQuery {
        return FilterQuery.LivingSpaceFilter(defaultLivingSpaceFilterQueryMin, defaultFilterQueryDelete)
    }

    private fun getLivingSpaceQueryWithNoneParam(): FilterQuery {
        return FilterQuery.LivingSpaceFilter(defaultFilterQueryDelete, defaultFilterQueryDelete)
    }

    //endregion

    //region Price
    private fun getPriceFilterWithBothParams(): Filter {
        return Filter.PriceFilter(defaultPriceFilterDoubleMin, defaultPriceFilterDoubleMax)
    }

    private fun getPriceFilterWithOnlyMinParam(): Filter {
        return Filter.PriceFilter(defaultPriceFilterDoubleMin, null)
    }

    private fun getPriceFilterQueryWithBothParams(): FilterQuery {
        return FilterQuery.PriceFilter(defaultPriceFilterQueryMin, defaultPriceFilterQueryMax)
    }

    private fun getPriceFilterQueryWithOnlyMinParam(): FilterQuery {
        return FilterQuery.PriceFilter(defaultPriceFilterQueryMin, defaultFilterQueryDelete)
    }

    private fun getPriceFilterQueryWithNoneParam(): FilterQuery {
        return FilterQuery.PriceFilter(defaultFilterQueryDelete, defaultFilterQueryDelete)
    }

    //endregion

    //region Nb rooms
    private fun getNbRoomsFilterWithBothParams(): Filter {
        return Filter.NbRoomsFilter(defaultNbRoomsFilterIntMin, defaultNbRoomsFilterIntMax)
    }

    private fun getNbRoomsFilterWithOnlyMinParam(): Filter {
        return Filter.NbRoomsFilter(defaultNbRoomsFilterIntMin, null)
    }

    private fun getNbRoomsFilterQueryWithBothParams(): FilterQuery {
        return FilterQuery.NbRoomsFilter(defaultNbRoomsFilterQueryMin, defaultNbRoomsFilterQueryMax)
    }

    private fun getNbRoomsFilterQueryWithOnlyMinParam(): FilterQuery {
        return FilterQuery.NbRoomsFilter(defaultNbRoomsFilterQueryMin, defaultFilterQueryDelete)
    }

    private fun getNbRoomsFilterQueryWithNoneParam(): FilterQuery {
        return FilterQuery.NbRoomsFilter(defaultFilterQueryDelete, defaultFilterQueryDelete)
    }

    //endregion

    //region Nb bedrooms
    private fun getNbBedroomsFilterWithBothParams(): Filter {
        return Filter.NbBedroomsFilter(defaultNbBedroomsFilterIntMin, defaultNbBedroomsFilterIntMax)
    }

    private fun getNbBedroomsFilterWithOnlyMinParam(): Filter {
        return Filter.NbBedroomsFilter(defaultNbBedroomsFilterIntMin, null)
    }

    private fun getNbBedroomsFilterQueryWithBothParams(): FilterQuery {
        return FilterQuery.NbBedroomsFilter(defaultNbBedroomsFilterQueryMin, defaultNbBedroomsFilterQueryMax)
    }

    private fun getNbBedroomsFilterQueryWithOnlyMinParam(): FilterQuery {
        return FilterQuery.NbBedroomsFilter(defaultNbBedroomsFilterQueryMin, defaultFilterQueryDelete)
    }

    private fun getNbBedroomsFilterQueryWithNoneParam(): FilterQuery {
        return FilterQuery.NbBedroomsFilter(defaultFilterQueryDelete, defaultFilterQueryDelete)
    }

    //endregion

    //region Nb bathrooms
    private fun getNbBathroomsFilterWithBothParams(): Filter {
        return Filter.NbBathroomsFilter(defaultNbBathroomsFilterIntMin, defaultNbBathroomsFilterIntMax)
    }

    private fun getNbBathroomsFilterWithOnlyMinParam(): Filter {
        return Filter.NbBathroomsFilter(defaultNbBathroomsFilterIntMin, null)
    }

    private fun getNbBathroomsFilterQueryWithBothParams(): FilterQuery {
        return FilterQuery.NbBathroomsFilter(defaultNbBathroomsFilterQueryMin, defaultNbBathroomsFilterQueryMax)
    }

    private fun getNbBathroomsFilterQueryWithOnlyMinParam(): FilterQuery {
        return FilterQuery.NbBathroomsFilter(defaultNbBathroomsFilterQueryMin, defaultFilterQueryDelete)
    }

    private fun getNbBathroomsFilterQueryWithNoneParam(): FilterQuery {
        return FilterQuery.NbBathroomsFilter(defaultFilterQueryDelete, defaultFilterQueryDelete)
    }

    //endregion

    //region Entry date
    private fun getEntryDateFilterWithBothParams(): Filter {
        return Filter.EntryDateFilter(defaultEntryDateFilterMin, defaultEntryDateFilterMax)
    }

    private fun getEntryDateFilterWithOnlyMinParam(): Filter {
        return Filter.EntryDateFilter(defaultEntryDateFilterMin, null)
    }

    private fun getEntryDateFilterQueryWithBothParams(): FilterQuery {
        return FilterQuery.EntryDateFilter(defaultEntryDateFilterQueryMin, defaultEntryDateFilterQueryMax)
    }

    private fun getEntryDateFilterQueryWithOnlyMinParam(): FilterQuery {
        return FilterQuery.EntryDateFilter(defaultEntryDateFilterQueryMin, defaultFilterQueryDelete)
    }

    private fun getEntryDateFilterQueryWithNoneParam(): FilterQuery {
        return FilterQuery.EntryDateFilter(defaultFilterQueryDelete, defaultFilterQueryDelete)
    }

    //endregion

    //region Sale date
    private fun getSaleDateFilterWithBothParams(): Filter {
        return Filter.SaleDateFilter(defaultSaleDateFilterMin, defaultSaleDateFilterMax)
    }

    private fun getSaleDateFilterWithOnlyMinParam(): Filter {
        return Filter.SaleDateFilter(defaultSaleDateFilterMin, null)
    }

    private fun getSaleDateFilterQueryWithBothParams(): FilterQuery {
        return FilterQuery.SaleDateFilter(defaultSaleDateFilterQueryMin, defaultSaleDateFilterQueryMax)
    }

    private fun getSaleDateFilterQueryWithOnlyMinParam(): FilterQuery {
        return FilterQuery.SaleDateFilter(defaultSaleDateFilterQueryMin, defaultFilterQueryDelete)
    }

    private fun getSaleDateFilterQueryWithNoneParam(): FilterQuery {
        return FilterQuery.SaleDateFilter(defaultFilterQueryDelete, defaultFilterQueryDelete)
    }

    //endregion

    //endregion
}