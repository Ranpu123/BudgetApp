package com.example.budgetapp.presentation.viewModels.records

import app.cash.turbine.test
import com.example.budgetapp.domain.models.income.Income
import com.example.budgetapp.domain.repository_interfaces.IExpenseRepository
import com.example.budgetapp.domain.repository_interfaces.IFixedExpenseRepository
import com.example.budgetapp.domain.repository_interfaces.IFixedIncomeRepository
import com.example.budgetapp.domain.repository_interfaces.IIncomeRepository
import com.example.budgetapp.presentation.viewModels.home.HomeViewModel
import com.example.budgetapp.testUtils.TransactionsTestHelper
import io.mockk.clearAllMocks
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.lang.RuntimeException

@OptIn(ExperimentalCoroutinesApi::class)
class RecordsViewModelTest{

    private val expenseRepository: IExpenseRepository = mockk(relaxed = true)
    private val incomeRepository: IIncomeRepository = mockk(relaxed = true)
    private val fixedExpenseRepository: IFixedExpenseRepository = mockk(relaxed = true)
    private val fixedIncomeRepository: IFixedIncomeRepository = mockk(relaxed = true)
    private lateinit var viewModel: RecordsViewModel

    @Before
    fun setup(){
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = RecordsViewModel(
            incomeRepository = incomeRepository,
            fixedIncomeRepository = fixedIncomeRepository,
            fixedExpenseRepository = fixedExpenseRepository,
            expenseRepository = expenseRepository
        )
        clearAllMocks()
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `Initial state should be loading state`() = runTest {
        val uiState = viewModel.uiState.take(1).toList()

        assertEquals(1, uiState.size)
        assertEquals(RecordsUiState(isLoading = true), uiState[0])
    }
    @Test
    fun `When collecting flows should update state`() = runTest{

        coEvery { incomeRepository.fetchAll() } returns flowOf(TransactionsTestHelper.incomes)
        coEvery { expenseRepository.fetchAll() } returns flowOf(TransactionsTestHelper.expenses)
        coEvery { fixedIncomeRepository.fetchAll() } returns flowOf(TransactionsTestHelper.fixedIncomes)
        coEvery { fixedExpenseRepository.fetchAll() } returns flowOf(TransactionsTestHelper.fixedExpenses)

        viewModel = RecordsViewModel(
            incomeRepository = incomeRepository,
            fixedIncomeRepository = fixedIncomeRepository,
            fixedExpenseRepository = fixedExpenseRepository,
            expenseRepository = expenseRepository
        )

        val uiState = viewModel.uiState.take(2).toList()

        val expected = RecordsUiState(
            isLoading = false,
            errorMsg = null,
            incomes = TransactionsTestHelper.incomes,
            expenses = TransactionsTestHelper.expenses,
            fixedExpense = TransactionsTestHelper.fixedExpenses,
            fixedIncome = TransactionsTestHelper.fixedIncomes
        )

        assertEquals(2, uiState.size)
        assertEquals(true, uiState[0].isLoading)

        assertEquals(expected, uiState[1])
    }

    @Test
    fun `When flow emits exception should update to error state`() = runTest{
        Dispatchers.setMain(UnconfinedTestDispatcher())

        coEvery { incomeRepository.fetchAll() } returns flow<List<Income>> { throw Exception("Fake Error") }
        coEvery { expenseRepository.fetchAll() } returns flowOf(TransactionsTestHelper.expenses)
        coEvery { fixedIncomeRepository.fetchAll() } returns flowOf(TransactionsTestHelper.fixedIncomes)
        coEvery { fixedExpenseRepository.fetchAll() } returns flowOf(TransactionsTestHelper.fixedExpenses)

        viewModel = RecordsViewModel(
            incomeRepository = incomeRepository,
            fixedIncomeRepository = fixedIncomeRepository,
            fixedExpenseRepository = fixedExpenseRepository,
            expenseRepository = expenseRepository
        )

        advanceUntilIdle()
        viewModel.uiState.test {
            assertEquals("Fake Error",awaitItem().errorMsg)
        }

    }

    @Test
    fun `When removeTransaction() with income is called should remove income`() = runTest{
        viewModel.removeTransaction(TransactionsTestHelper.incomes.first())

        advanceUntilIdle()

        coVerify(exactly = 1) { incomeRepository.removeIncome(any()) }
        coVerify(exactly = 0) { expenseRepository.removeExpense(any()) }
        coVerify(exactly = 0) { fixedIncomeRepository.removeFixedIncome(any()) }
        coVerify(exactly = 0) { fixedExpenseRepository.removeFixedExpense(any()) }
    }

    @Test
    fun `When removeTransaction() with expense is called should remove expense`() = runTest{
        viewModel.removeTransaction(TransactionsTestHelper.expenses.first())

        advanceUntilIdle()

        coVerify(exactly = 0) { incomeRepository.removeIncome(any()) }
        coVerify(exactly = 1) { expenseRepository.removeExpense(any()) }
        coVerify(exactly = 0) { fixedIncomeRepository.removeFixedIncome(any()) }
        coVerify(exactly = 0) { fixedExpenseRepository.removeFixedExpense(any()) }
    }

    @Test
    fun `When removeTransaction() with fixed expense is called should remove fixed expense`() = runTest{
        viewModel.removeTransaction(TransactionsTestHelper.fixedExpenses.first())

        advanceUntilIdle()

        coVerify(exactly = 0) { incomeRepository.removeIncome(any()) }
        coVerify(exactly = 0) { expenseRepository.removeExpense(any()) }
        coVerify(exactly = 0) { fixedIncomeRepository.removeFixedIncome(any()) }
        coVerify(exactly = 1) { fixedExpenseRepository.removeFixedExpense(any()) }
    }

    @Test
    fun `When removeTransaction() with fixed income is called should remove fixed income`() = runTest{
        viewModel.removeTransaction(TransactionsTestHelper.fixedIncomes.first())

        advanceUntilIdle()

        coVerify(exactly = 0) { incomeRepository.removeIncome(any()) }
        coVerify(exactly = 0) { expenseRepository.removeExpense(any()) }
        coVerify(exactly = 1) { fixedIncomeRepository.removeFixedIncome(any()) }
        coVerify(exactly = 0) { fixedExpenseRepository.removeFixedExpense(any()) }
    }

    @Test
    fun `When removeTransaction() with transaction is called should not remove`() = runTest{
        viewModel.removeTransaction(TransactionsTestHelper.transactions.first())

        advanceUntilIdle()

        coVerify(exactly = 0) { incomeRepository.removeIncome(any()) }
        coVerify(exactly = 0) { expenseRepository.removeExpense(any()) }
        coVerify(exactly = 0) { fixedIncomeRepository.removeFixedIncome(any()) }
        coVerify(exactly = 0) { fixedExpenseRepository.removeFixedExpense(any()) }
    }


}