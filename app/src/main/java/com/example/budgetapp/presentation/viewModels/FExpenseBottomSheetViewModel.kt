package com.example.budgetapp.presentation.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.budgetapp.domain.models.expense.EXPENSE_CATEGORIES
import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.domain.models.expense.FixedExpense
import com.example.budgetapp.domain.repository_interfaces.IExpenseRepository
import com.example.budgetapp.domain.repository_interfaces.IFixedExpenseRepository
import com.example.budgetapp.domain.use_cases.ValidateTransactionDescription
import com.example.budgetapp.domain.use_cases.ValidateTransactionValue
import com.example.budgetapp.utils.formatCurrency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

class FExpenseBottomSheetViewModel(
    private val repository: IFixedExpenseRepository,
    private val validateDescription: ValidateTransactionDescription = ValidateTransactionDescription(),
    private val validateValue: ValidateTransactionValue = ValidateTransactionValue(),
    private val transaction: FixedExpense? = null,
): ViewModel() {

    private var _uiState = MutableStateFlow(TransactionBottomSheetUIState())
    val uiState: StateFlow<TransactionBottomSheetUIState> = _uiState.asStateFlow()

    var category = mutableStateOf( EXPENSE_CATEGORIES.OTHER)
    var description = mutableStateOf(EXPENSE_CATEGORIES.OTHER.displayName)
    var value = mutableStateOf(formatCurrency("0"))
    var date = mutableStateOf(Instant.now().toEpochMilli())

    fun validadeForm(description: String, value: Double): Boolean{
        val valueResult = validateValue.execute(value)
        val descriptionResult = validateDescription.execute(description)

        var hasError = listOf(
            valueResult,
            descriptionResult).any{!it.success}

        if(hasError){
            _uiState.value = TransactionBottomSheetUIState(
                descriptionError = descriptionResult.errorMessage,
                valueError = valueResult.errorMessage)
            return false
        }
        return true
    }

    fun addNewTransaction(
        date: LocalDate,
        value: Double,
        category: EXPENSE_CATEGORIES,
        description: String,
    ){
        repository.addFixedExpense(FixedExpense(
            date = date.atTime(LocalTime.now()),
            value = if(value > 0.0) -value else value,
            category = category,
            description = description
        ))
    }

    fun clearState(){
        _uiState.value = TransactionBottomSheetUIState()
        description.value = EXPENSE_CATEGORIES.OTHER.displayName
        value.value = formatCurrency("0")
        date.value = Instant.now().toEpochMilli()
        category.value = EXPENSE_CATEGORIES.OTHER
    }
}