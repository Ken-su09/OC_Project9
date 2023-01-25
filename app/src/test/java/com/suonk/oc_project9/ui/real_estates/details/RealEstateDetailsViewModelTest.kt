package com.suonk.oc_project9.ui.real_estates.details

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.content.Context
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.suonk.oc_project9.domain.real_estate.GetPositionFromFullAddressUseCase
import com.suonk.oc_project9.domain.real_estate.GetRealEstateFlowByIdUseCase
import com.suonk.oc_project9.domain.real_estate.UpsertNewRealEstateUseCase
import com.suonk.oc_project9.domain.real_estate.UpdateRealEstateUseCase
import com.suonk.oc_project9.utils.*
import com.suonk.oc_project9.utils.Defaults.getDefaultRealEstateDetailsViewState
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RealEstateDetailsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val insertNewRealEstateUseCase: UpsertNewRealEstateUseCase = mockk()
    private val getRealEstateFlowByIdUseCase: GetRealEstateFlowByIdUseCase = mockk()
    private val getPositionFromFullAddressUseCase: GetPositionFromFullAddressUseCase = mockk()
    private val updateRealEstateUseCase: UpdateRealEstateUseCase = mockk()

    private val context: Context = mockk()
    private val navArgProducer: NavArgProducer = mockk()

    private val realEstateDetailsViewModel = RealEstateDetailsViewModel(
        insertNewRealEstateUseCase = insertNewRealEstateUseCase,
        getRealEstateFlowByIdUseCase = getRealEstateFlowByIdUseCase,
        getPositionFromFullAddressUseCase = getPositionFromFullAddressUseCase,
        updateRealEstateUseCase = updateRealEstateUseCase,
        coroutineDispatcherProvider = testCoroutineRule.getTestCoroutineDispatcherProvider(),
        context = context,
        navArgProducer = navArgProducer
    )

    @Before
    fun setup() {
        justRun {


        }
        coJustRun { insertNewRealEstateUseCase.invoke(any(), arrayListOf()) }
        coJustRun { updateRealEstateUseCase.invoke(any(), arrayListOf()) }
        every {
            getRealEstateFlowByIdUseCase.invoke(
                navArgProducer.getNavArgs(
                    RealEstateDetailsFragmentArgs::class
                ).id
            )
        }
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // WHEN
        realEstateDetailsViewModel.realEstateDetailsViewStateLiveData.observeForTesting(this) {

            // THEN
            assertThat(it.value).isEqualTo(getDefaultRealEstateDetailsViewState())
        }
    }
}