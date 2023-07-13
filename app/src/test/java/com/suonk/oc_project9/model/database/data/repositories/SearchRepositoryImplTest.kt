package com.suonk.oc_project9.model.database.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.suonk.oc_project9.R
import com.suonk.oc_project9.ui.filter.Filter
import com.suonk.oc_project9.utils.TestCoroutineRule
import com.suonk.oc_project9.utils.sort.Sorting
import junit.framework.TestCase
import kotlinx.coroutines.test.runCurrent
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal

class SearchRepositoryImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val searchRepositoryImpl = SearchRepositoryImpl()

    private val defaultCurrentSearchParameter = "defaultCurrentSearchParameter"

    @Test
    fun `test get current filter parameters flow`() = testCoroutineRule.runTest {
        // WHEN
        searchRepositoryImpl.addFilter(getDefaultFilter1())
        searchRepositoryImpl.addFilter(getDefaultFilter2())
        searchRepositoryImpl.getCurrentFilterParametersFlow().test {
            // THEN
            TestCase.assertEquals(getDefaultFiltersList(), awaitItem())

            runCurrent()

            // WHEN II
            searchRepositoryImpl.removeFilter(getDefaultFilter2())

            // THEN II
            TestCase.assertEquals(getDefaultFiltersListAfterRemove(), awaitItem())

            runCurrent()

            // WHEN II
            searchRepositoryImpl.addFilter(getDefaultFilterAfterAdd())

            // THEN II
            TestCase.assertEquals(getDefaultFiltersListAfterAdd(), awaitItem())
        }
    }

    // Search

    @Test
    fun `test get and set current search parameters flow`() = testCoroutineRule.runTest {
        // GIVEN
        searchRepositoryImpl.setCurrentSearchParameters(defaultCurrentSearchParameter)

        // WHEN
        searchRepositoryImpl.getCurrentSearchParametersFlow().test {
            // THEN
            TestCase.assertEquals(defaultCurrentSearchParameter, awaitItem())
        }
    }

    //region Tests sort

    @Test
    fun `test get and set current sort parameters flow with date asc`() = testCoroutineRule.runTest {
        // GIVEN
        searchRepositoryImpl.setCurrentSortFilterParametersFlow(R.id.sort_by_date_asc)

        // WHEN
        searchRepositoryImpl.getCurrentSortParameterFlow().test {
            // THEN
            TestCase.assertEquals(Sorting.DATE_ASC, awaitItem())
        }
    }

    @Test
    fun `test get and set current sort parameters flow with date desc`() = testCoroutineRule.runTest {
        // GIVEN
        searchRepositoryImpl.setCurrentSortFilterParametersFlow(R.id.sort_by_date_desc)

        // WHEN
        searchRepositoryImpl.getCurrentSortParameterFlow().test {
            // THEN
            TestCase.assertEquals(Sorting.DATE_DESC, awaitItem())
        }
    }

    @Test
    fun `test get and set current sort parameters flow with price asc`() = testCoroutineRule.runTest {
        // GIVEN
        searchRepositoryImpl.setCurrentSortFilterParametersFlow(R.id.sort_by_price_asc)

        // WHEN
        searchRepositoryImpl.getCurrentSortParameterFlow().test {
            // THEN
            TestCase.assertEquals(Sorting.PRICE_ASC, awaitItem())
        }
    }

    @Test
    fun `test get and set current sort parameters flow with price desc`() = testCoroutineRule.runTest {
        // GIVEN
        searchRepositoryImpl.setCurrentSortFilterParametersFlow(R.id.sort_by_price_desc)

        // WHEN
        searchRepositoryImpl.getCurrentSortParameterFlow().test {
            // THEN
            TestCase.assertEquals(Sorting.PRICE_DESC, awaitItem())
        }
    }

    @Test
    fun `test get and set current sort parameters flow with living space asc`() = testCoroutineRule.runTest {
        // GIVEN
        searchRepositoryImpl.setCurrentSortFilterParametersFlow(R.id.sort_by_living_space_asc)

        // WHEN
        searchRepositoryImpl.getCurrentSortParameterFlow().test {
            // THEN
            TestCase.assertEquals(Sorting.LIVING_SPACE_ASC, awaitItem())
        }
    }

    @Test
    fun `test get and set current sort parameters flow with living space desc`() = testCoroutineRule.runTest {
        // GIVEN
        searchRepositoryImpl.setCurrentSortFilterParametersFlow(R.id.sort_by_living_space_desc)

        // WHEN
        searchRepositoryImpl.getCurrentSortParameterFlow().test {
            // THEN
            TestCase.assertEquals(Sorting.LIVING_SPACE_DESC, awaitItem())
        }
    }

    @Test
    fun `test get and set current sort parameters flow with rooms numbers asc`() = testCoroutineRule.runTest {
        // GIVEN
        searchRepositoryImpl.setCurrentSortFilterParametersFlow(R.id.sort_by_rooms_number_asc)

        // WHEN
        searchRepositoryImpl.getCurrentSortParameterFlow().test {
            // THEN
            TestCase.assertEquals(Sorting.ROOMS_NUMBER_ASC, awaitItem())
        }
    }

    @Test
    fun `test get and set current sort parameters flow with rooms numbers desc`() = testCoroutineRule.runTest {
        // GIVEN
        searchRepositoryImpl.setCurrentSortFilterParametersFlow(R.id.sort_by_rooms_number_desc)

        // WHEN
        searchRepositoryImpl.getCurrentSortParameterFlow().test {
            // THEN
            TestCase.assertEquals(Sorting.ROOMS_NUMBER_DESC, awaitItem())
        }
    }

    //endregion

    //region Tests filter

    @Test
    fun `test get and set current filter parameters flow with house`() = testCoroutineRule.runTest {
        // GIVEN
        searchRepositoryImpl.setCurrentSortFilterParametersFlow(R.id.house_filter)

        // WHEN
        searchRepositoryImpl.getCurrentSortFilterParametersFlow().test {
            // THEN
            TestCase.assertEquals(R.id.house_filter, awaitItem())
        }
    }

    @Test
    fun `test get and set current filter parameters flow with penthouse`() = testCoroutineRule.runTest {
        // GIVEN
        searchRepositoryImpl.setCurrentSortFilterParametersFlow(R.id.penthouse_filter)

        // WHEN
        searchRepositoryImpl.getCurrentSortFilterParametersFlow().test {
            // THEN
            TestCase.assertEquals(R.id.penthouse_filter, awaitItem())
        }
    }

    @Test
    fun `test get and set current filter parameters flow with duplex`() = testCoroutineRule.runTest {
        // GIVEN
        searchRepositoryImpl.setCurrentSortFilterParametersFlow(R.id.duplex_filter)

        // WHEN
        searchRepositoryImpl.getCurrentSortFilterParametersFlow().test {
            // THEN
            TestCase.assertEquals(R.id.duplex_filter, awaitItem())
        }
    }

    @Test
    fun `test get and set current filter parameters flow with flat`() = testCoroutineRule.runTest {
        // GIVEN
        searchRepositoryImpl.setCurrentSortFilterParametersFlow(R.id.flat_filter)

        // WHEN
        searchRepositoryImpl.getCurrentSortFilterParametersFlow().test {
            // THEN
            TestCase.assertEquals(R.id.flat_filter, awaitItem())
        }
    }

    @Test
    fun `test get and set current filter parameters flow with loft`() = testCoroutineRule.runTest {
        // GIVEN
        searchRepositoryImpl.setCurrentSortFilterParametersFlow(R.id.loft_filter)

        // WHEN
        searchRepositoryImpl.getCurrentSortFilterParametersFlow().test {
            // THEN
            TestCase.assertEquals(R.id.loft_filter, awaitItem())
        }
    }

    //endregion

    //region Others

    @Test
    fun `test get and set current filter parameters with remove filter`() = testCoroutineRule.runTest {
        // GIVEN
        searchRepositoryImpl.setCurrentSortFilterParametersFlow(R.id.remove_filter)

        // WHEN
        searchRepositoryImpl.getCurrentSortFilterParametersFlow().test {
            // THEN
            TestCase.assertEquals(R.id.remove_filter, awaitItem())
        }
    }

    @Test
    fun `test get and set current filter parameters with non sense filter`() = testCoroutineRule.runTest {
        // GIVEN
        searchRepositoryImpl.setCurrentSortFilterParametersFlow(-1)

        // WHEN
        searchRepositoryImpl.getCurrentSortFilterParametersFlow().test {
            // THEN
            TestCase.assertEquals(R.id.remove_filter, awaitItem())
        }
    }

    //endregion

    //region Default Values

    private fun getDefaultFiltersList(): List<Filter> {
        return listOf(getDefaultFilter1(), getDefaultFilter2())
    }

    private fun getDefaultFiltersListAfterAdd(): List<Filter> {
        return listOf(getDefaultFilter1(), getDefaultFilterAfterAdd())
    }

    private fun getDefaultFiltersListAfterRemove(): List<Filter> {
        return listOf(getDefaultFilter1())
    }

    private fun getDefaultFilter1(): Filter {
        return Filter.PriceFilter(BigDecimal(1000), null)
    }

    private fun getDefaultFilter2(): Filter {
        return Filter.NbRoomsFilter(4, 10)
    }

    private fun getDefaultFilterAfterAdd(): Filter {
        return Filter.NbRoomsFilter(8, null)
    }

    //endregion
}