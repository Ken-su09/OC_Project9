package com.suonk.oc_project9.domain.loan_simulator.interest_rate

import com.suonk.oc_project9.domain.LoanSimulatorRepository
import java.math.BigDecimal
import javax.inject.Inject

class SetInterestRateUseCase @Inject constructor(private val loanSimulatorRepository: LoanSimulatorRepository) {

    fun invoke(interestValue: BigDecimal) {
        loanSimulatorRepository.setInterestRate(interestValue)
    }
}