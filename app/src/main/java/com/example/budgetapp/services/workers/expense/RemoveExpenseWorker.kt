package com.example.budgetapp.services.workers.expense

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.services.local.dao.expense.ExpenseDao
import com.example.budgetapp.services.local.models.RoomExpense
import com.example.budgetapp.services.remote.IBudgetAppAPI
import com.example.budgetapp.services.workers.utils.OperationHelper
import com.example.budgetapp.services.workers.utils.isConnected
import com.example.budgetapp.utils.BudgetAppConstants
import com.example.budgetapp.utils.BudgetAppConstants.TRANSACTION_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.sql.SQLException
import kotlin.math.exp

class RemoveExpenseWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params), KoinComponent {
    override suspend fun doWork(): Result {
        val dao: ExpenseDao by inject()
        val restApi: IBudgetAppAPI by inject()
        val context: Context by inject()

        if(!isConnected(context)){
            Log.e("[Network]", "There is no connection available!")
            return Result.retry()
        }

        var transactionId = inputData.getString(TRANSACTION_ID)

        if (transactionId.isNullOrBlank()) {
            Log.e("[Worker]", "transactionId is empty")
            Result.failure()
        }

        return withContext(Dispatchers.IO) {
            try {

                return@withContext OperationHelper<Expense>(
                    fetch = {dao.getExpense(transactionId!!)},
                    operation = {expense -> restApi.removeExpense(expense.id.toString())},
                    update = {expense -> dao.hardRemove(RoomExpense.fromExpense(expense,0))}
                )


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
}