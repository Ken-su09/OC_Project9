package com.suonk.oc_project9.domain.loan_simulator.money

import com.suonk.oc_project9.domain.LoanSimulatorRepository
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SetMoneyLoanUseCase @Inject constructor(private val loanSimulatorRepository: LoanSimulatorRepository) {

    fun invoke(moneyValue: BigDecimal) {
        loanSimulatorRepository.setMoneyValueFlow(moneyValue = moneyValue)
    }
}