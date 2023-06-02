package com.suonk.oc_project9.domain

import kotlinx.coroutines.flow.StateFlow
import java.math.BigDecimal

interface LoanSimulatorRepository {
    fun getMoneyValueFlow(): StateFlow<BigDecimal>
    fun setMoneyValueFlow(moneyValue: BigDecimal)

    fun getInterestRateValueFlow(): StateFlow<BigDecimal>
    fun setInterestRateValueFlow(interestRateValue: BigDecimal)

    fun getMonthValueFlow(): StateFlow<Int>
    fun setMonthValueFlow(monthValue: Int)
}