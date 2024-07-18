package com.example.budgetapp.domain.repository_interfaces

import com.example.budgetapp.domain.models.income.Income
import kotlinx.coroutines.flow.Flow

interface IIncomeRepository {
    fun fetchAll(): Flow<List<Income>>
    suspend fun addIncome(income: Income): Long
    suspend fun removeIncome(income: Income): Int
    suspend fun updateIncome(income: Income): Long
}