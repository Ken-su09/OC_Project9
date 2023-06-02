package com.suonk.oc_project9.ui.loan_simulator

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.suonk.oc_project9.R
import com.suonk.oc_project9.domain.loan_simulator.interest_rate.GetInterestRateUseCase
import com.suonk.oc_project9.domain.loan_simulator.interest_rate.SetInterestRateUseCase
import com.suonk.oc_project9.domain.loan_simulator.money.GetMoneyLoanUseCase
import com.suonk.oc_project9.domain.loan_simulator.money.SetMoneyLoanUseCase
import com.suonk.oc_project9.domain.loan_simulator.month.GetMonthLoanUseCase
import com.suonk.oc_project9.domain.loan_simulator.month.SetMonthLoanUseCase
import com.suonk.oc_project9.utils.CoroutineDispatcherProvider
import com.suonk.oc_project9.utils.NativeText
import com.suonk.oc_project9.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class LoanSimulatorViewModel @Inject constructor(
    private val getMoneyLoanUseCase: GetMoneyLoanUseCase,
    private val getMonthLoanUseCase: GetMonthLoanUseCase,
    private val getInterestRateUseCase: GetInterestRateUseCase,

    private val setMoneyLoanUseCase: SetMoneyLoanUseCase,
    private val setMonthLoanUseCase: SetMonthLoanUseCase,
    private val setInterestRateUseCase: SetInterestRateUseCase,

    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : ViewModel() {

    val toastMessageSingleLiveEvent = SingleLiveEvent<NativeText>()

    val loanSimulatorViewStateLiveData: LiveData<LoanSimulatorViewState> = liveData(coroutineDispatcherProvider.io) {
        combine(
            getMoneyLoanUseCase.invoke(), getInterestRateUseCase.invoke(), getMonthLoanUseCase.invoke()
        ) { moneyLoan, interestRate, monthLoan ->

            var monthlyPayment = BigDecimal(0)
            var totalPayment = BigDecimal(0)
            var totalInterest = BigDecimal(0)

            if (moneyLoan != BigDecimal(0) && monthLoan != 0) {
                monthlyPayment = calculateMonthlyPayment(moneyLoan, interestRate, monthLoan).setScale(2, RoundingMode.HALF_EVEN)
                totalPayment = calculateTotalPayment(monthlyPayment, monthLoan).setScale(2, RoundingMode.HALF_EVEN)
                totalInterest = calculateTotalInterest(totalPayment, moneyLoan).setScale(2, RoundingMode.HALF_EVEN)
            }

            emit(
                LoanSimulatorViewState(
                    result = "$totalPayment $",
                    list = listOf(
                        PieChartDataViewState(
                            value = "${monthlyPayment.setScale(2, RoundingMode.HALF_EVEN)}",
                            title = "Monthly Payment",
                            color = Color.rgb(200, 200, 27)
                        ),
                        PieChartDataViewState(value = "$totalPayment", title = "Total Payment", color = Color.rgb(7, 125, 84)),
                        PieChartDataViewState(value = "$totalInterest", title = "Total Interest", color = Color.rgb(0, 153, 255)),
                    ),
                )
            )
        }.collect()
    }

    fun onMoneyValueValidation(moneyString: String?, interestRateString: String?, monthString: String?) {
        Log.i("FragContainerDetails", "moneyString : ${moneyString}")
        Log.i("FragContainerDetails", "interestRateString : ${interestRateString}")
        Log.i("FragContainerDetails", "monthString : ${monthString}")

        viewModelScope.launch(coroutineDispatcherProvider.default) {
            if (isEmptyOrBlank(moneyString) || isEmptyOrBlank(interestRateString) || isEmptyOrBlank(monthString)) {
                withContext(coroutineDispatcherProvider.main) {
                    toastMessageSingleLiveEvent.setValue(NativeText.Resource(R.string.field_empty_toast_msg))
                }
            } else if (isFieldsDigits(moneyString) || isFieldsDigits(interestRateString) || isFieldsDigits(monthString)) {
                withContext(coroutineDispatcherProvider.main) {
                    toastMessageSingleLiveEvent.setValue(NativeText.Resource(R.string.fields_are_not_digits))
                }
            } else {
                val moneyValue = BigDecimal(moneyString?.toDoubleOrNull() ?: 0.0)
                val interestRateValue = BigDecimal(moneyString?.toDoubleOrNull() ?: 0.0).divide(BigDecimal(100.0))
                val monthValue = monthString?.toIntOrNull() ?: 0

                setMoneyLoanUseCase.invoke(moneyValue)
                setInterestRateUseCase.invoke(interestRateValue)
                setMonthLoanUseCase.invoke(monthValue)
            }
        }
    }

    private fun isEmptyOrBlank(value: String?): Boolean {
        return value == null || value.isEmpty() || value.isBlank() || value == "" || value == " "
    }

    private fun isFieldsDigits(value: String?): Boolean {
        return value == null || value.toDoubleOrNull() == null
    }

    private fun calculateMonthlyPayment(loanAmount: BigDecimal, interestRate: BigDecimal, loanTerm: Int): BigDecimal {
        val monthlyInterestRate = interestRate.div(BigDecimal(12.0))
        val loanTermInMonths = loanTerm * 12
        val numerator = loanAmount * monthlyInterestRate * (monthlyInterestRate.plus(BigDecimal(1))).pow(loanTermInMonths)
        var denominator = (BigDecimal(1) + monthlyInterestRate).pow(loanTermInMonths) - BigDecimal(1)

        if (denominator == BigDecimal(0)) denominator = BigDecimal(1)

        return numerator / denominator
    }

    private fun calculateTotalPayment(monthlyPayment: BigDecimal, loanTerm: Int): BigDecimal {
        return monthlyPayment * BigDecimal(loanTerm) * BigDecimal(12)
    }

    private fun calculateTotalInterest(totalPayment: BigDecimal, loanAmount: BigDecimal): BigDecimal {
        return totalPayment - loanAmount
    }
}