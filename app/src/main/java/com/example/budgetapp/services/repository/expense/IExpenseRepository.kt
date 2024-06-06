package com.example.budgetapp.services.repository.expense

import com.example.budgetapp.models.expense.Expense

interface IExpenseRepository {

    fun fetchAll():MutableList<Expense>
    fun addExpense(expense: Expense)
    fun removeExpense(expense: Expense)
    fun updateExpense(expense: Expense)

}