package com.suonk.oc_project9.domain.loan_simulator

import com.suonk.oc_project9.domain.LoanSimulatorRepository
import com.suonk.oc_project9.model.database.data.entities.loan_simulator.LoanSimulatorEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLoanSimulatorEntityFlowUseCase @Inject constructor(private val loanSimulatorRepository: LoanSimulatorRepository) {

    fun invoke(): Flow<LoanSimulatorEntity> {
        return loanSimulatorRepository.getLoanSimulatorEntityFlow()
    }
}