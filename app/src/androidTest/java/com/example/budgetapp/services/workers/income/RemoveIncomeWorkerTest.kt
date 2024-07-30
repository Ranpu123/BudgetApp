package com.example.budgetapp.services.workers.income

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import com.example.budgetapp.KoinTestRule
import com.example.budgetapp.domain.models.income.Income
import com.example.budgetapp.services.local.dao.income.IncomeDao
import com.example.budgetapp.services.local.models.RoomIncome
import com.example.budgetapp.services.remote.IBudgetAppAPI
import com.example.budgetapp.testUtils.TransactionsTestHelper
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.sql.SQLException

class RemoveIncomeWorkerTest{
    private val dao: IncomeDao = mockk(relaxed = true)
    private val restApi: IBudgetAppAPI = mockk(relaxed = true)
    private val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext

    private val koinModules = module {
        single<IncomeDao> { dao }
        single<IBudgetAppAPI> { restApi }
        single<Context> { context }
    }

    @get:Rule
    val koinTestRule = KoinTestRule(
        modules = listOf(koinModules)
    )

    @Test
    fun when_data_is_not_set_should_fail() {
        val worker = TestListenableWorkerBuilder<RemoveIncomeWorker>(context)
            .build()
        runBlocking {
            val result = worker.doWork()
            assertEquals(ListenableWorker.Result.failure(), result)
        }
    }

    @Test
    fun when_no_connection_found_should_retry() {
        val data = Data.Builder()
        data.putString("transactionId" ,"123")
        data.putInt("userId", 1)

        InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("svc wifi disable")
        InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("svc data disable")

        val worker = TestListenableWorkerBuilder<RemoveIncomeWorker>(context)
            .setInputData(data.build())
            .build()
        runBlocking {
            val result = worker.doWork()
            assertEquals(ListenableWorker.Result.retry(), result)
        }

        InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("svc wifi enable")
        InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("svc data enable")
    }

    @Test
    fun should_succeed(){

        val mockResponse: Response<Income> = mockk()
        val input = TransactionsTestHelper.incomes.first()

        coEvery { dao.getIncome(any()) } returns RoomIncome.fromIncome(input,1)
        coEvery { restApi.removeIncome(any()) } returns mockResponse
        coEvery { dao.hardRemove(any<RoomIncome>()) } returns 1
        every { mockResponse.isSuccessful } returns true

        val data = Data.Builder()
        data.putString("transactionId" ,"123")

        val worker = TestListenableWorkerBuilder<RemoveIncomeWorker>(context)
            .setInputData(data.build())
            .build()
        runBlocking {
            val result = worker.doWork()
            assertEquals(ListenableWorker.Result.success(), result)
        }
    }

    @Test
    fun when_SQLException_thrown_should_retry(){

        coEvery { dao.getIncome(any()) } throws SQLException("Error")

        val data = Data.Builder()
        data.putString("transactionId" ,"123")

        val worker = TestListenableWorkerBuilder<RemoveIncomeWorker>(context)
            .setInputData(data.build())
            .build()
        runBlocking {
            val result = worker.doWork()
            assertEquals(ListenableWorker.Result.retry(), result)
        }
    }

    @Test
    fun when_IOException_thrown_should_retry(){

        val input = TransactionsTestHelper.incomes.first()

        coEvery { dao.getIncome(any()) } returns RoomIncome.fromIncome(input,1)
        coEvery { restApi.removeIncome(any()) } throws IOException("Error")

        val data = Data.Builder()
        data.putString("transactionId" ,"123")

        val worker = TestListenableWorkerBuilder<RemoveIncomeWorker>(context)
            .setInputData(data.build())
            .build()
        runBlocking {
            val result = worker.doWork()
            assertEquals(ListenableWorker.Result.retry(), result)
        }
    }

    @Test
    fun when_HttpException_thrown_should_retry(){

        val input = TransactionsTestHelper.incomes.first()
        val errResponse: Response<*> = mockk()
        every { errResponse.message() } returns "Error"
        every { errResponse.code() } returns 502
        coEvery { dao.getIncome(any()) } returns RoomIncome.fromIncome(input,1)
        coEvery { restApi.removeIncome(any()) } throws HttpException(errResponse)

        val data = Data.Builder()
        data.putString("transactionId" ,"123")

        val worker = TestListenableWorkerBuilder<RemoveIncomeWorker>(context)
            .setInputData(data.build())
            .build()
        runBlocking {
            val result = worker.doWork()
            assertEquals(ListenableWorker.Result.retry(), result)
        }
    }

    @Test
    fun when_Exception_thrown_should_fail(){

        val input = TransactionsTestHelper.incomes.first()

        coEvery { dao.getIncome(any()) } returns RoomIncome.fromIncome(input,1)
        coEvery { restApi.removeIncome(any()) } throws Exception("Error")

        val data = Data.Builder()
        data.putString("transactionId" ,"123")

        val worker = TestListenableWorkerBuilder<RemoveIncomeWorker>(context)
            .setInputData(data.build())
            .build()
        runBlocking {
            val result = worker.doWork()
            assertEquals(ListenableWorker.Result.failure(), result)
        }
    }
}