package com.example.budgetapp.domain.repository_interfaces

import com.example.budgetapp.domain.models.income.FixedIncome
import kotlinx.coroutines.flow.Flow


interface IFixedIncomeRepository {
    suspend fun fetchAll():Flow<List<FixedIncome>>
    suspend fun addFixedIncome(fixedIncome: FixedIncome): Long
    suspend fun removeFixedIncome(fixedIncome: FixedIncome): Int
    suspend fun updateFixedIncome(fixedIncome: FixedIncome): Int
}