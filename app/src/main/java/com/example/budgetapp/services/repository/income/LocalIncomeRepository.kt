package com.example.budgetapp.services.repository.income

import com.example.budgetapp.domain.models.income.IncomeCategory
import com.example.budgetapp.domain.repository_interfaces.IIncomeRepository
import com.example.budgetapp.domain.models.income.Income
import com.example.budgetapp.services.local.dao.income.IncomeDao
import com.example.budgetapp.services.local.models.RoomIncome
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class LocalIncomeRepository(
    private val dao: IncomeDao
): IIncomeRepository {

    override fun fetchAll(): Flow<List<Income>> {
        return dao.fetchAll(1)
    }

    override suspend fun addIncome(income: Income): Long {
        return dao.add(RoomIncome.fromIncome(income, 1))
    }

    override suspend fun removeIncome(income: Income): Int {
        return dao.remove(income.id.toString())
    }

    override suspend fun updateIncome(income: Income): Long {
        return dao.update(RoomIncome.fromIncome(income,1))
    }
}