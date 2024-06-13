package com.example.budgetapp.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.budgetapp.domain.models.expense.EXPENSE_CATEGORIES
import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.domain.models.expense.FixedExpense
import com.example.budgetapp.domain.models.income.FixedIncome
import com.example.budgetapp.domain.models.income.INCOME_CATEGORIES
import com.example.budgetapp.domain.models.income.Income
import com.example.budgetapp.domain.models.transaction.FixedTransaction
import com.example.budgetapp.domain.repository_interfaces.IExpenseRepository
import com.example.budgetapp.domain.repository_interfaces.IFixedExpenseRepository
import com.example.budgetapp.domain.repository_interfaces.IFixedIncomeRepository
import com.example.budgetapp.domain.repository_interfaces.IIncomeRepository
import com.example.budgetapp.domain.use_cases.ValidateTransactionDescription
import com.example.budgetapp.domain.use_cases.ValidateTransactionValue
import com.example.budgetapp.services.repository.expense.LocalExpenseRepository
import com.example.budgetapp.services.repository.fixed_expense.LocalFixedExpenseRepository
import com.example.budgetapp.services.repository.fixed_income.LocalFixedIncomeRepository
import com.example.budgetapp.services.repository.income.LocalIncomeRepository
import com.example.budgetapp.utils.validDayofMonth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class HomeViewModel(
    private val expenseRepository: IExpenseRepository = LocalExpenseRepository,
    private val incomeRepository: IIncomeRepository = LocalIncomeRepository,
    private val fixedExpenseRepository: IFixedExpenseRepository = LocalFixedExpenseRepository,
    private val fixedIncomeRepository: IFixedIncomeRepository = LocalFixedIncomeRepository,


    ): ViewModel() {

    private var _expenses = mutableListOf<Expense>()
    private var _incomes = mutableListOf<Income>()

    private var _fixedIncomes = mutableListOf<FixedIncome>()
    private var _fixedExpenses = mutableListOf<FixedExpense>()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()


    init {
        updateAll()
    }

    private fun fetchData(){
        _expenses = expenseRepository.fetchAll()
        _incomes = incomeRepository.fetchAll()
        _fixedExpenses = fixedExpenseRepository.fetchAll()
        _fixedIncomes = fixedIncomeRepository.fetchAll()
    }

    private fun updateHomeState(){
        _uiState.value = HomeUiState(
            expenses = _expenses,
            incomes = _incomes,
            fixedExpense = _fixedExpenses,
            fixedIncome = _fixedIncomes,
            userName = "Vinícius",
            balance = getTotalBalance(),
            incomeBalance = getIncomeBalance(),
            expenseBalance = getExpenseBalance()
        )
    }

    fun updateAll(){
        fetchData()
        checkDueTransactions(_fixedExpenses)
        checkDueTransactions(_fixedIncomes)
        fetchData()
        updateHomeState()
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

    fun addNewExpense(
        date: LocalDate,
        value: Double,
        category: EXPENSE_CATEGORIES,
        description: String,
    ){
        expenseRepository.addExpense(Expense(
            date = date.atTime(LocalTime.now()),
            value = if(value > 0.0) -value else value,
            category = category,
            description = description
        ))
        fetchData()
        updateHomeState()
    }

    fun addNewIncome(
        date: LocalDate,
        value: Double,
        category: INCOME_CATEGORIES,
        description: String,
    ){
        incomeRepository.addIncome(Income(
            date = date.atTime(LocalTime.now()),
            value = if(value < 0.0) +value else value,
            category = category,
            description = description
        ))
        fetchData()
        updateHomeState()
    }

    private fun getTotalBalance(): Double {
        var total: Double = 0.0
        total += getIncomeBalance()
        total += getExpenseBalance()
        return total
    }

    private fun getIncomeBalance(): Double{
        var total: Double = 0.0
        total += _incomes.sumOf { it.value }
        return total
    }

    private fun getExpenseBalance(): Double{
        var total: Double = 0.0
        total += _expenses.sumOf { it.value }
        return total
    }
}