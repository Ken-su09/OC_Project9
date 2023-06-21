package com.suonk.oc_project9.model.database.data

import com.suonk.oc_project9.domain.LoanSimulatorRepository
import com.suonk.oc_project9.model.database.data.entities.loan_simulator.LoanSimulatorEntity
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton

@ViewModelScoped
class LoanSimulatorRepositoryImpl @Inject constructor() : LoanSimulatorRepository {

    private val loanSimulatorEntityFlow = MutableStateFlow(
        LoanSimulatorEntity(
            moneyLoan = BigDecimal(0.0),
            monthLoan = 0,
            interestRate = BigDecimal(0.0)
        )
    )

    override fun getLoanSimulatorEntityFlow(): StateFlow<LoanSimulatorEntity> = loanSimulatorEntityFlow

    override fun setMoneyLoan(value: BigDecimal) {
        loanSimulatorEntityFlow.update {
            it.copy(moneyLoan = value)
        }
    }

    override fun setMonthLoan(value: Int) {
        loanSimulatorEntityFlow.update {
            it.copy(monthLoan = value)
        }
    }

    override fun setInterestRate(value: BigDecimal) {
        loanSimulatorEntityFlow.update {
            it.copy(interestRate = value)
        }
    }
}