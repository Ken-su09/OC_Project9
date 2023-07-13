package com.suonk.oc_project9.domain.loan_simulator.interest_rate

import com.suonk.oc_project9.domain.LoanSimulatorRepository
import io.mockk.confirmVerified
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import java.math.BigDecimal

class SetInterestRateUseCaseTest {

    private companion object {
        val defaultInterestRateValue = BigDecimal(12.0)
    }

    private val loanSimulatorRepository: LoanSimulatorRepository = mockk()

    private val setInterestRateLoanUseCase = SetInterestRateUseCase(loanSimulatorRepository)

    @Test
    fun `test set interest rate loan invoke`() {
        // GIVEN
        justRun { loanSimulatorRepository.setInterestRate(defaultInterestRateValue) }

        // WHEN
        setInterestRateLoanUseCase.invoke(defaultInterestRateValue)

        verify { loanSimulatorRepository.setInterestRate(defaultInterestRateValue) }
        confirmVerified(loanSimulatorRepository)
    }
}