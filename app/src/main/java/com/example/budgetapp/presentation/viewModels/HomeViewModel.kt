package com.example.budgetapp.presentation.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.budgetapp.domain.models.ICategories
import com.example.budgetapp.domain.models.expense.EXPENSE_CATEGORIES
import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.domain.models.expense.FixedExpense
import com.example.budgetapp.domain.models.income.FixedIncome
import com.example.budgetapp.domain.models.income.Income
import com.example.budgetapp.domain.repository_interfaces.IExpenseRepository
import com.example.budgetapp.domain.repository_interfaces.IFixedExpenseRepository
import com.example.budgetapp.domain.repository_interfaces.IFixedIncomeRepository
import com.example.budgetapp.domain.repository_interfaces.IIncomeRepository
import com.example.budgetapp.services.repository.expense.LocalExpenseRepository
import com.example.budgetapp.services.repository.fixed_expense.LocalFixedExpenseRepository
import com.example.budgetapp.services.repository.fixed_income.LocalFixedIncomeRepository
import com.example.budgetapp.services.repository.income.LocalIncomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

class HomeViewModel: ViewModel() {

    private val expenseRepository: IExpenseRepository = LocalExpenseRepository
    private val incomeRepository: IIncomeRepository = LocalIncomeRepository

    private val fixedExpenseRepository: IFixedExpenseRepository = LocalFixedExpenseRepository
    private val fixedIncomeRepository: IFixedIncomeRepository = LocalFixedIncomeRepository

    val _expenses = MutableStateFlow(mutableListOf<Expense>())
    val _incomes = MutableStateFlow(mutableListOf<Income>())

    val expenses: StateFlow<MutableList<Expense>> get() = _expenses
    val incomes: StateFlow<MutableList<Income>> get() = _incomes

    val fixedIncomes = MutableStateFlow(mutableListOf<FixedIncome>())
    val fixedExpenses = MutableStateFlow(mutableListOf<FixedExpense>())

    //val balance

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    init {
        fetchData()
        updateState()
    }

    private fun fetchData(){
        _expenses.value = expenseRepository.fetchAll()
        _incomes.value = incomeRepository.fetchAll()
        fixedExpenses.value = fixedExpenseRepository.fetchAll()
        fixedIncomes.value = fixedIncomeRepository.fetchAll()
        _expenses.value.sortByDescending { it.date }
        _incomes.value.sortByDescending { it.date }
    }

    private fun updateState(){
        _uiState.value = HomeUiState(
            expenses = _expenses.value,
            incomes = _incomes.value,
            fixedExpense = fixedExpenses.value,
            fixedIncome = fixedIncomes.value,
            userName = "Vinícius",
            balance = getTotalBalance(),
            incomeBalance = getIncomeBalance(),
            expenseBalance = getExpenseBalance()
        )
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
        updateState()
    }
    private fun getTotalBalance(): Double {
        var total: Double = 0.0
        total += getIncomeBalance()
        total += getExpenseBalance()
        return total
    }

    private fun getIncomeBalance(): Double{
        var total: Double = 0.0
        total += incomes.value.sumOf { it.value }
        return total
    }

    private fun getExpenseBalance(): Double{
        var total: Double = 0.0
        total += expenses.value.sumOf { it.value }
        return total
    }
}