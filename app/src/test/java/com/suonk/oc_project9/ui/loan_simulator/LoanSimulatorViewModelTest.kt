package com.suonk.oc_project9.ui.loan_simulator

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.suonk.oc_project9.R
import com.suonk.oc_project9.domain.loan_simulator.GetLoanSimulatorEntityFlowUseCase
import com.suonk.oc_project9.domain.loan_simulator.interest_rate.SetInterestRateUseCase
import com.suonk.oc_project9.domain.loan_simulator.money.SetMoneyLoanUseCase
import com.suonk.oc_project9.domain.loan_simulator.month.SetMonthLoanUseCase
import com.suonk.oc_project9.model.database.data.entities.loan_simulator.LoanSimulatorEntity
import com.suonk.oc_project9.utils.*
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal

class LoanSimulatorViewModelTest {

    companion object {
        val GET_MONEY_LOAN_DEFAULT_RETURN = BigDecimal(0.0)
        const val GET_MONTH_LOAN_DEFAULT_RETURN = 0
        val GET_INTEREST_RATE_DEFAULT_RETURN = BigDecimal(0.0)

        // Monthly Payment
        val MONTHLY_PAYMENT_VALUE = "0"

        // Total Payment
        val TOTAL_PAYMENT_RESULT = BigDecimal(0)

        // Total Interest
        val TOTAL_INTEREST_RESULT = BigDecimal(0.0)

        const val MONEY_INPUT_STRING = "10000"
        val MONEY_INPUT_VALUE = BigDecimal(10000)
        const val INTEREST_INPUT_RATE_STRING = "5"
        val INTEREST_INPUT_RATE = BigDecimal(5)
        const val MONTH_INPUT_STRING = "60"
        const val MONTH_INPUT_INT = 60
//        val MONTHLY_PAYMENT_VALUE_AFTER_CALCULATION = BigDecimal(188.71)

        //        const val MONTHLY_PAYMENT_VALUE_AFTER_CALCULATION = 188.71
        const val MONTHLY_PAYMENT_STRING_AFTER_CALCULATION = "188.71"

        const val NOT_DIGIT_MONEY_STRING = "1000d1a2"
        const val NOT_DIGIT_INTEREST_RATE_STRING = "1000d1a2"
        const val NOT_DIGIT_MONTH_STRING = "1000d1a2"
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val getLoanSimulatorEntityFlowUseCase: GetLoanSimulatorEntityFlowUseCase = mockk()

    private val setMoneyLoanUseCase: SetMoneyLoanUseCase = mockk()
    private val setMonthLoanUseCase: SetMonthLoanUseCase = mockk()
    private val setInterestRateUseCase: SetInterestRateUseCase = mockk()

    private lateinit var loanSimulatorViewModel: LoanSimulatorViewModel

    @Before
    fun setup() {
        every { getLoanSimulatorEntityFlowUseCase.invoke() } returns flowOf(getDefaultLoanSimulatorEntity())

        loanSimulatorViewModel = LoanSimulatorViewModel(
            getLoanSimulatorEntityFlowUseCase = getLoanSimulatorEntityFlowUseCase,
            setMoneyLoanUseCase = setMoneyLoanUseCase,
            setMonthLoanUseCase = setMonthLoanUseCase,
            setInterestRateUseCase = setInterestRateUseCase,
        )
    }

    @Test
    fun `initial case`() = testCoroutineRule.runTest {
        // WHEN
        loanSimulatorViewModel.loanSimulatorViewStateLiveData.observeForTesting(this) {

            // THEN
            assertThat(it.value).isEqualTo(getDefaultLoanSimulatorViewState())

            coVerify {
                getLoanSimulatorEntityFlowUseCase.invoke()
            }
            confirmVerified(
                getLoanSimulatorEntityFlowUseCase, setMoneyLoanUseCase, setMonthLoanUseCase, setInterestRateUseCase
            )
        }
    }

    @Test
    fun `calculate loan payment per month`() = testCoroutineRule.runTest {
        // GIVEN
        justRun { setMoneyLoanUseCase.invoke(MONEY_INPUT_VALUE) }
        justRun { setMonthLoanUseCase.invoke(MONTH_INPUT_INT) }
        justRun { setInterestRateUseCase.invoke(INTEREST_INPUT_RATE) }

        every { getLoanSimulatorEntityFlowUseCase.invoke() } returns flowOf(getDefaultLoanSimulatorEntityAfterCalculation())

        loanSimulatorViewModel.onMoneyValueValidation(MONEY_INPUT_STRING, INTEREST_INPUT_RATE_STRING, MONTH_INPUT_STRING)

        // WHEN
        loanSimulatorViewModel.loanSimulatorViewStateLiveData.observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(getDefaultLoanSimulatorViewStateAfterCalculate())

            coVerify {
                setMoneyLoanUseCase.invoke(MONEY_INPUT_VALUE)
                setMonthLoanUseCase.invoke(MONTH_INPUT_INT)
                setInterestRateUseCase.invoke(INTEREST_INPUT_RATE)

                getLoanSimulatorEntityFlowUseCase.invoke()
            }
            confirmVerified(
                getLoanSimulatorEntityFlowUseCase, setMoneyLoanUseCase, setMonthLoanUseCase, setInterestRateUseCase
            )
        }
    }

