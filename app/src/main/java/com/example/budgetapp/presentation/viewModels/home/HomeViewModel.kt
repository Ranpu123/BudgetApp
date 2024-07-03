package com.example.budgetapp.presentation.viewModels.home


import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.domain.models.expense.FixedExpense
import com.example.budgetapp.domain.models.income.FixedIncome
import com.example.budgetapp.domain.models.income.Income
import com.example.budgetapp.domain.models.transaction.FixedTransaction
import com.example.budgetapp.domain.repository_interfaces.IExpenseRepository
import com.example.budgetapp.domain.repository_interfaces.IFixedExpenseRepository
import com.example.budgetapp.domain.repository_interfaces.IFixedIncomeRepository
import com.example.budgetapp.domain.repository_interfaces.IIncomeRepository
import com.example.budgetapp.services.repository.fixed_expense.LocalFixedExpenseRepository
import com.example.budgetapp.services.repository.fixed_income.LocalFixedIncomeRepository
import com.example.budgetapp.utils.validDayofMonth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalTime
import kotlin.math.exp

class HomeViewModel(
    private val expenseRepository: IExpenseRepository,
    private val incomeRepository: IIncomeRepository,
    private val fixedExpenseRepository: IFixedExpenseRepository,
    private val fixedIncomeRepository: IFixedIncomeRepository,


    ): ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        startObserver()
    }

    fun startObserver(){
        viewModelScope.launch{
            combine(
                incomeRepository.fetchAll(),
                expenseRepository.fetchAll(),

            ){
                incomes, expenses ->

                HomeUiState(
                    expenses = expenses,
                    incomes = incomes,
                    balance = getTotalBalance(incomes, expenses),
                    incomeBalance = getIncomeBalance(incomes),
                    expenseBalance = getExpenseBalance(expenses),
                    userName = "Vinícius",
                )
            }.catch {
                e->
                _uiState.value = HomeUiState(errorMsg = e.message)
            }.collect{
                _uiState.value = it
            }
        }
    }
    private fun checkDueTransactions(fixedTransactions: List<FixedTransaction<*>>){
        fixedTransactions.forEach(){ transaction ->

            val delayedMonths = transaction.isDue()

            if(delayedMonths > 0){
                val curTime = LocalTime.now()
                for( i in 1..delayedMonths){

                    var date = transaction.lastDate
                        .plusMonths(1)
                        .withDayOfMonth(validDayofMonth(transaction.date.dayOfMonth, transaction.lastDate ));

                    when(transaction) {
                        is FixedExpense -> expenseRepository.addExpense(
                            Expense(
                                date = date.atTime(curTime),
                                value = transaction.value,
                                category = transaction.category,
                                description = transaction.description
                            )
                        )

                        is FixedIncome -> incomeRepository.addIncome(
                            Income(
                                date = date.atTime(curTime),
                                value = transaction.value,
                                category = transaction.category,
                                description = transaction.description
                            )
                        )
                    }
                    transaction.lastDate = date

                }
                when(transaction) {
                    is FixedExpense -> fixedExpenseRepository.updateFixedExpense(transaction)
                    is FixedIncome -> fixedIncomeRepository.updateFixedIncome(transaction)
                }
            }
        }
    }

    private fun getTotalBalance(incomes: List<Income>, expenses: List<Expense>): Double {
        var total: Double = 0.0
        total += getIncomeBalance(incomes)
        total += getExpenseBalance(expenses)
        return total
    }

    private fun getIncomeBalance(incomes: List<Income>): Double{
        var total: Double = 0.0
        total += incomes.sumOf { it.value }
        return total
    }

    private fun getExpenseBalance(expenses: List<Expense>): Double{
        var total: Double = 0.0
        total += expenses.sumOf { it.value }
        return total
    }
}