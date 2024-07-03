package com.example.budgetapp.domain.repository_interfaces

import com.example.budgetapp.domain.models.expense.Expense
import kotlinx.coroutines.flow.Flow

interface IExpenseRepository {
    suspend fun fetchAll(): Flow<List<Expense>>
    suspend fun addExpense(expense: Expense): Long
    suspend fun removeExpense(expense: Expense): Int
    suspend fun updateExpense(expense: Expense): Int
}