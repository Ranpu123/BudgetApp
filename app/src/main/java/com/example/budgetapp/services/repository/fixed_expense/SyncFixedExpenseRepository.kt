package com.example.budgetapp.services.repository.fixed_expense

import androidx.work.BackoffPolicy
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.budgetapp.domain.models.expense.FixedExpense
import com.example.budgetapp.domain.repository_interfaces.IFixedExpenseRepository
import com.example.budgetapp.services.local.dao.fixed_expense.FixedExpenseDao
import com.example.budgetapp.services.local.models.RoomFixedExpense
import com.example.budgetapp.services.workers.expense.AddExpenseWorker
import com.example.budgetapp.services.workers.fixed_expense.AddFixedExpenseWorker
import com.example.budgetapp.services.workers.fixed_expense.RemoveFixedExpenseWorker
import com.example.budgetapp.services.workers.fixed_expense.UpdateFixedExpenseLastDateWorker
import com.example.budgetapp.services.workers.utils.createOneTimeWorkRequest
import com.example.budgetapp.utils.BudgetAppConstants.TRANSACTION_ID
import com.example.budgetapp.utils.BudgetAppConstants.USER_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class SyncFixedExpenseRepository(
    private val dao: FixedExpenseDao,
    private val workManager: WorkManager
): IFixedExpenseRepository {
    override fun fetchAll(): Flow<List<FixedExpense>> {
        return dao.fetchAll(1)
    }

    override suspend fun addFixedExpense(fixedExpense: FixedExpense): Long {
        return withContext(Dispatchers.IO) {
            val n = async{dao.add(RoomFixedExpense.fromFixedExpense(fixedExpense, 1))}.await()

            var data = Data.Builder()
            data.putString(TRANSACTION_ID, fixedExpense.id.toString())
            data.putInt(USER_ID, 1)

            var addRequest = createOneTimeWorkRequest(data, AddFixedExpenseWorker::class.java)
            workManager.enqueue(addRequest)

            return@withContext n
        }
    }

    override suspend fun removeFixedExpense(fixedExpense: FixedExpense): Int {
        val n = dao.remove(fixedExpense.id.toString())

        var data = Data.Builder()
        data.putString(TRANSACTION_ID ,fixedExpense.id.toString())

        var removeRequest = createOneTimeWorkRequest(data, RemoveFixedExpenseWorker::class.java)
        workManager.enqueue(removeRequest)

        return n
    }

    override suspend fun updateFixedExpense(fixedExpense: FixedExpense): Long {
        TODO("Not yet implemented")
    }

    override suspend fun updateFixedExpense(fixedExpenses: List<FixedExpense>): Int {
        var n = dao.update(fixedExpenses.map { RoomFixedExpense.fromFixedExpense(it, 1) })

        fixedExpenses.forEach {
            var data = Data.Builder()
            data.putString(TRANSACTION_ID ,it.id.toString())
            data.putInt(USER_ID, 1)

            var updateRequest = createOneTimeWorkRequest(data, UpdateFixedExpenseLastDateWorker::class.java)
            workManager.enqueue(updateRequest)
        }

        return n
    }
}