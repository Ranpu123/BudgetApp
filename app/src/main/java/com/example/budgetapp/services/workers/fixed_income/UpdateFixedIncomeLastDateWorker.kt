package com.example.budgetapp.services.workers.fixed_income

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.budgetapp.domain.models.income.FixedIncome
import com.example.budgetapp.services.local.dao.fixed_income.FixedIncomeDao
import com.example.budgetapp.services.local.models.RoomFixedIncome
import com.example.budgetapp.services.remote.IBudgetAppAPI
import com.example.budgetapp.services.remote.models.ApiFixedIncome
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

class UpdateFixedIncomeLastDateWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params), KoinComponent{
    override suspend fun doWork(): Result {
        val dao: FixedIncomeDao by inject()
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

                return@withContext OperationHelper<FixedIncome>(
                    fetch = {dao.getFixedIncome(transactionId!!)},
                    operation = {fixedIncome -> restApi.updateFixedIncomeLastDate(fixedIncome.id.toString(),
                        ApiFixedIncome.fromFixedIncome(fixedIncome, userId))},
                    update = {fixedIncome -> dao.update(RoomFixedIncome.fromFixedIncome(fixedIncome, userId, dirty = false))},
                    on404 = Result.retry()
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