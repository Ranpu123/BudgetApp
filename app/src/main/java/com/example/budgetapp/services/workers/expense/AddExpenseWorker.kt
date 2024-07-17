package com.example.budgetapp.services.workers.expense

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.services.local.dao.expense.ExpenseDao
import com.example.budgetapp.services.local.models.RoomExpense
import com.example.budgetapp.services.remote.IBudgetAppAPI
import com.example.budgetapp.services.remote.models.ApiExpense
import com.example.budgetapp.services.workers.utils.OperationHelper
import com.example.budgetapp.services.workers.utils.isConnected
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.GlobalContext.get
import retrofit2.HttpException
import java.io.IOException
import java.sql.SQLException

class AddExpenseWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params), KoinComponent{
    override suspend fun doWork(): Result {

        val dao: ExpenseDao by inject()
        val restApi: IBudgetAppAPI by inject()
        val context: Context by inject()

        if(!isConnected(context)){
            Log.e("[Network]", "There is no connection available!")
            return Result.retry()
        }

        var transactionId = inputData.getString("transactionId")
        var userId = inputData.getInt("userId", -1)

        if (transactionId.isNullOrBlank() && userId == -1) {
            Log.e("[Worker]", "transactionId or userId is empty")
            Result.failure()
        }

        return withContext(Dispatchers.IO) {
            try {

                return@withContext OperationHelper<Expense>(
                    fetch = {(dao.getExpense(transactionId!!))},
                    operation = {expense -> restApi.addExpense(ApiExpense.fromExpense(expense, userId))},
                    update = {expense -> dao.update(RoomExpense.fromExpense(expense, userId, dirty = false))}
                )

            }catch (e: Exception){
                Log.e("[Worker]", "Unexpected error: " + e.message.toString())
                Result.failure()
            }catch (e: SQLException){
                Log.e("[SQLite]", e.message.toString())
                Result.retry()
            }catch (e: IOException){
                Log.e("[Network]", e.message.toString())
                Result.retry()
            }catch (e: HttpException){
                Log.e("[HTTP]", e.message.toString())
                Result.retry()
            }
        }
    }
}