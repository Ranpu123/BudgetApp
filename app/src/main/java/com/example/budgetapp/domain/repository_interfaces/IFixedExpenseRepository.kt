package com.example.budgetapp.domain.repository_interfaces

import com.example.budgetapp.domain.models.expense.FixedExpense

interface IFixedExpenseRepository {
    fun fetchAll():MutableList<FixedExpense>
    fun addFixedExpense(fixedExpense: FixedExpense)
    fun removeFixedExpense(fixedExpense: FixedExpense)
    fun updateFixedExpense(fixedExpense: FixedExpense)
}