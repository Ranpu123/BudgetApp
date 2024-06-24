package com.example.budgetapp.presentation.viewModels.records

import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.domain.models.expense.FixedExpense
import com.example.budgetapp.domain.models.income.FixedIncome
import com.example.budgetapp.domain.models.income.Income

class RecordsUiState(
    val expenses: List<Expense> = listOf(),
    val incomes: List<Income> = listOf(),
    val fixedExpense: List<FixedExpense> = listOf(),
    val fixedIncome: List<FixedIncome> = listOf(),
)