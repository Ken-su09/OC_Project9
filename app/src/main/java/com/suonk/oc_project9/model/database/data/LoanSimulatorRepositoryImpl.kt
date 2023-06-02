package com.suonk.oc_project9.model.database.data

import com.suonk.oc_project9.domain.LoanSimulatorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoanSimulatorRepositoryImpl @Inject constructor() : LoanSimulatorRepository {

    private val moneyValueFlow = MutableStateFlow(BigDecimal(0.0))
    private val interestRateValueFlow = MutableStateFlow(BigDecimal(0.0))
    private val monthValueFlow = MutableStateFlow(0)

    override fun getMoneyValueFlow(): StateFlow<BigDecimal> = moneyValueFlow

    override fun setMoneyValueFlow(moneyValue: BigDecimal) {
        moneyValueFlow.tryEmit(moneyValue)
    }

    override fun getInterestRateValueFlow(): StateFlow<BigDecimal> = interestRateValueFlow

    override fun setInterestRateValueFlow(interestRateValue: BigDecimal) {
        interestRateValueFlow.tryEmit(interestRateValue)
    }


    override fun getMonthValueFlow(): StateFlow<Int> = monthValueFlow

    override fun setMonthValueFlow(monthValue: Int) {
        monthValueFlow.tryEmit(monthValue)
    }
}