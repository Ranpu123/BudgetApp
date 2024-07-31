package com.example.budgetapp.services.repository.income

import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.budgetapp.services.local.dao.fixed_expense.FixedExpenseDao
import com.example.budgetapp.services.local.dao.income.IncomeDao
import com.example.budgetapp.services.local.models.RoomFixedExpense
import com.example.budgetapp.services.local.models.RoomIncome
import com.example.budgetapp.services.repository.fixed_expense.SyncFixedExpenseRepository
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

class SyncIncomeRepositoryTest{
    private val dao: IncomeDao =  mockk(relaxed = true)
    private val workManager: WorkManager = mockk(relaxed = true)
    private lateinit var repository: SyncIncomeRepository

    @Before
    fun setup(){
        Dispatchers.setMain(StandardTestDispatcher())
        every { dao.fetchAll(any()) } returns flowOf(TransactionsTestHelper.incomes)
        coEvery { dao.add(any<RoomIncome>()) } returns 3L
        coEvery { dao.remove(any()) } returns 1
        repository = SyncIncomeRepository(
            dao = dao,
            workManager = workManager
        )
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `Should fetch all local incomes`() = runTest{
        var res = repository.fetchAll().take(1).toList()
        assertEquals(TransactionsTestHelper.incomes, res[0])
    }

    @Test
    fun `Should add income and launch sync worker`() = runTest{
        var input = TransactionsTestHelper.incomes.first()
        val n = repository.addIncome(input)

        assertEquals(3L, n)

        coVerifyOrder {
            dao.add(any<RoomIncome>())
            workManager.enqueue(any<WorkRequest>())
        }
    }

    @Test
    fun `Should remove income and launch sync worker`() = runTest {
        var input = TransactionsTestHelper.incomes.first()
        val n = repository.removeIncome(input)

        assertEquals(1, n)

        coVerifyOrder {
            dao.remove(any())
            workManager.enqueue(any<WorkRequest>())
        }
    }
}