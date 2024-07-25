package com.example.budgetapp.services.repository.fixed_expense

import com.example.budgetapp.domain.repository_interfaces.IFixedExpenseRepository
import com.example.budgetapp.domain.models.expense.FixedExpense
import com.example.budgetapp.services.local.dao.fixed_expense.FixedExpenseDao
import com.example.budgetapp.services.local.models.RoomFixedExpense
import kotlinx.coroutines.flow.Flow


class LocalFixedExpenseRepository(
    private val dao: FixedExpenseDao
): IFixedExpenseRepository {

    override fun fetchAll(): Flow<List<FixedExpense>> {
        return dao.fetchAll(1)
    }

    override suspend fun addFixedExpense(fixedExpense: FixedExpense): Long {
        return dao.add(RoomFixedExpense.fromFixedExpense(fixedExpense, 1))
    }

    override suspend fun removeFixedExpense(fixedExpense: FixedExpense): Int {
        return dao.remove(fixedExpense.id.toString())
    }

    override suspend fun updateFixedExpense(fixedExpense: FixedExpense): Long {
        return dao.update(RoomFixedExpense.fromFixedExpense(fixedExpense, 1))
    }

    override suspend fun updateFixedExpense(fixedExpenses: List<FixedExpense>): Int {
        return dao.update(fixedExpenses.map { RoomFixedExpense.fromFixedExpense(it, 1) })
    }
}