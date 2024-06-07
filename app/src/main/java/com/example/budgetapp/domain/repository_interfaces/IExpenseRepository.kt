package com.example.budgetapp.domain.repository_interfaces

import com.example.budgetapp.domain.models.expense.Expense

interface IExpenseRepository {

    fun fetchAll():MutableList<Expense>
    fun addExpense(expense: Expense)
    fun removeExpense(expense: Expense)
    fun updateExpense(expense: Expense)

}