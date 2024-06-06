package com.example.budgetapp.services.repository.fixed_expense

import com.example.budgetapp.models.expense.FixedExpense

interface IFixedExpenseRepository {
    fun fetchAll():MutableList<FixedExpense>
    fun addFixedExpense(fixedExpense: FixedExpense)
    fun removeFixedExpense(fixedExpense: FixedExpense)
    fun updateFixedExpense(fixedExpense: FixedExpense)
}