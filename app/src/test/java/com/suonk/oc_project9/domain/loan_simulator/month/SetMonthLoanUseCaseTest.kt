package com.suonk.oc_project9.domain.loan_simulator.month

import com.suonk.oc_project9.domain.LoanSimulatorRepository
import io.mockk.confirmVerified
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class SetMonthLoanUseCaseTest {

    private companion object {
        const val defaultMonthValue = 12
    }

    private val loanSimulatorRepository: LoanSimulatorRepository = mockk()

    private val setMonthLoanUseCase = SetMonthLoanUseCase(loanSimulatorRepository)

    @Test
    fun `test set month loan invoke`() {
        // GIVEN
        justRun { loanSimulatorRepository.setMonthLoan(defaultMonthValue) }

        // WHEN
        setMonthLoanUseCase.invoke(defaultMonthValue)

        verify { loanSimulatorRepository.setMonthLoan(defaultMonthValue) }
        confirmVerified(loanSimulatorRepository)
    }
}