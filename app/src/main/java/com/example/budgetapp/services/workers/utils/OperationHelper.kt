package com.example.budgetapp.services.workers.utils

import android.util.Log
import androidx.work.ListenableWorker.Result
import retrofit2.Response

suspend fun <T> OperationHelper(
    fetch: suspend () -> T,
    operation: suspend (T) -> Response<T>,
    update: suspend(T) -> Unit,
    on404: Result = Result.failure()
): Result {
    var transaction = fetch()
    var res = operation(transaction)
    if (!res.isSuccessful) {
        Log.e("[Worker]", "${res.code()},  ${res.message()} :: ${transaction.toString()}")
        when (res.code()) {
            400 -> return Result.failure()
            404 -> return on404
            500 -> return Result.retry()
            else -> return Result.failure()
        }
    }

    update(transaction)

    return Result.success()
}