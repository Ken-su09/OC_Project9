package com.suonk.oc_project9.ui.real_estates.list

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.content.Context
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.suonk.oc_project9.R
import com.suonk.oc_project9.domain.real_estate.GetAllRealEstatesUseCase
import com.suonk.oc_project9.model.database.data.entities.RealEstateEntityWithPhotos
import com.suonk.oc_project9.utils.*
import com.suonk.oc_project9.utils.Defaults.getAllDefaultRealEstatesWithPhotos
import com.suonk.oc_project9.utils.sort.Sorting
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RealEstatesListViewModelTest {

    companion object {
        private val REAL_ESTATE_WITH_PHOTOS_FLOW: Flow<List<RealEstateEntityWithPhotos>> = flowOf()
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val getAllRealEstatesUseCase: GetAllRealEstatesUseCase = mockk()
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider = mockk()
    private val context: Context = mockk()

    private val realEstatesListViewModel = RealEstatesListViewModel(
        getAllRealEstatesUseCase, coroutineDispatcherProvider, context
    )

    @Before
    fun setup() {
        justRun {


        }
        coJustRun {}
        every {
            every { getAllRealEstatesUseCase.invoke() } returns flowOf(getAllDefaultRealEstatesWithPhotos())
        }
    }

    @Test
    fun `initial case`() = testCoroutineRule.runTest {
        // GIVEN
        every {
            every { getAllRealEstatesUseCase.invoke() } returns flowOf(arrayListOf())
        }

        // WHEN
        realEstatesListViewModel.realEstateLiveData.observeForTesting(this) {

            // THEN
            assertThat(it.value).isEqualTo(arrayListOf())
            assertThat(it.value?.size).isEqualTo(0)
        }
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // WHEN
        realEstatesListViewModel.realEstateLiveData.observeForTesting(this) {

            // THEN
            assertThat(it.value).isEqualTo(getAllDefaultRealEstatesWithPhotos())
        }
    }

    @Test
    fun `sort by ascending price`() = testCoroutineRule.runTest {
        // WHEN
        realEstatesListViewModel.realEstateLiveData.observeForTesting(this) {

            realEstatesListViewModel.setMutableState(R.id.sort_by_date_asc)

            // THEN
            assertThat(it.value).isEqualTo(getAllDefaultRealEstatesWithPhotos())
        }
    }
}