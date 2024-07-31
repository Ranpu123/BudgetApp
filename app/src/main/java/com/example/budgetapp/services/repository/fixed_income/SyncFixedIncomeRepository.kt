package com.example.budgetapp.services.repository.fixed_income

import androidx.work.BackoffPolicy
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.budgetapp.domain.models.income.FixedIncome
import com.example.budgetapp.domain.repository_interfaces.IFixedIncomeRepository
import com.example.budgetapp.services.local.dao.fixed_income.FixedIncomeDao
import com.example.budgetapp.services.local.models.RoomFixedIncome
import com.example.budgetapp.services.workers.expense.AddExpenseWorker
import com.example.budgetapp.services.workers.fixed_income.AddFixedIncomeWorker
import com.example.budgetapp.services.workers.fixed_income.RemoveFixedIncomeWorker
import com.example.budgetapp.services.workers.fixed_income.UpdateFixedIncomeLastDateWorker
import com.example.budgetapp.services.workers.utils.createOneTimeWorkRequest
import com.example.budgetapp.utils.BudgetAppConstants.TRANSACTION_ID
import com.example.budgetapp.utils.BudgetAppConstants.USER_ID
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

class SyncFixedIncomeRepository(
    private val dao: FixedIncomeDao,
    private val workManager: WorkManager
): IFixedIncomeRepository {
    override fun fetchAll(): Flow<List<FixedIncome>> {
        return dao.fetchAll(1)
    }

    override suspend fun addFixedIncome(fixedIncome: FixedIncome): Long {
        var n = dao.add(RoomFixedIncome.fromFixedIncome(fixedIncome, 1))

        var data = Data.Builder()
        data.putString(TRANSACTION_ID ,fixedIncome.id.toString())
        data.putInt(USER_ID, 1)

        var addRequest = createOneTimeWorkRequest(data, AddFixedIncomeWorker::class.java)
        workManager.enqueue(addRequest)

        return n
    }

    override suspend fun removeFixedIncome(fixedIncome: FixedIncome): Int {
        var n = dao.remove(fixedIncome.id.toString())

        var data = Data.Builder()
        data.putString(TRANSACTION_ID ,fixedIncome.id.toString())

        var removeRequest = createOneTimeWorkRequest(data, RemoveFixedIncomeWorker::class.java)
        workManager.enqueue(removeRequest)

        return n
    }

    override suspend fun updateFixedIncome(fixedIncome: FixedIncome): Long {
        TODO("Not yet implemented")
    }

    override suspend fun updateFixedIncome(fixedIncomes: List<FixedIncome>): Int {
        var n = dao.update(fixedIncomes.map { RoomFixedIncome.fromFixedIncome(it, 1) })

        fixedIncomes.forEach {
            var data = Data.Builder()
            data.putString(TRANSACTION_ID ,it.id.toString())
            data.putInt(USER_ID, 1)

            var updateRequest = createOneTimeWorkRequest(data, UpdateFixedIncomeLastDateWorker::class.java)
            workManager.enqueue(updateRequest)
        }

        return n
    }
}