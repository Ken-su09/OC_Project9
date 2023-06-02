package com.suonk.oc_project9.domain.loan_simulator.interest_rate

import com.suonk.oc_project9.domain.LoanSimulatorRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetInterestRateUseCase @Inject constructor(private val loanSimulatorRepository: LoanSimulatorRepository) {

    fun invoke() = loanSimulatorRepository.getInterestRateValueFlow()
}