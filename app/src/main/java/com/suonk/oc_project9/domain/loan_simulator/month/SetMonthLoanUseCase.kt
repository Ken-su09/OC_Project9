package com.suonk.oc_project9.domain.loan_simulator.month

import com.suonk.oc_project9.domain.LoanSimulatorRepository
import javax.inject.Inject

class SetMonthLoanUseCase @Inject constructor(private val loanSimulatorRepository: LoanSimulatorRepository) {

    fun invoke(monthValue: Int) {
        loanSimulatorRepository.setMonthLoan(monthValue)
    }
}