package com.suonk.oc_project9.domain.loan_simulator.money

import com.suonk.oc_project9.domain.LoanSimulatorRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMoneyLoanUseCase @Inject constructor(private val loanSimulatorRepository: LoanSimulatorRepository) {

    fun invoke() = loanSimulatorRepository.getMoneyValueFlow()
}