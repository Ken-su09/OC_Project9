package com.suonk.oc_project9.ui.loan_simulator

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.suonk.oc_project9.R
import com.suonk.oc_project9.domain.loan_simulator.GetLoanSimulatorEntityFlowUseCase
import com.suonk.oc_project9.domain.loan_simulator.interest_rate.SetInterestRateUseCase
import com.suonk.oc_project9.domain.loan_simulator.money.SetMoneyLoanUseCase
import com.suonk.oc_project9.domain.loan_simulator.month.SetMonthLoanUseCase
import com.suonk.oc_project9.utils.CoroutineDispatcherProvider
import com.suonk.oc_project9.utils.NativeText
import com.suonk.oc_project9.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class LoanSimulatorViewModel @Inject constructor(
    private val getLoanSimulatorEntityFlowUseCase: GetLoanSimulatorEntityFlowUseCase,

    private val setMoneyLoanUseCase: SetMoneyLoanUseCase,
    private val setMonthLoanUseCase: SetMonthLoanUseCase,
    private val setInterestRateUseCase: SetInterestRateUseCase,
) : ViewModel() {

    val toastMessageSingleLiveEvent = SingleLiveEvent<NativeText>()

    val loanSimulatorViewStateLiveData: LiveData<LoanSimulatorViewState> = liveData {
        getLoanSimulatorEntityFlowUseCase.invoke().collect { loanSimulatorEntity ->
            val monthlyPayment = if (loanSimulatorEntity.moneyLoan != BigDecimal(0.0) && loanSimulatorEntity.monthLoan != 0) {
                calculateLoanPayment(
                    amount = loanSimulatorEntity.moneyLoan.toDouble(),
                    duration = loanSimulatorEntity.monthLoan,
                    interestRate = loanSimulatorEntity.interestRate.toDouble()
                ).setScale(2, RoundingMode.HALF_EVEN)
            } else {
                BigDecimal(0.0)
            }

            emit(
                LoanSimulatorViewState(
                    result = NativeText.Argument(R.string.string_arg_with_currency, monthlyPayment.toString())
                )
            )
        }
    }

    fun onMoneyValueValidation(moneyString: String?, interestRateString: String?, monthString: String?) {
        if (isEmptyOrBlank(moneyString) || isEmptyOrBlank(interestRateString) || isEmptyOrBlank(monthString)) {
            toastMessageSingleLiveEvent.setValue(NativeText.Resource(R.string.field_empty_toast_msg))
        } else if (isFieldsDigits(moneyString) || isFieldsDigits(interestRateString) || isFieldsDigits(monthString)) {
            toastMessageSingleLiveEvent.setValue(NativeText.Resource(R.string.fields_are_not_digits))
        } else if (isFieldsEqualsToZero(moneyString) || isFieldsEqualsToZero(interestRateString) || isFieldsEqualsToZero(monthString)) {
            toastMessageSingleLiveEvent.setValue(NativeText.Resource(R.string.fields_should_not_be_equal_to_zero))
        } else {
            val moneyValue = BigDecimal(moneyString?.toDoubleOrNull() ?: 0.0)
            val interestRateValue = BigDecimal(interestRateString?.toDoubleOrNull() ?: 0.0)
            val monthValue = monthString?.toIntOrNull() ?: 0

            setMoneyLoanUseCase.invoke(moneyValue)
            setInterestRateUseCase.invoke(interestRateValue)
            setMonthLoanUseCase.invoke(monthValue)
        }
    }

    private fun isEmptyOrBlank(value: String?): Boolean {
        return value == null || value.isEmpty() || value.isBlank() || value == "" || value == " "
    }

    private fun isFieldsDigits(value: String?): Boolean {
        return value == null || value.toDoubleOrNull() == null
    }

    private fun isFieldsEqualsToZero(value: String?): Boolean {
        return value == null || value.toDoubleOrNull() == 0.0 || value.toInt() == 0
    }

    private fun calculateLoanPayment(amount: Double, duration: Int, interestRate: Double): BigDecimal {
        val monthlyInterestRate = interestRate / 12.0 / 100.0

        return BigDecimal(amount) * BigDecimal(monthlyInterestRate) * (BigDecimal(1) + BigDecimal(monthlyInterestRate)).pow(
            BigDecimal(
                duration
            ).toInt()
        ) / ((BigDecimal(1) + BigDecimal(monthlyInterestRate)).pow(BigDecimal(duration).toInt()) - BigDecimal(1))
    }
}