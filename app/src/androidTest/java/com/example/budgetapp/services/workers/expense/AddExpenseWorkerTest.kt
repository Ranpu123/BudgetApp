package com.example.budgetapp.services.workers.expense

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Data
import androidx.work.ListenableWorker.Result
import androidx.work.testing.TestListenableWorkerBuilder
import com.example.budgetapp.KoinTestRule
import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.services.local.dao.expense.ExpenseDao
import com.example.budgetapp.services.local.models.RoomExpense
import com.example.budgetapp.services.remote.IBudgetAppAPI
import com.example.budgetapp.services.workers.utils.isConnected
import com.example.budgetapp.testUtils.TransactionsTestHelper
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.sql.SQLException

class AddExpenseWorkerTest{

    private val dao: ExpenseDao = mockk(relaxed = true)
    private val restApi: IBudgetAppAPI = mockk(relaxed = true)
    private val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext

    private val koinModules = module {
        single<ExpenseDao> { dao }
        single<IBudgetAppAPI> { restApi }
        single<Context> { context }
    }

    @get:Rule
    val koinTestRule = KoinTestRule(
        modules = listOf(koinModules)
    )

    @Test
    fun when_data_is_not_set_should_fail() {
        val worker = TestListenableWorkerBuilder<AddExpenseWorker>(context)
            .build()
        runBlocking {
            val result = worker.doWork()
            assertEquals(Result.failure(), result)
        }
    }

    @Test
    fun when_no_connection_found_should_retry() {
        var data = Data.Builder()
        data.putString("transactionId" ,"123")
        data.putInt("userId", 1)

        InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("svc wifi disable")
        InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("svc data disable")

        val worker = TestListenableWorkerBuilder<AddExpenseWorker>(context)
            .setInputData(data.build())
            .build()
        runBlocking {
            val result = worker.doWork()
            assertEquals(Result.retry(), result)
        }

        InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("svc wifi enable")
        InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("svc data enable")
    }

    @Test
    fun should_succeed(){

        var mockResponse: Response<Expense> = mockk()
        var input = TransactionsTestHelper.expenses.first()

        coEvery { dao.getExpense(any()) } returns RoomExpense.fromExpense(input,1)
        coEvery { restApi.addExpense(any()) } returns mockResponse
        coEvery { dao.update(any<RoomExpense>()) } returns 1
        every { mockResponse.isSuccessful } returns true

        var data = Data.Builder()
        data.putString("transactionId" ,"123")
        data.putInt("userId", 1)

        val worker = TestListenableWorkerBuilder<AddExpenseWorker>(context)
            .setInputData(data.build())
            .build()
        runBlocking {
            val result = worker.doWork()
            assertEquals(Result.success(), result)
        }
    }

    @Test
    fun when_SQLException_thrown_should_retry(){

        coEvery { dao.getExpense(any()) } throws SQLException("Error")

        var data = Data.Builder()
        data.putString("transactionId" ,"123")
        data.putInt("userId", 1)

        val worker = TestListenableWorkerBuilder<AddExpenseWorker>(context)
            .setInputData(data.build())
            .build()
        runBlocking {
            val result = worker.doWork()
            assertEquals(Result.retry(), result)
        }
    }

    @Test
    fun when_IOException_thrown_should_retry(){

        var input = TransactionsTestHelper.expenses.first()

        coEvery { dao.getExpense(any()) } returns RoomExpense.fromExpense(input,1)
        coEvery { restApi.addExpense(any()) } throws IOException("Error")

        var data = Data.Builder()
        data.putString("transactionId" ,"123")
        data.putInt("userId", 1)

        val worker = TestListenableWorkerBuilder<AddExpenseWorker>(context)
            .setInputData(data.build())
            .build()
        runBlocking {
            val result = worker.doWork()
            assertEquals(Result.retry(), result)
        }
    }

    @Test
    fun when_HttpException_thrown_should_retry(){

        var input = TransactionsTestHelper.expenses.first()
        var errResponse:Response<*> = mockk()
        every { errResponse.message() } returns "Error"
        every { errResponse.code() } returns 502
        coEvery { dao.getExpense(any()) } returns RoomExpense.fromExpense(input,1)
        coEvery { restApi.addExpense(any()) } throws HttpException(errResponse)

        var data = Data.Builder()
        data.putString("transactionId" ,"123")
        data.putInt("userId", 1)

        val worker = TestListenableWorkerBuilder<AddExpenseWorker>(context)
            .setInputData(data.build())
            .build()
        runBlocking {
            val result = worker.doWork()
            assertEquals(Result.retry(), result)
        }
    }

    @Test
    fun when_Exception_thrown_should_fail(){

        var input = TransactionsTestHelper.expenses.first()

        coEvery { dao.getExpense(any()) } returns RoomExpense.fromExpense(input,1)
        coEvery { restApi.addExpense(any()) } throws Exception("Error")

        var data = Data.Builder()
        data.putString("transactionId" ,"123")
        data.putInt("userId", 1)

        val worker = TestListenableWorkerBuilder<AddExpenseWorker>(context)
            .setInputData(data.build())
            .build()
        runBlocking {
            val result = worker.doWork()
            assertEquals(Result.failure(), result)
        }
    }
}