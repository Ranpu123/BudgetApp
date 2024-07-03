package com.example.budgetapp.services.repository.income

import com.example.budgetapp.domain.models.income.IncomeCategory
import com.example.budgetapp.domain.repository_interfaces.IIncomeRepository
import com.example.budgetapp.domain.models.income.Income
import com.example.budgetapp.services.dao.income.IncomeDao
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class LocalIncomeRepository(
    private val dao: IncomeDao
): IIncomeRepository {

    override suspend fun fetchAll(): Flow<List<Income>> {
        return dao.fetchAll()
    }

    override suspend fun addIncome(income: Income): Long {
        return dao.add(income)
    }

    override suspend fun removeIncome(income: Income): Int {
        return dao.remove(income)
    }

    override suspend fun updateIncome(income: Income): Int {
        return dao.update(income)
    }
}