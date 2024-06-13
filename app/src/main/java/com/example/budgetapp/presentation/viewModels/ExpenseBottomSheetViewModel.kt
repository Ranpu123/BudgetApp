package com.example.budgetapp.presentation.viewModels

import androidx.lifecycle.ViewModel
import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.domain.repository_interfaces.IExpenseRepository
import com.example.budgetapp.domain.use_cases.ValidateTransactionDescription
import com.example.budgetapp.domain.use_cases.ValidateTransactionValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ExpenseBottomSheetViewModel(
    private val expenseRepository: IExpenseRepository,
    private val validateDescription: ValidateTransactionDescription = ValidateTransactionDescription(),
    private val validateValue: ValidateTransactionValue = ValidateTransactionValue(),
    private val expense: Expense? = null,
): ViewModel() {

    private var _uiState = MutableStateFlow(TransactionBottomSheetUIState())
    val uiState: StateFlow<TransactionBottomSheetUIState> = _uiState.asStateFlow()

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

}