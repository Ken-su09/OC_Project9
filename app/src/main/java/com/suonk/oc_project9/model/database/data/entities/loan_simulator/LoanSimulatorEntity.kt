package com.suonk.oc_project9.model.database.data.entities.loan_simulator

import java.math.BigDecimal

data class LoanSimulatorEntity(
    val moneyLoan: BigDecimal,
    val monthLoan: Int,
    val interestRate: BigDecimal
    )