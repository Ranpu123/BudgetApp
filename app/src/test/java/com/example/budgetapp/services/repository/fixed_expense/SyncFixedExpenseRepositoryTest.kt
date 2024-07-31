package com.example.budgetapp.services.repository.fixed_expense

import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.budgetapp.services.local.dao.expense.ExpenseDao
import com.example.budgetapp.services.local.dao.fixed_expense.FixedExpenseDao
import com.example.budgetapp.services.local.models.RoomExpense
import com.example.budgetapp.services.local.models.RoomFixedExpense
import com.example.budgetapp.services.repository.expense.SyncExpenseRepository
import com.example.budgetapp.testUtils.TransactionsTestHelper
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
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

class SyncFixedExpenseRepositoryTest{
    private val dao: FixedExpenseDao =  mockk(relaxed = true)
    private val workManager: WorkManager = mockk(relaxed = true)
    private lateinit var repository: SyncFixedExpenseRepository

    @Before
    fun setup(){
        Dispatchers.setMain(StandardTestDispatcher())
        every { dao.fetchAll(any()) } returns flowOf(TransactionsTestHelper.fixedExpenses)
        coEvery { dao.add(any<RoomFixedExpense>()) } returns 3L
        coEvery { dao.remove(any()) } returns 1
        repository = SyncFixedExpenseRepository(
            dao = dao,
            workManager = workManager
        )
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `Should fetch all local fixed expenses`() = runTest{
        var res = repository.fetchAll().take(1).toList()
        assertEquals(TransactionsTestHelper.fixedExpenses, res[0])
    }

    @Test
    fun `Should add fixed expense and launch sync worker`() = runTest{
        var input = TransactionsTestHelper.fixedExpenses.first()
        val n = repository.addFixedExpense(input)

        assertEquals(3L, n)

        coVerifyOrder {
            dao.add(any<RoomFixedExpense>())
            workManager.enqueue(any<WorkRequest>())
        }
    }

    @Test
    fun `Should remove fixed expense and launch sync worker`() = runTest {
        var input = TransactionsTestHelper.fixedExpenses.first()
        val n = repository.removeFixedExpense(input)

        assertEquals(1, n)

        coVerifyOrder {
            dao.remove(any())
            workManager.enqueue(any<WorkRequest>())
        }
    }
}