package com.example.budgetapp.services.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.services.local.dao.expense.ExpenseDao
import com.example.budgetapp.services.local.dao.fixed_expense.FixedExpenseDao
import com.example.budgetapp.services.local.dao.fixed_income.FixedIncomeDao
import com.example.budgetapp.services.local.dao.income.IncomeDao
import com.example.budgetapp.services.local.models.RoomExpense
import com.example.budgetapp.services.local.models.RoomFixedExpense
import com.example.budgetapp.services.local.models.RoomFixedIncome
import com.example.budgetapp.services.local.models.RoomIncome
import com.example.budgetapp.services.remote.IBudgetAppAPI
import com.example.budgetapp.services.workers.utils.OperationHelper
import com.example.budgetapp.services.workers.utils.isConnected
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.sql.SQLException

class DeletePendingWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params), KoinComponent {
    override suspend fun doWork(): Result {
        val expenseDao: ExpenseDao by inject()
        val incomeDao: IncomeDao by inject()
        val fixedIncomeDao: FixedIncomeDao by inject()
        val fixedExpenseDao: FixedExpenseDao by inject()

        val restApi: IBudgetAppAPI by inject()
        val context: Context by inject()

        if(!isConnected(context)){
            Log.e("[Network]", "There is no connection available!")
            return Result.retry()
        }

        var userId = inputData.getInt("userId", -1)
        if(userId == -1){
            Result.failure()
        }

        return withContext(Dispatchers.IO){
            try{

                var results = listOf(
                    removePending(
                        fetch = {expenseDao.getToDeleteExpenses(userId)},
                        operation = {expense -> restApi.removeExpense(expense.id.toString())},
                        hardRemove = {expenses -> expenseDao.hardRemove(expenses.map { RoomExpense.fromExpense(it, userId) })},
                    ),
                    removePending(
                        fetch = {incomeDao.getToDeleteIncome(userId)},
                        operation = {income -> restApi.removeIncome(income.id.toString())},
                        hardRemove = {incomes -> incomeDao.hardRemove(incomes.map { RoomIncome.fromIncome(it, userId) })},
                    ),
                    removePending(
                        fetch = {fixedIncomeDao.getToDeleteFixedIncome(userId)},
                        operation = {fixedIncome -> restApi.removeFixedIncome(fixedIncome.id.toString())},
                        hardRemove = {fixedIncomes -> fixedIncomeDao.hardRemove(fixedIncomes.map { RoomFixedIncome.fromFixedIncome(it, userId) })},
                    ),
                    removePending(
                        fetch = {fixedExpenseDao.getToDeleteFixedExpenses(userId)},
                        operation = {fixedExpense -> restApi.removeFixedExpense(fixedExpense.id.toString())},
                        hardRemove = {fixedExpenses -> fixedExpenseDao.hardRemove(fixedExpenses.map { RoomFixedExpense.fromFixedExpense(it, userId) })},
                    )
                )

                when{
                    results.any { it == Result.retry() } -> return@withContext Result.retry()
                    results.any { it == Result.failure()} -> return@withContext Result.failure()
                }

                var data = Data.Builder()
                data.putInt("userId", userId)

                return@withContext Result.success(data.build())
            }catch (e: Exception) {
                Log.e("[Worker]", "Unexpected error: " + e.message.toString())
                Result.failure()
            } catch (e: SQLException) {
                Log.e("[SQLite]", e.message.toString())
                Result.retry()
            } catch (e: IOException) {
                Log.e("[Network]", e.message.toString())
                Result.retry()
            } catch (e: HttpException) {
                Log.e("[HTTP]", e.message.toString())
                Result.retry()
            }
        }
    }

    private suspend fun <T> removePending(
        fetch: suspend() -> List<T>,
        operation: suspend(T) -> Response<T>,
        hardRemove: suspend(List<T>) -> Int,
    ): Result {
        var toRemove = fetch()
        var results = mutableListOf<Result>()
        var removed = mutableListOf<T>()

        if(toRemove.isNotEmpty()){
            toRemove.forEach {
                var res = handleResponse(res = operation(it))
                results.add(res)
                removed.add(it)
            }
        }

        hardRemove(removed)

        when{
            results.any { it == Result.retry() } -> return Result.retry()
            results.any { it == Result.failure()} -> return Result.failure()
        }

        return Result.success()
    }

    private fun <T> handleResponse(
        res: Response<T>
    ): Result {
        if (!res.isSuccessful) {
            Log.e("[Worker]", "${res.code()},  ${res.message()}")
            when (res.code()) {
                400 -> return Result.failure()
                404 -> return Result.success()
                500 -> return Result.retry()
                else -> return Result.failure()
            }
        }
        return Result.success()
    }

}