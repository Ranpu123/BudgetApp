package com.example.budgetapp.services.repository.expense

import com.example.budgetapp.domain.repository_interfaces.IExpenseRepository
import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.services.local.dao.expense.ExpenseDao
import kotlinx.coroutines.flow.Flow

class LocalExpenseRepository(
    private val dao: ExpenseDao
): IExpenseRepository {

    override fun fetchAll(): Flow<List<Expense>> {
        return dao.fetchAll()
    }

    override suspend fun addExpense(expense: Expense): Long {
        return dao.add(expense)
    }

    override suspend fun removeExpense(expense: Expense): Int {
        return dao.remove(expense)
    }

    override suspend fun updateExpense(expense: Expense): Long {
        return dao.update(expense)
    }
}