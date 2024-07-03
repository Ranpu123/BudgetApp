package com.example.budgetapp.services.repository.fixed_expense

import com.example.budgetapp.domain.repository_interfaces.IFixedExpenseRepository
import com.example.budgetapp.domain.models.expense.FixedExpense
import com.example.budgetapp.services.dao.fixedExpense.FixedExpenseDao
import kotlinx.coroutines.flow.Flow


class LocalFixedExpenseRepository(
    private val dao: FixedExpenseDao
): IFixedExpenseRepository {

    override suspend fun fetchAll(): Flow<List<FixedExpense>> {
        return dao.fetchAll()
    }

    override suspend fun addFixedExpense(fixedExpense: FixedExpense): Long {
        return dao.add(fixedExpense)
    }

    override suspend fun removeFixedExpense(fixedExpense: FixedExpense): Int {
        return dao.delete(fixedExpense)
    }

    override suspend fun updateFixedExpense(fixedExpense: FixedExpense): Int {
        return dao.update(fixedExpense)
    }
}