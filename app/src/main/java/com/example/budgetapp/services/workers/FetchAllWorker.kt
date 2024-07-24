package com.example.budgetapp.services.workers

import android.app.Notification
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.Data
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
import com.example.budgetapp.services.workers.utils.isConnected
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.sql.SQLException

class FetchAllWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext,
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

        return withContext(Dispatchers.IO){

            try {

                var results = listOf(
                    fetchAndSave(
                        call = {restApi.getExpenses(userId)},
                        onSuccess = {expenses -> expenseDao.add(expenses.map { RoomExpense.fromExpense(it, userId, dirty = false) })}
                    ),
                    fetchAndSave(
                        call = {restApi.getIncomes(userId)},
                        onSuccess = {incomes -> incomeDao.add(incomes.map { RoomIncome.fromIncome(it, userId, dirty = false) })}
                    ),
                    fetchAndSave(
                        call = {restApi.getFixedExpense(userId)},
                        onSuccess = {fixedExpenses -> fixedExpenseDao.add(fixedExpenses.map { RoomFixedExpense.fromFixedExpense(it, userId, dirty = false) })}
                    ),
                    fetchAndSave(
                        call = {restApi.getFixedIncomes(userId)},
                        onSuccess = {fixedIncomes -> fixedIncomeDao.add(fixedIncomes.map { RoomFixedIncome.fromFixedIncome(it, userId, dirty = false) })}
                    )
                )

                when{
                    results.any { it == Result.retry() } -> return@withContext Result.retry()
                    results.any { it == Result.failure() } -> return@withContext Result.failure()
                }

                var data = Data.Builder()
                data.putInt("userId", userId)

                return@withContext Result.success(data.build())


            }catch (e: SQLException){
                Log.e("[SQLite]", e.message.toString())
                Result.retry()
            }catch (e: IOException){
                Log.e("[Network]", e.message.toString())
                Result.retry()
            }catch (e: HttpException){
                Log.e("[HTTP]", e.message.toString())
                Result.retry()
            }catch (e: SocketTimeoutException){
                Log.e("[Network]", e.message.toString())
                Result.retry()
            }catch (e: Exception) {
                Log.e("[Worker]", "Unexpected error: " + e.message.toString())
                Result.failure()
            }
        }
    }

    private suspend fun <T>fetchAndSave(
        call: suspend() -> Response<List<T>>,
        onSuccess: suspend (List<T>) -> Unit
    ): Result{

        var res = call()
        if(!res.isSuccessful){
            Log.e("[Worker]", "${res.code()},  ${res.message()}")
            when(res.code()){
                400 -> Result.failure()
                500 -> Result.retry()
                else -> Result.failure()
            }
        }

        res.body()?.let { onSuccess(it) }

        return Result.success()
    }
}