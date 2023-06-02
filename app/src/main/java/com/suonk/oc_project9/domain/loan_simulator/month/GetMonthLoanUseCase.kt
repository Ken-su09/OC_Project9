package com.suonk.oc_project9.domain.loan_simulator.month

import com.suonk.oc_project9.domain.LoanSimulatorRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMonthLoanUseCase @Inject constructor(private val loanSimulatorRepository: LoanSimulatorRepository) {

    fun invoke() = loanSimulatorRepository.getMonthValueFlow()
}