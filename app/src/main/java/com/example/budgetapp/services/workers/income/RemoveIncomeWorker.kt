package com.example.budgetapp.services.workers.income

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.budgetapp.domain.models.income.Income
import com.example.budgetapp.services.local.dao.income.IncomeDao
import com.example.budgetapp.services.local.models.RoomIncome
import com.example.budgetapp.services.remote.IBudgetAppAPI
import com.example.budgetapp.services.remote.models.ApiIncome
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

class RemoveIncomeWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params), KoinComponent {
    override suspend fun doWork(): Result {
        val dao: IncomeDao by inject()
        val restApi: IBudgetAppAPI by inject()
        val context: Context by inject()

        if(!isConnected(context)){
            Log.e("[Network]", "There is no connection available!")
            return Result.retry()
        }

        return withContext(Dispatchers.IO) {

            var transactionId = inputData.getString("transactionId")

            if (transactionId.isNullOrBlank()) {
                Log.e("[Worker]", "transactionId is empty")
                Result.failure()
            }

            try {

                return@withContext OperationHelper<Income>(
                    fetch = {dao.getIncome(transactionId!!)},
                    operation = {income -> restApi.removeIncome(income.id.toString())},
                    update = {income -> dao.hardRemove(RoomIncome.fromIncome(income, 0))}
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