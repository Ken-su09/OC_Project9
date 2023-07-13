package com.suonk.oc_project9.domain.loan_simulator.money

import com.suonk.oc_project9.domain.LoanSimulatorRepository
import io.mockk.confirmVerified
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import java.math.BigDecimal

class SetMoneyLoanUseCaseTest {

    private companion object {
        val defaultMoneyValue = BigDecimal(1000.0)
    }

    private val loanSimulatorRepository: LoanSimulatorRepository = mockk()

    private val setMoneyLoanUseCase = SetMoneyLoanUseCase(loanSimulatorRepository)

    @Test
    fun `test set money loan invoke`() {
        // GIVEN
        justRun { loanSimulatorRepository.setMoneyLoan(defaultMoneyValue) }

        // WHEN
        setMoneyLoanUseCase.invoke(defaultMoneyValue)

        verify { loanSimulatorRepository.setMoneyLoan(defaultMoneyValue) }
        confirmVerified(loanSimulatorRepository)
    }
}