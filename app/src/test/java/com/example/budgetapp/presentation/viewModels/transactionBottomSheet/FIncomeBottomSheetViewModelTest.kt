package com.example.budgetapp.presentation.viewModels.transactionBottomSheet

import android.content.Context
import com.example.budgetapp.domain.models.income.FixedIncome
import com.example.budgetapp.domain.models.income.IncomeCategory
import com.example.budgetapp.domain.repository_interfaces.IFixedIncomeRepository
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

class FIncomeBottomSheetViewModelTest: KoinTest{
    private var context: Context = mockk(relaxed = true)
    private lateinit var viewModel: FIncomeBottomSheetViewModel
    private val repository: IFixedIncomeRepository = mockk(relaxed = true)
    private val validateDescription: ValidateTransactionDescription = mockk(relaxed = true)
    private val validateValue: ValidateTransactionValue = mockk(relaxed = true)
    private val today = Instant.now().toEpochMilli()

    @Before
    fun setup(){
        Dispatchers.setMain(UnconfinedTestDispatcher())
        every { context.getString(any()) } returns "TEXTO STUB"
        mockkStatic(Instant::class)
        every { Instant.now().toEpochMilli() } returns today

        viewModel = FIncomeBottomSheetViewModel(
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
        every { validateDescription.execute(description) } returns ValidateResult(true, "Description Error")

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
        assertEquals(IncomeCategory.OTHER, viewModel.category.value)

    }

    @Test
    fun `when checkform() is successful should add new fixed income`() = runTest{

        var id = UUID.randomUUID()
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns id

        every { validateValue.execute(50.00) } returns ValidateResult(true, null)
        every { validateDescription.execute("description") } returns ValidateResult(true, null)

        var entry = FixedIncome(
            value = 50.0,
            category = IncomeCategory.OTHER,
            description = "description",
            date = LocalDateTime.now()
        )

        viewModel.value.value = "R$ 50,00"
        viewModel.description.value = entry.description
        viewModel.category.value = entry.category
        viewModel.date.value = entry.date.toInstant(ZoneOffset.UTC).toEpochMilli()

        viewModel.checkForm()

        assertTrue(viewModel.uiState.value.isDone)

        coVerify(exactly = 1) { repository.addFixedIncome(any()) }
    }

    @Test
    fun `when checkform() fails should not add new fixed income`() = runTest{

        var id = UUID.randomUUID()
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns id

        every { validateValue.execute(0.00) } returns ValidateResult(false, "value can't be zero")
        every { validateDescription.execute("") } returns ValidateResult(false, "empty description")

        var entry = FixedIncome(
            value = 50.0,
            category = IncomeCategory.OTHER,
            description = "description",
            date = LocalDateTime.now()
        )

        viewModel.value.value = "R$ 0,00"
        viewModel.description.value = ""
        viewModel.category.value = entry.category
        viewModel.date.value = entry.date.toInstant(ZoneOffset.UTC).toEpochMilli()

        viewModel.checkForm()

        assertFalse(viewModel.uiState.value.isDone)

        coVerify(exactly = 0) { repository.addFixedIncome(any()) }
    }
}