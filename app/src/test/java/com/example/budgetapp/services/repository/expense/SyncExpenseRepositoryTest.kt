package com.example.budgetapp.services.repository.expense

import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.Operation
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import app.cash.turbine.test
import com.example.budgetapp.services.local.dao.expense.ExpenseDao
import com.example.budgetapp.services.local.models.RoomExpense
import com.example.budgetapp.services.workers.expense.AddExpenseWorker
import com.example.budgetapp.services.workers.expense.RemoveExpenseWorker
import com.example.budgetapp.services.workers.utils.createOneTimeWorkRequest
import com.example.budgetapp.testUtils.TransactionsTestHelper
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SyncExpenseRepositoryTest{

    private val dao: ExpenseDao =  mockk(relaxed = true)
    private val workManager: WorkManager = mockk(relaxed = true)
    private lateinit var repository: SyncExpenseRepository

    @Before
    fun setup(){
        Dispatchers.setMain(StandardTestDispatcher())
        every { dao.fetchAll(any()) } returns flowOf(TransactionsTestHelper.expenses)
        coEvery { dao.add(any<RoomExpense>()) } returns 3L
        coEvery { dao.remove(any()) } returns 1
        repository = SyncExpenseRepository(
            dao = dao,
            workManager = workManager
        )
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `Should fetch all local expenses`() = runTest{
        var res = repository.fetchAll().take(1).toList()
        assertEquals(TransactionsTestHelper.expenses, res[0])
    }

    @Test
    fun `Should add expense and launch sync worker`() = runTest{
        var input = TransactionsTestHelper.expenses.first()
        val n = repository.addExpense(input)

        assertEquals(3L, n)

        coVerifyOrder {
            dao.add(any<RoomExpense>())
            workManager.enqueue(any<WorkRequest>())
        }
    }

    @Test
    fun `Should remove expense and launch sync worker`() = runTest {
        var input = TransactionsTestHelper.expenses.first()
        val n = repository.removeExpense(input)

        assertEquals(1, n)

        coVerifyOrder {
            dao.remove(any())
            workManager.enqueue(any<WorkRequest>())
        }
    }
}