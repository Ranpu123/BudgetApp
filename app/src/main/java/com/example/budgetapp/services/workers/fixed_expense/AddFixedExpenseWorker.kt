package com.example.budgetapp.services.workers.fixed_expense

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.budgetapp.domain.models.expense.FixedExpense
import com.example.budgetapp.services.local.dao.fixed_expense.FixedExpenseDao
import com.example.budgetapp.services.local.models.RoomFixedExpense
import com.example.budgetapp.services.remote.IBudgetAppAPI
import com.example.budgetapp.services.remote.models.ApiFixedExpense
import com.example.budgetapp.services.workers.utils.OperationHelper
import com.example.budgetapp.services.workers.utils.isConnected
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.HttpException
import java.io.IOException
import java.sql.SQLException

class AddFixedExpenseWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params), KoinComponent {
    override suspend fun doWork(): Result {
        val dao: FixedExpenseDao by inject()
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

                return@withContext OperationHelper<FixedExpense>(
                    fetch = { dao.getFixedExpense(transactionId!!)},
                    operation = {fixedExpense ->restApi.addFixedExpense(ApiFixedExpense.fromFixedExpense(fixedExpense, userId))},
                    update = {fixedExpense -> dao.update(RoomFixedExpense.fromFixedExpense(fixedExpense, userId, dirty = false))}
                )

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
}