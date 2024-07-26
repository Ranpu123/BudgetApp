package com.example.budgetapp.presentation.viewModels.home

import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkContinuation
import androidx.work.WorkManager
import app.cash.turbine.test
import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.domain.models.expense.ExpenseCategory
import com.example.budgetapp.domain.models.expense.FixedExpense
import com.example.budgetapp.domain.models.income.FixedIncome
import com.example.budgetapp.domain.models.income.Income
import com.example.budgetapp.domain.models.income.IncomeCategory
import com.example.budgetapp.domain.repository_interfaces.IExpenseRepository
import com.example.budgetapp.domain.repository_interfaces.IFixedExpenseRepository
import com.example.budgetapp.domain.repository_interfaces.IFixedIncomeRepository
import com.example.budgetapp.domain.repository_interfaces.IIncomeRepository
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
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
import java.time.LocalDate
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest{

    private val expenseRepository: IExpenseRepository = mockk(relaxed = true)
    private val incomeRepository: IIncomeRepository = mockk(relaxed = true)
    private val fixedExpenseRepository: IFixedExpenseRepository = mockk(relaxed = true)
    private val fixedIncomeRepository: IFixedIncomeRepository = mockk(relaxed = true)
    private val workManager: WorkManager = mockk(relaxed = true)

    //private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private lateinit var viewModel: HomeViewModel

    private val incomes = listOf(
        Income(
            value = 50.0,
            category = IncomeCategory.OTHER,
            description = "description",
            date = LocalDateTime.of(2024,7,23,12,0)
        ),
        Income(
            value = 43.21,
            category = IncomeCategory.GIFT,
            description = "Presente",
            date = LocalDateTime.of(2024,7,23,12,0)
        ),
    )

    private val expenses = listOf(
        Expense(
            value = -50.0,
            category = ExpenseCategory.OTHER,
            description = "description",
            date = LocalDateTime.of(2024,6,23,12,0)
        ),
        Expense(
            value = -43.21,
            category = ExpenseCategory.RESTAURANT,
            description = "Presente",
            date = LocalDateTime.of(2024,6,23,12,0)
        ),
    )

    private val fixedIncomes = listOf(
        FixedIncome(
            value = 50.0,
            category = IncomeCategory.OTHER,
            description = "description",
            date = LocalDateTime.of(2024,4,23,12,0),
            lastDate = LocalDate.of(2024,5,23),
        )
    )

    private val fixedExpenses = listOf(
        FixedExpense(
            value = -50.0,
            category = ExpenseCategory.OTHER,
            description = "description",
            date = LocalDateTime.of(2024,4,23,12,0),
            lastDate = LocalDate.of(2024,5,23),
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = HomeViewModel(
            expenseRepository,
            incomeRepository,
            fixedExpenseRepository,
            fixedIncomeRepository,
            workManager
        )
        clearAllMocks()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Initial state should be loading state`() = runTest {
        val uiState = viewModel.uiState.take(1).toList()

        assertEquals(1, uiState.size)
        assertEquals(HomeUiState(isLoading = true), uiState[0])
    }
    @Test
    fun `When collecting flows should update state`() = runTest{

        coEvery { incomeRepository.fetchAll() } returns flowOf(incomes)
        coEvery { expenseRepository.fetchAll() } returns flowOf(expenses)
        coEvery { fixedIncomeRepository.fetchAll() } returns flowOf(fixedIncomes)
        coEvery { fixedExpenseRepository.fetchAll() } returns flowOf(fixedExpenses)

        val uiState = viewModel.uiState.take(2).toList()

        val expected = HomeUiState(
            isLoading = false,
            expenseBalance = -93.21,
            incomeBalance = 93.21,
            errorMsg = null,
            userName = "Vinícius",
            balance = 0.0,
            incomes = incomes,
            expenses = expenses,
            fixedExpense = fixedExpenses,
            fixedIncome = fixedIncomes
        )

        assertEquals(2, uiState.size)
        assertEquals(true, uiState[0].isLoading)

        assertHomeUiStateEquals(expected, uiState[1])
    }

    @Test
    fun `When fixedTransaction is due should add Transactions and update fixedTransactions`() = runTest{

        coEvery { incomeRepository.fetchAll() } returns flowOf(incomes)
        coEvery { expenseRepository.fetchAll() } returns flowOf(expenses)
        coEvery { fixedIncomeRepository.fetchAll() } returns flowOf(fixedIncomes)
        coEvery { fixedExpenseRepository.fetchAll() } returns flowOf(fixedExpenses)

        viewModel = HomeViewModel(
            expenseRepository,
            incomeRepository,
            fixedExpenseRepository,
            fixedIncomeRepository,
            workManager
        )

        advanceUntilIdle()

        coVerify(exactly = 2) { incomeRepository.addIncome(any()) }
        coVerify(exactly = 2) { expenseRepository.addExpense(any()) }

        coVerify(exactly = 1) { fixedIncomeRepository.updateFixedIncome(any<List<FixedIncome>>()) }
        coVerify(exactly = 1) { fixedExpenseRepository.updateFixedExpense(any<List<FixedExpense>>()) }

    }

    @Test
    fun `When flow emits exception should update to error state`() = runTest{
        var errorFlow = flow<List<Income>> { RuntimeException("teste") }

        coEvery { incomeRepository.fetchAll() } returns errorFlow
        coEvery { expenseRepository.fetchAll() } returns flowOf(expenses)
        coEvery { fixedIncomeRepository.fetchAll() } returns flowOf(fixedIncomes)
        coEvery { fixedExpenseRepository.fetchAll() } returns flowOf(fixedExpenses)

        advanceUntilIdle()

        assertEquals("test",viewModel.uiState.value.errorMsg)

    }

    @Test
    fun `Should launch startup worker on initialization`(){

        viewModel = HomeViewModel(
            expenseRepository,
            incomeRepository,
            fixedExpenseRepository,
            fixedIncomeRepository,
            workManager
        )

        verify(exactly = 1) { workManager.beginUniqueWork(any(),any(), any<OneTimeWorkRequest>()) }
    }

    private fun assertHomeUiStateEquals(expected: HomeUiState, actual: HomeUiState) {
        assertEquals(expected.isLoading, actual.isLoading)
        assertEquals(expected.incomes, actual.incomes)
        assertEquals(expected.expenses, actual.expenses)
        assertEquals(expected.fixedIncome, actual.fixedIncome)
        assertEquals(expected.fixedExpense, actual.fixedExpense)
        assertEquals(expected.userName, actual.userName)
        assertEquals(expected.errorMsg, actual.errorMsg)
        assertEquals(expected.balance, actual.balance, 0.0001)
        assertEquals(expected.incomeBalance, actual.incomeBalance, 0.0001)
        assertEquals(expected.expenseBalance, actual.expenseBalance, 0.0001)
    }

}