package com.example.budgetapp.services.workers.utils

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkRequest
import java.util.concurrent.TimeUnit

fun <T: ListenableWorker>createOneTimeWorkRequest(inputData: Data.Builder, clazz: Class<T>): OneTimeWorkRequest {

    var request = OneTimeWorkRequest.Builder(clazz)
        .setInputData(inputData.build())
        .setBackoffCriteria(
            backoffPolicy = BackoffPolicy.EXPONENTIAL,
            timeUnit = TimeUnit.MILLISECONDS,
            backoffDelay = WorkRequest.MIN_BACKOFF_MILLIS
        ).build()
    return request
}