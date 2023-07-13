package com.suonk.oc_project9.model.database.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.suonk.oc_project9.model.database.data.entities.loan_simulator.LoanSimulatorEntity
import com.suonk.oc_project9.utils.TestCoroutineRule
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal

class LoanSimulatorRepositoryImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val loanSimulatorRepositoryImpl = LoanSimulatorRepositoryImpl()

    private val defaultMoneyLoan = BigDecimal(1000)
    private val defaultMonthLoan = 12
    private val defaultInterestRate = BigDecimal(12)

    @Test
    fun `get loan simulator entity flow without setting`() = testCoroutineRule.runTest {
        // GIVEN

        // WHEN
        loanSimulatorRepositoryImpl.getLoanSimulatorEntityFlow().test {
            // THEN
            TestCase.assertEquals(getDefaultLoanSimulatorEmpty(), awaitItem())
        }
    }

    @Test
    fun `get loan simulator entity with setting`() = testCoroutineRule.runTest {
        // GIVEN
        loanSimulatorRepositoryImpl.setMoneyLoan(defaultMoneyLoan)
        loanSimulatorRepositoryImpl.setMonthLoan(defaultMonthLoan)
        loanSimulatorRepositoryImpl.setInterestRate(defaultInterestRate)

        // WHEN
        loanSimulatorRepositoryImpl.getLoanSimulatorEntityFlow().test {
            // THEN
            TestCase.assertEquals(getDefaultLoanSimulator(), awaitItem())
        }
    }

    private fun getDefaultLoanSimulator(): LoanSimulatorEntity {
        return LoanSimulatorEntity(moneyLoan = defaultMoneyLoan, monthLoan = defaultMonthLoan, interestRate = defaultInterestRate)
    }

    private fun getDefaultLoanSimulatorEmpty(): LoanSimulatorEntity {
        return LoanSimulatorEntity(moneyLoan = BigDecimal(0.0), monthLoan = 0, interestRate = BigDecimal(0.0))
    }
}