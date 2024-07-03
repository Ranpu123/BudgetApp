package com.example.budgetapp.presentation.viewModels.home

import androidx.compose.runtime.mutableStateOf
import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.domain.models.expense.FixedExpense
import com.example.budgetapp.domain.models.income.FixedIncome
import com.example.budgetapp.domain.models.income.Income
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class HomeUiState(
    val expenses: List<Expense> = emptyList(),
    val incomes: List<Income> = emptyList(),
    val fixedExpense: List<FixedExpense> = emptyList(),
    val fixedIncome: List<FixedIncome> = emptyList(),
    val userName: String = "",
    val balance: Double = 0.0,
    val incomeBalance: Double = 0.0,
    val expenseBalance: Double = 0.0,
    val isLoading: Boolean = false,
    val errorMsg: String? = null
)