    // EMPTY DATA

    @Test
    fun `try to calculate with all data empty`() = testCoroutineRule.runTest {
        // GIVEN
        loanSimulatorViewModel.onMoneyValueValidation("", "", "")

        // WHEN
        loanSimulatorViewModel.loanSimulatorViewStateLiveData.observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(getDefaultLoanSimulatorViewState())

            coVerify {
                getLoanSimulatorEntityFlowUseCase.invoke()
            }
            confirmVerified(
                getLoanSimulatorEntityFlowUseCase, setMoneyLoanUseCase, setMonthLoanUseCase, setInterestRateUseCase
            )
        }
    }

    @Test
    fun `try to calculate with money only empty`() = testCoroutineRule.runTest {
        // GIVEN
        loanSimulatorViewModel.onMoneyValueValidation("", INTEREST_INPUT_RATE_STRING, MONTH_INPUT_STRING)

        // WHEN
        loanSimulatorViewModel.loanSimulatorViewStateLiveData.observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(getDefaultLoanSimulatorViewState())

            coVerify {
                getLoanSimulatorEntityFlowUseCase.invoke()
            }
            confirmVerified(
                getLoanSimulatorEntityFlowUseCase, setMoneyLoanUseCase, setMonthLoanUseCase, setInterestRateUseCase
            )
        }
    }

    @Test
    fun `try to calculate with month only empty`() = testCoroutineRule.runTest {
        // GIVEN
        loanSimulatorViewModel.onMoneyValueValidation(MONEY_INPUT_STRING, INTEREST_INPUT_RATE_STRING, "")

        // WHEN
        loanSimulatorViewModel.loanSimulatorViewStateLiveData.observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(getDefaultLoanSimulatorViewState())

            coVerify {
                getLoanSimulatorEntityFlowUseCase.invoke()
            }
            confirmVerified(
                getLoanSimulatorEntityFlowUseCase, setMoneyLoanUseCase, setMonthLoanUseCase, setInterestRateUseCase
            )
        }
    }

    @Test
    fun `try to calculate with interest only empty`() = testCoroutineRule.runTest {
        // GIVEN
        loanSimulatorViewModel.onMoneyValueValidation(MONEY_INPUT_STRING, "", MONTH_INPUT_STRING)

        // WHEN
        loanSimulatorViewModel.loanSimulatorViewStateLiveData.observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(getDefaultLoanSimulatorViewState())

            coVerify {
                getLoanSimulatorEntityFlowUseCase.invoke()
            }
            confirmVerified(
                getLoanSimulatorEntityFlowUseCase, setMoneyLoanUseCase, setMonthLoanUseCase, setInterestRateUseCase
            )
        }
    }

    // NON DIGIT

    @Test
    fun `try to calculate with all data with non digit content`() = testCoroutineRule.runTest {
        // GIVEN
        loanSimulatorViewModel.onMoneyValueValidation(NOT_DIGIT_MONEY_STRING, NOT_DIGIT_INTEREST_RATE_STRING, NOT_DIGIT_MONTH_STRING)

        // WHEN
        loanSimulatorViewModel.loanSimulatorViewStateLiveData.observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(getDefaultLoanSimulatorViewState())

            coVerify {
                getLoanSimulatorEntityFlowUseCase.invoke()
            }
            confirmVerified(
                getLoanSimulatorEntityFlowUseCase, setMoneyLoanUseCase, setMonthLoanUseCase, setInterestRateUseCase
            )
        }
    }

    @Test
    fun `try to calculate with money data only with non digit content`() = testCoroutineRule.runTest {
        // GIVEN

        loanSimulatorViewModel.onMoneyValueValidation(NOT_DIGIT_MONEY_STRING, INTEREST_INPUT_RATE_STRING, MONTH_INPUT_STRING)

        // WHEN
        loanSimulatorViewModel.loanSimulatorViewStateLiveData.observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(getDefaultLoanSimulatorViewState())

            coVerify {
                getLoanSimulatorEntityFlowUseCase.invoke()
            }
            confirmVerified(
                getLoanSimulatorEntityFlowUseCase, setMoneyLoanUseCase, setMonthLoanUseCase, setInterestRateUseCase
            )
        }
    }

    @Test
    fun `try to calculate with month only data only with non digit content`() = testCoroutineRule.runTest {
        // GIVEN
        loanSimulatorViewModel.onMoneyValueValidation(MONEY_INPUT_STRING, INTEREST_INPUT_RATE_STRING, NOT_DIGIT_MONTH_STRING)

        // WHEN
        loanSimulatorViewModel.loanSimulatorViewStateLiveData.observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(getDefaultLoanSimulatorViewState())

            coVerify {
                getLoanSimulatorEntityFlowUseCase.invoke()
            }
            confirmVerified(
                getLoanSimulatorEntityFlowUseCase, setMoneyLoanUseCase, setMonthLoanUseCase, setInterestRateUseCase
            )
        }
    }

    @Test
    fun `try to calculate with interest rate only data only with non digit content`() = testCoroutineRule.runTest {
        // GIVEN

        loanSimulatorViewModel.onMoneyValueValidation(MONEY_INPUT_STRING, NOT_DIGIT_INTEREST_RATE_STRING, MONTH_INPUT_STRING)

        // WHEN
        loanSimulatorViewModel.loanSimulatorViewStateLiveData.observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(getDefaultLoanSimulatorViewState())

            coVerify {
                getLoanSimulatorEntityFlowUseCase.invoke()

            }
            confirmVerified(
                getLoanSimulatorEntityFlowUseCase, setMoneyLoanUseCase, setMonthLoanUseCase, setInterestRateUseCase
            )
        }
    }

    // NULL DATA

    @Test
    fun `try to calculate with all NULL data`() = testCoroutineRule.runTest {
        // GIVEN

        loanSimulatorViewModel.onMoneyValueValidation(null, null, null)

        // WHEN
        loanSimulatorViewModel.loanSimulatorViewStateLiveData.observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(getDefaultLoanSimulatorViewState())

            coVerify {
                getLoanSimulatorEntityFlowUseCase.invoke()
            }
            confirmVerified(
                getLoanSimulatorEntityFlowUseCase, setMoneyLoanUseCase, setMonthLoanUseCase, setInterestRateUseCase
            )
        }
    }

    @Test
    fun `try to calculate with all data equals to 0`() = testCoroutineRule.runTest {
        // GIVEN
        loanSimulatorViewModel.onMoneyValueValidation("0.0", "0.0", "0")

        // WHEN
        loanSimulatorViewModel.loanSimulatorViewStateLiveData.observeForTesting(this) {
            // THEN
            assertThat(it.value).isEqualTo(getDefaultLoanSimulatorViewState())

            coVerify {
                getLoanSimulatorEntityFlowUseCase.invoke()
            }
            confirmVerified(
                getLoanSimulatorEntityFlowUseCase, setMoneyLoanUseCase, setMonthLoanUseCase, setInterestRateUseCase
            )
        }
    }

    //region DEFAULT VALUES

    private fun getDefaultLoanSimulatorEntity(): LoanSimulatorEntity {
        return LoanSimulatorEntity(
            moneyLoan = GET_MONEY_LOAN_DEFAULT_RETURN,
            monthLoan = GET_MONTH_LOAN_DEFAULT_RETURN,
            interestRate = GET_INTEREST_RATE_DEFAULT_RETURN
        )
    }

    // TODO : Default Values
    private fun getDefaultLoanSimulatorEntityAfterCalculation(): LoanSimulatorEntity {
        return LoanSimulatorEntity(
            moneyLoan = BigDecimal(10000.0), monthLoan = 60, interestRate = BigDecimal(5.0)
        )
    }

    private fun getDefaultLoanSimulatorViewState(): LoanSimulatorViewState {
        return LoanSimulatorViewState(result = NativeText.Argument(R.string.string_arg_with_currency, MONTHLY_PAYMENT_VALUE))
    }

    private fun getDefaultLoanSimulatorViewStateAfterCalculate(): LoanSimulatorViewState {
        return LoanSimulatorViewState(
            result = NativeText.Argument(
                R.string.string_arg_with_currency, MONTHLY_PAYMENT_STRING_AFTER_CALCULATION
            )
        )
    }

    //endregion
}