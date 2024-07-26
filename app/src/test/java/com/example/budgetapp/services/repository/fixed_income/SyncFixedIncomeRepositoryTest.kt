package com.example.budgetapp.services.repository.fixed_income

import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.budgetapp.services.local.dao.fixed_expense.FixedExpenseDao
import com.example.budgetapp.services.local.dao.fixed_income.FixedIncomeDao
import com.example.budgetapp.services.local.models.RoomFixedExpense
import com.example.budgetapp.services.local.models.RoomFixedIncome
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

class SyncFixedIncomeRepositoryTest{
    private val dao: FixedIncomeDao =  mockk(relaxed = true)
    private val workManager: WorkManager = mockk(relaxed = true)
    private lateinit var repository: SyncFixedIncomeRepository

    @Before
    fun setup(){
        Dispatchers.setMain(StandardTestDispatcher())
        every { dao.fetchAll(any()) } returns flowOf(TransactionsTestHelper.fixedIncomes)
        coEvery { dao.add(any<RoomFixedIncome>()) } returns 3L
        coEvery { dao.remove(any()) } returns 1
        repository = SyncFixedIncomeRepository(
            dao = dao,
            workManager = workManager
        )
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `Should fetch all local fixed incomes`() = runTest{
        var res = repository.fetchAll().take(1).toList()
        assertEquals(TransactionsTestHelper.fixedIncomes, res[0])
    }

    @Test
    fun `Should add fixed income and launch sync worker`() = runTest{
        var input = TransactionsTestHelper.fixedIncomes.first()
        val n = repository.addFixedIncome(input)

        assertEquals(3L, n)

        coVerifyOrder {
            dao.add(any<RoomFixedIncome>())
            workManager.enqueue(any<WorkRequest>())
        }
    }

    @Test
    fun `Should remove fixed income and launch sync worker`() = runTest {
        var input = TransactionsTestHelper.fixedIncomes.first()
        val n = repository.removeFixedIncome(input)

        assertEquals(1, n)

        coVerifyOrder {
            dao.remove(any())
            workManager.enqueue(any<WorkRequest>())
        }
    }
}