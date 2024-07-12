package com.example.budgetapp.services.repository.fixed_income

import com.example.budgetapp.domain.repository_interfaces.IFixedIncomeRepository
import com.example.budgetapp.domain.models.income.FixedIncome
import com.example.budgetapp.services.dao.fixed_income.FixedIncomeDao
import kotlinx.coroutines.flow.Flow

class LocalFixedIncomeRepository(
    private val dao: FixedIncomeDao
): IFixedIncomeRepository {

    override fun fetchAll(): Flow<List<FixedIncome>> {
        return dao.fetchAll()
    }

    override suspend fun addFixedIncome(fixedIncome: FixedIncome): Long {
        return dao.add(fixedIncome)
    }

    override suspend fun removeFixedIncome(fixedIncome: FixedIncome): Int {
        return dao.remove(fixedIncome)
    }

    override suspend fun updateFixedIncome(fixedIncome: FixedIncome): Long {
        return dao.update(fixedIncome)
    }

    override suspend fun updateFixedIncome(fixedIncomes: List<FixedIncome>): Int {
        return dao.update(fixedIncomes)
    }
}