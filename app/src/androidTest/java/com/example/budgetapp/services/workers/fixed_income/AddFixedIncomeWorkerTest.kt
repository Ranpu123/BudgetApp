package com.example.budgetapp.services.workers.fixed_income

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import com.example.budgetapp.KoinTestRule
import com.example.budgetapp.domain.models.expense.FixedExpense
import com.example.budgetapp.domain.models.income.FixedIncome
import com.example.budgetapp.services.local.dao.fixed_expense.FixedExpenseDao
import com.example.budgetapp.services.local.dao.fixed_income.FixedIncomeDao
import com.example.budgetapp.services.local.models.RoomFixedExpense
import com.example.budgetapp.services.local.models.RoomFixedIncome
import com.example.budgetapp.services.remote.IBudgetAppAPI
import com.example.budgetapp.services.workers.fixed_expense.AddFixedExpenseWorker
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

class AddFixedIncomeWorkerTest{
    private val dao: FixedIncomeDao = mockk(relaxed = true)
    private val restApi: IBudgetAppAPI = mockk(relaxed = true)
    private val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext

    private val koinModules = module {
        single<FixedIncomeDao> { dao }
        single<IBudgetAppAPI> { restApi }
        single<Context> { context }
    }

    @get:Rule
    val koinTestRule = KoinTestRule(
        modules = listOf(koinModules)
    )

    @Test
    fun when_data_is_not_set_should_fail() {
        val worker = TestListenableWorkerBuilder<AddFixedIncomeWorker>(context)
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

        val worker = TestListenableWorkerBuilder<AddFixedIncomeWorker>(context)
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

        val mockResponse: Response<FixedIncome> = mockk()
        val input = TransactionsTestHelper.fixedIncomes.first()

        coEvery { dao.getFixedIncome(any()) } returns RoomFixedIncome.fromFixedIncome(input,1)
        coEvery { restApi.addFixedIncome(any()) } returns mockResponse
        coEvery { dao.add(any<RoomFixedIncome>()) } returns 1
        every { mockResponse.isSuccessful } returns true

        val data = Data.Builder()
        data.putString("transactionId" ,"123")

        val worker = TestListenableWorkerBuilder<AddFixedIncomeWorker>(context)
            .setInputData(data.build())
            .build()
        runBlocking {
            val result = worker.doWork()
            assertEquals(ListenableWorker.Result.success(), result)
        }
    }

    @Test
    fun when_SQLException_thrown_should_retry(){

        coEvery { dao.getFixedIncome(any()) } throws SQLException("Error")

        val data = Data.Builder()
        data.putString("transactionId" ,"123")

        val worker = TestListenableWorkerBuilder<AddFixedIncomeWorker>(context)
            .setInputData(data.build())
            .build()
        runBlocking {
            val result = worker.doWork()
            assertEquals(ListenableWorker.Result.retry(), result)
        }
    }

    @Test
    fun when_IOException_thrown_should_retry(){

        val input = TransactionsTestHelper.fixedIncomes.first()

        coEvery { dao.getFixedIncome(any()) } returns RoomFixedIncome.fromFixedIncome(input,1)
        coEvery { restApi.addFixedIncome(any()) } throws IOException("Error")

        val data = Data.Builder()
        data.putString("transactionId" ,"123")

        val worker = TestListenableWorkerBuilder<AddFixedIncomeWorker>(context)
            .setInputData(data.build())
            .build()
        runBlocking {
            val result = worker.doWork()
            assertEquals(ListenableWorker.Result.retry(), result)
        }
    }

    @Test
    fun when_HttpException_thrown_should_retry(){

        val input = TransactionsTestHelper.fixedIncomes.first()
        val errResponse: Response<*> = mockk()
        every { errResponse.message() } returns "Error"
        every { errResponse.code() } returns 502
        coEvery { dao.getFixedIncome(any()) } returns RoomFixedIncome.fromFixedIncome(input,1)
        coEvery { restApi.addFixedIncome(any()) } throws HttpException(errResponse)

        val data = Data.Builder()
        data.putString("transactionId" ,"123")

        val worker = TestListenableWorkerBuilder<AddFixedIncomeWorker>(context)
            .setInputData(data.build())
            .build()
        runBlocking {
            val result = worker.doWork()
            assertEquals(ListenableWorker.Result.retry(), result)
        }
    }

    @Test
    fun when_Exception_thrown_should_fail(){

        val input = TransactionsTestHelper.fixedIncomes.first()

        coEvery { dao.getFixedIncome(any()) } returns RoomFixedIncome.fromFixedIncome(input,1)
        coEvery { restApi.addFixedIncome(any()) } throws Exception("Error")

        val data = Data.Builder()
        data.putString("transactionId" ,"123")

        val worker = TestListenableWorkerBuilder<AddFixedIncomeWorker>(context)
            .setInputData(data.build())
            .build()
        runBlocking {
            val result = worker.doWork()
            assertEquals(ListenableWorker.Result.failure(), result)
        }
    }
}