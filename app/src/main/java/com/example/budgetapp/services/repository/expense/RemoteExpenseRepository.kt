package com.example.budgetapp.services.repository.expense

import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.domain.repository_interfaces.IExpenseRepository
import com.example.budgetapp.services.remote.IBudgetAppAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoteExpenseRepository(
    private val restApi: IBudgetAppAPI
): IExpenseRepository {
    override fun fetchAll(): Flow<List<Expense>> {
        return flow{
            emit(restApi.getExpenses(1).body().orEmpty())
        }
    }

    override suspend fun addExpense(expense: Expense): Long {
        val response = restApi.addExpense(expense)
        return if(response.isSuccessful){
            1
        }else{
            0
        }
    }

    override suspend fun removeExpense(expense: Expense): Int {
        val response = restApi.removeExpense(expense.id.toString())
        return if (response.isSuccessful){
            1
        }else{
            0
        }
    }

    override suspend fun updateExpense(expense: Expense): Long {
        TODO("Not yet implemented")
    }

}