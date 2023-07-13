package com.suonk.oc_project9.model.database.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.suonk.oc_project9.utils.TestCoroutineRule
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test

class CurrentRealEstateIdRepositoryImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val currentRealEstateIdRepositoryImpl = CurrentRealEstateIdRepositoryImpl()

    private val DEFAULT_LONG = 1L

    @Test
    fun `get real estate id flow without setting`() = testCoroutineRule.runTest {
        // GIVEN

        // WHEN
        currentRealEstateIdRepositoryImpl.getCurrentRealEstateIdFlow().test {
            // THEN
            TestCase.assertEquals(null, awaitItem())
        }
    }

    @Test
    fun `get real estate id flow with setting`() = testCoroutineRule.runTest {
        // GIVEN
        currentRealEstateIdRepositoryImpl.setCurrentRealEstateIdFlow(DEFAULT_LONG)

        // WHEN
        currentRealEstateIdRepositoryImpl.getCurrentRealEstateIdFlow().test {
            // THEN
            TestCase.assertEquals(DEFAULT_LONG, awaitItem())
        }
    }

//    @Test
//    fun `get real estate id channel with setting`() = testCoroutineRule.runTest {
//        // GIVEN
//        currentRealEstateIdRepositoryImpl.setCurrentRealEstateIdFlow(DEFAULT_LONG)
//
//        // WHEN
//        currentRealEstateIdRepositoryImpl.getCurrentRealEstateIdChannel().consumeEach {
//            TestCase.assertEquals(DEFAULT_LONG, it)
//        }
//    }
}