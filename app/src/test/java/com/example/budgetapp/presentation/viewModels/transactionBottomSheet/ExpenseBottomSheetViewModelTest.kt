package com.example.budgetapp.presentation.viewModels.transactionBottomSheet


import android.content.Context
import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.domain.models.expense.ExpenseCategory
import com.example.budgetapp.domain.repository_interfaces.IExpenseRepository
import com.example.budgetapp.domain.use_cases.ValidateResult
import com.example.budgetapp.domain.use_cases.ValidateTransactionDescription
import com.example.budgetapp.domain.use_cases.ValidateTransactionValue
import com.example.budgetapp.utils.formatCurrency
import io.mockk.coVerify

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.koin.test.KoinTest
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID


class ExpenseBottomSheetViewModelTest: KoinTest {

    private var context: Context = mockk(relaxed = true)
    private lateinit var viewModel: ExpenseBottomSheetViewModel
    private val repository: IExpenseRepository = mockk(relaxed = true)
    private val validateDescription: ValidateTransactionDescription = mockk(relaxed = true)
    private val validateValue: ValidateTransactionValue = mockk(relaxed = true)
    private val today = Instant.now().toEpochMilli()

    @Before
    fun setup(){
        Dispatchers.setMain(UnconfinedTestDispatcher())
        every { context.getString(any()) } returns "TEXTO STUB"
        mockkStatic(Instant::class)
        every { Instant.now().toEpochMilli() } returns today

        viewModel = ExpenseBottomSheetViewModel(
            repository,
            validateDescription,
            validateValue,
            context,
            null
        )
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `when validateForm() data is valid error message should be null`(){
        val description = "Outros"
        val value = 50.00

        every { validateValue.execute(value) } returns ValidateResult(true, null)
        every { validateDescription.execute(description) } returns ValidateResult(true, null)

        val res = viewModel.validateForm(description, value)

        assertTrue(res)
        assertNull(viewModel.uiState.value.descriptionError)
        assertNull(viewModel.uiState.value.valueError)
    }

    @Test
    fun `when validateForm() data is invalid should set error message`(){
        val description = ""
        val value = 0.0

        every { validateValue.execute(value) } returns ValidateResult(false, "Value Error")
        every { validateDescription.execute(description) } returns ValidateResult(false, "Description Error")

        val res = viewModel.validateForm(description, value)

        assertFalse(res)
        assertEquals("Value Error", viewModel.uiState.value.valueError)
        assertEquals("Description Error", viewModel.uiState.value.descriptionError)
    }

    @Test
    fun `when called clearState() should clear UI state`(){
        viewModel.clearState()

        assertEquals(TransactionBottomSheetUIState(), viewModel.uiState.value)
        assertEquals("TEXTO STUB",viewModel.description.value)
        assertEquals(formatCurrency("0"), viewModel.value.value)
        assertEquals(Instant.now().toEpochMilli(), viewModel.date.value)
        assertEquals(ExpenseCategory.OTHER, viewModel.category.value)

    }
    
    @Test
    fun `when checkform() is successful should add new expense`() = runTest{

        var id = UUID.randomUUID()
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns id

        every { validateValue.execute(50.00) } returns ValidateResult(true, null)
        every { validateDescription.execute("description") } returns ValidateResult(true, null)

        var entry = Expense(
            value = -50.0,
            category = ExpenseCategory.OTHER,
            description = "description",
            date = LocalDateTime.now()
        )

        viewModel.value.value = "R$ 50,00"
        viewModel.description.value = entry.description
        viewModel.category.value = entry.category
        viewModel.date.value = entry.date.toInstant(ZoneOffset.UTC).toEpochMilli()

        assertTrue(viewModel.checkForm())

        coVerify(exactly = 1) { repository.addExpense(any()) }
    }

    @Test
    fun `when checkform() fails should not add new expense`() = runTest{

        var id = UUID.randomUUID()
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns id

        every { validateValue.execute(0.00) } returns ValidateResult(false, "value can't be zero")
        every { validateDescription.execute("") } returns ValidateResult(false, "empty description")

        var entry = Expense(
            value = -50.0,
            category = ExpenseCategory.OTHER,
            description = "description",
            date = LocalDateTime.now()
        )

        viewModel.value.value = "R$ 0,00"
        viewModel.description.value = ""
        viewModel.category.value = entry.category
        viewModel.date.value = entry.date.toInstant(ZoneOffset.UTC).toEpochMilli()

        assertFalse(viewModel.checkForm())

        coVerify(exactly = 0) { repository.addExpense(any()) }
    }

}