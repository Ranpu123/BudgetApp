package com.example.budgetapp.domain.repository_interfaces

import com.example.budgetapp.domain.models.expense.FixedExpense
import kotlinx.coroutines.flow.Flow

interface IFixedExpenseRepository {
    fun fetchAll(): Flow<List<FixedExpense>>
    suspend fun addFixedExpense(fixedExpense: FixedExpense): Long
    suspend fun removeFixedExpense(fixedExpense: FixedExpense): Int
    suspend fun updateFixedExpense(fixedExpense: FixedExpense): Long
    suspend fun updateFixedExpense(fixedExpenses: List<FixedExpense>): Int
}