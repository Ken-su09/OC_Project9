package com.suonk.oc_project9.domain

import com.suonk.oc_project9.model.database.data.entities.loan_simulator.LoanSimulatorEntity
import kotlinx.coroutines.flow.StateFlow
import java.math.BigDecimal

interface LoanSimulatorRepository {
    fun getLoanSimulatorEntityFlow(): StateFlow<LoanSimulatorEntity>

    fun setMoneyLoan(value: BigDecimal)
    fun setMonthLoan(value: Int)
    fun setInterestRate(value: BigDecimal)
}