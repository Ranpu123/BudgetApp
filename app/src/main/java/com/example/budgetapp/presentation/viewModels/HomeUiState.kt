package com.example.budgetapp.presentation.viewModels

import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.domain.models.expense.FixedExpense
import com.example.budgetapp.domain.models.income.FixedIncome
import com.example.budgetapp.domain.models.income.Income

data class HomeUiState(
    val expenses: MutableList<Expense> = mutableListOf(),
    val incomes: MutableList<Income> = mutableListOf(),
    val fixedExpense: MutableList<FixedExpense> = mutableListOf(),
    val fixedIncome: MutableList<FixedIncome> = mutableListOf(),
    val userName: String = "",
    val balance: Double = 0.0,
    val incomeBalance: Double = 0.0,
    val expenseBalance: Double = 0.0,
)