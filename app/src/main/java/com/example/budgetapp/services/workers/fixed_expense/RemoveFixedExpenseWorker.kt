package com.example.budgetapp.services.workers.fixed_expense

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.budgetapp.domain.models.expense.FixedExpense
import com.example.budgetapp.services.local.dao.fixed_expense.FixedExpenseDao
import com.example.budgetapp.services.local.models.RoomFixedExpense
import com.example.budgetapp.services.remote.IBudgetAppAPI
import com.example.budgetapp.services.workers.utils.OperationHelper
import com.example.budgetapp.services.workers.utils.isConnected
import com.example.budgetapp.utils.BudgetAppConstants
import com.example.budgetapp.utils.BudgetAppConstants.TRANSACTION_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.sql.SQLException

class RemoveFixedExpenseWorker(
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

        return withContext(Dispatchers.IO) {

            var transactionId = inputData.getString(TRANSACTION_ID)

            if (transactionId.isNullOrBlank()) {
                Log.e("[Worker]", "transactionId is empty")
                Result.failure()
            }

            try {

                return@withContext OperationHelper<FixedExpense>(
                    fetch = {dao.getFixedExpense(transactionId!!)},
                    operation = {fixedExpense -> restApi.removeFixedExpense(fixedExpense.id.toString())},
                    update = {fixedExpense -> dao.hardRemove(RoomFixedExpense.fromFixedExpense(fixedExpense, 0))}
                )


            } catch (e: SQLException) {
                Log.e("[SQLite]", e.message.toString())
                Result.retry()
            } catch (e: IOException) {
                Log.e("[Network]", e.message.toString())
                Result.retry()
            } catch (e: HttpException) {
                Log.e("[HTTP]", e.message.toString())
                Result.retry()
            }catch (e: SocketTimeoutException){
                Log.e("[Network]", e.message.toString())
                Result.retry()
            } catch (e: Exception) {
                Log.e("[Worker]", "Unexpected error: " + e.message.toString())
                Result.failure()
            }
        }
    }
}