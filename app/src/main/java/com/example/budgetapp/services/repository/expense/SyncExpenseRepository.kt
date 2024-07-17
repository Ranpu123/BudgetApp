package com.example.budgetapp.services.repository.expense

import androidx.work.BackoffPolicy
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.domain.repository_interfaces.IExpenseRepository
import com.example.budgetapp.services.local.dao.expense.ExpenseDao
import com.example.budgetapp.services.local.models.RoomExpense
import com.example.budgetapp.services.workers.expense.AddExpenseWorker
import com.example.budgetapp.services.workers.expense.RemoveExpenseWorker
import com.example.budgetapp.services.workers.utils.createOneTimeWorkRequest
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

class SyncExpenseRepository(
    private val dao: ExpenseDao,
    private val workManager: WorkManager
): IExpenseRepository {
    override fun fetchAll(): Flow<List<Expense>> {
        return dao.fetchAll(1)
    }

    override suspend fun addExpense(expense: Expense): Long {
        val n = dao.add(RoomExpense.fromExpense(expense,1))

        var data = Data.Builder()
        data.putString("transactionId" ,expense.id.toString())
        data.putInt("userId", 1)

        var addRequest = createOneTimeWorkRequest(data, AddExpenseWorker::class.java)
        workManager.enqueue(addRequest)

        return n
    }

    override suspend fun removeExpense(expense: Expense): Int {
        val n = dao.remove(expense.id.toString())

        var data = Data.Builder()
        data.putString("transactionId" ,expense.id.toString())

        var removeRequest = createOneTimeWorkRequest(data, RemoveExpenseWorker::class.java)
        workManager.enqueue(removeRequest)

        return n
    }

    override suspend fun updateExpense(expense: Expense): Long {
        throw UnsupportedOperationException()
    }
}