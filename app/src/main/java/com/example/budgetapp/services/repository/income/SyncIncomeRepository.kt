package com.example.budgetapp.services.repository.income

import androidx.work.BackoffPolicy
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.budgetapp.domain.models.income.Income
import com.example.budgetapp.domain.repository_interfaces.IIncomeRepository
import com.example.budgetapp.services.local.dao.income.IncomeDao
import com.example.budgetapp.services.local.models.RoomExpense
import com.example.budgetapp.services.local.models.RoomIncome
import com.example.budgetapp.services.workers.expense.AddExpenseWorker
import com.example.budgetapp.services.workers.income.AddIncomeWorker
import com.example.budgetapp.services.workers.income.RemoveIncomeWorker
import com.example.budgetapp.services.workers.utils.createOneTimeWorkRequest
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

class SyncIncomeRepository(
    private val dao: IncomeDao,
    private val workManager: WorkManager
):IIncomeRepository {
    override fun fetchAll(): Flow<List<Income>> {
        return dao.fetchAll(1)
    }

    override suspend fun addIncome(income: Income): Long {
        val n = dao.add(RoomIncome.fromIncome(income,1))

        var data = Data.Builder()
        data.putString("transactionId" ,income.id.toString())
        data.putInt("userId", 1)

        var addRequest = createOneTimeWorkRequest(data, AddIncomeWorker::class.java)
        workManager.enqueue(addRequest)

        return n
    }

    override suspend fun removeIncome(income: Income): Int {
        val n = dao.remove(income.id.toString())

        var data = Data.Builder()
        data.putString("transactionId" ,income.id.toString())

        var addRequest = createOneTimeWorkRequest(data, RemoveIncomeWorker::class.java)
        workManager.enqueue(addRequest)

        return n
    }

    override suspend fun updateIncome(income: Income): Long {
        TODO("Not yet implemented")
    }
}