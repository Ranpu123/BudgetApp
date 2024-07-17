package com.example.budgetapp.services.workers

import android.content.Context
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import com.example.budgetapp.services.local.dao.expense.ExpenseDao
import com.example.budgetapp.services.local.dao.fixed_expense.FixedExpenseDao
import com.example.budgetapp.services.local.dao.fixed_income.FixedIncomeDao
import com.example.budgetapp.services.local.dao.income.IncomeDao
import com.example.budgetapp.services.local.models.RoomExpense
import com.example.budgetapp.services.local.models.RoomFixedExpense
import com.example.budgetapp.services.local.models.RoomFixedIncome
import com.example.budgetapp.services.local.models.RoomIncome
import com.example.budgetapp.services.remote.IBudgetAppAPI
import com.example.budgetapp.services.remote.models.ApiExpense
import com.example.budgetapp.services.remote.models.ApiFixedExpense
import com.example.budgetapp.services.remote.models.ApiFixedIncome
import com.example.budgetapp.services.remote.models.ApiIncome
import com.example.budgetapp.services.workers.utils.isConnected
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.sql.SQLException
import java.util.concurrent.TimeUnit

class StartupWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext,
    params
), KoinComponent {
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

        return withContext(Dispatchers.IO) {
            try {
                var results = listOf(
                    updateDirty(
                        call = { incomeDao.getDirtyEntries(userId) },
                        clear = { incomes ->
                            incomeDao.clearDirty(incomes.map {
                                RoomIncome.fromIncome(
                                    it,
                                    userId,
                                    dirty = false
                                )
                            })
                        },
                        update = { income ->
                            restApi.updateIncome(
                                income.id.toString(),
                                ApiIncome.fromIncome(income, userId)
                            )
                        },
                        add = { income -> restApi.addIncome(ApiIncome.fromIncome(income, userId)) }
                    ),
                    updateDirty(
                        call = { expenseDao.getDirtyEntries(userId) },
                        clear = { expenses ->
                            expenseDao.clearDirty(expenses.map {
                                RoomExpense.fromExpense(
                                    it,
                                    userId,
                                    dirty = false
                                )
                            })
                        },
                        update = { expense ->
                            restApi.updateExpense(
                                expense.id.toString(),
                                ApiExpense.fromExpense(expense, userId)
                            )
                        },
                        add = { expense ->
                            restApi.addExpense(
                                ApiExpense.fromExpense(
                                    expense,
                                    userId
                                )
                            )
                        }
                    ),
                    updateDirty(
                        call = { fixedExpenseDao.getDirtyEntries(userId) },
                        clear = { fixedExpenses ->
                            fixedExpenseDao.clearDirty(fixedExpenses.map {
                                RoomFixedExpense.fromFixedExpense(
                                    it,
                                    userId,
                                    dirty = false
                                )
                            })
                        },
                        update = { fixedExpense ->
                            restApi.updateFixedExpense(
                                fixedExpense.id.toString(),
                                ApiFixedExpense.fromFixedExpense(fixedExpense, userId)
                            )
                        },
                        add = { fixedExpense ->
                            restApi.addFixedExpense(
                                ApiFixedExpense.fromFixedExpense(
                                    fixedExpense,
                                    userId
                                )
                            )
                        }
                    ),
                    updateDirty(
                        call = { fixedIncomeDao.getDirtyEntries(userId) },
                        clear = { fixedIncomes ->
                            fixedIncomeDao.clearDirty(fixedIncomes.map {
                                RoomFixedIncome.fromFixedIncome(
                                    it,
                                    userId,
                                    dirty = false
                                )
                            })
                        },
                        update = { fixedIncome ->
                            restApi.updateFixedIncome(
                                fixedIncome.id.toString(),
                                ApiFixedIncome.fromFixedIncome(fixedIncome, userId)
                            )
                        },
                        add = { fixedIncome ->
                            restApi.addFixedIncome(
                                ApiFixedIncome.fromFixedIncome(
                                    fixedIncome,
                                    userId
                                )
                            )
                        }
                    )
                )

                when {
                    results.any { it == Result.retry() } -> return@withContext Result.retry()
                    results.any { it == Result.failure() } -> return@withContext Result.failure()
                }

                var data = Data.Builder()
                data.putInt("userId", userId)

                return@withContext Result.success(data.build())

            } catch (e: Exception) {
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

    private suspend fun <T>updateDirty(
        call: suspend() -> List<T>,
        clear: suspend(List<T>) -> Int,
        update: suspend(T) -> Response<T>,
        add: suspend(T) -> Response<T>
    ): Result {
        var dirty = call()
        var results = mutableListOf<Result>()
        var updated = mutableListOf<T>()
        if(dirty.isNotEmpty()){

            dirty.forEach {
                var res = handleResponse(
                    res = update(it),
                    add = {handleResponse(add(it), {Result.failure()}) }
                )
                results.add(res)

                if(res == Result.success()){
                    updated.add(it)
                }
            }
            clear(updated)
        }

        when{
            results.any { it == Result.retry() } -> return Result.retry()
            results.any { it == Result.failure()} -> return Result.failure()
        }

        return Result.success()
    }

    private suspend fun <T> handleResponse(
        res: Response<T>,
        add: suspend () -> Result
    ): Result {
        if (!res.isSuccessful) {
            Log.e("[Worker]", "${res.code()},  ${res.message()}")
            when (res.code()) {
                400 -> return Result.failure()
                404 -> return add()
                500 -> return Result.retry()
                else -> return Result.failure()
            }
        }
        return Result.success()
    }
}