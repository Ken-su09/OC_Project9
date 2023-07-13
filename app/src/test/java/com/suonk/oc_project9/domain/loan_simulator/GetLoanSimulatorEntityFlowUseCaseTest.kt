package com.suonk.oc_project9.domain.loan_simulator

import app.cash.turbine.test
import com.suonk.oc_project9.domain.LoanSimulatorRepository
import com.suonk.oc_project9.model.database.data.entities.loan_simulator.LoanSimulatorEntity
import com.suonk.oc_project9.utils.TestCoroutineRule
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal

class GetLoanSimulatorEntityFlowUseCaseTest {

    private companion object {
        private val defaultMoneyLoan = BigDecimal(1000.0)
        private const val defaultMonthLoan = 12
        private val defaultInterestRate = BigDecimal(12.0)
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val loanSimulatorRepository: LoanSimulatorRepository = mockk()

    private val getLoanSimulatorEntityFlowUseCase = GetLoanSimulatorEntityFlowUseCase(loanSimulatorRepository)

    @Test
    fun `test get loan simulator invoke`() = testCoroutineRule.runTest {
        // GIVEN
        val stateFlowLoanSimulatorEntity = MutableStateFlow(getDefaultLoanSimulatorEntity())
        every { loanSimulatorRepository.getLoanSimulatorEntityFlow() } returns stateFlowLoanSimulatorEntity

        // WHEN
        getLoanSimulatorEntityFlowUseCase.invoke().test {
            // THEN
            assertEquals(getDefaultLoanSimulatorEntity(), awaitItem())

            verify { loanSimulatorRepository.getLoanSimulatorEntityFlow() }

            confirmVerified(loanSimulatorRepository)
        }
    }

    private fun getDefaultLoanSimulatorEntity(): LoanSimulatorEntity {
        return LoanSimulatorEntity(defaultMoneyLoan, defaultMonthLoan, defaultInterestRate)
    }
}