package com.example.budgetapp.presentation.viewModels.transactionBottomSheet

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetapp.domain.models.expense.ExpenseCategory
import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.domain.repository_interfaces.IExpenseRepository
import com.example.budgetapp.domain.use_cases.ValidateTransactionDescription
import com.example.budgetapp.domain.use_cases.ValidateTransactionValue
import com.example.budgetapp.utils.currencyToDouble
import com.example.budgetapp.utils.formatCurrency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

class ExpenseBottomSheetViewModel(
    private val repository: IExpenseRepository,
    private val validateDescription: ValidateTransactionDescription = ValidateTransactionDescription(),
    private val validateValue: ValidateTransactionValue = ValidateTransactionValue(),
    private val context: Context,
    private val transaction: Expense? = null,
): ViewModel() {

    private var _uiState = MutableStateFlow(TransactionBottomSheetUIState())
    val uiState: StateFlow<TransactionBottomSheetUIState> = _uiState.asStateFlow()

    var category = mutableStateOf( ExpenseCategory.OTHER)
    var description = mutableStateOf(ExpenseCategory.OTHER.asString(context))
    var value = mutableStateOf(formatCurrency("0"))
    var date = mutableStateOf(Instant.now().toEpochMilli())

    fun addNewTransaction(
        date: LocalDate,
        value: Double,
        category: ExpenseCategory,
        description: String,
    ){
        viewModelScope.launch {
            repository.addExpense(
                Expense(
                    date = date.atTime(LocalTime.now()),
                    value = if (value > 0.0) -value else value,
                    category = category,
                    description = description
                )
            )
        }
    }

    fun validateForm(description: String, value: Double): Boolean{
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

    fun clearState(){
        _uiState.value = TransactionBottomSheetUIState()
        description.value = ExpenseCategory.OTHER.asString(context)
        value.value = formatCurrency("0")
        date.value = Instant.now().toEpochMilli()
        category.value = ExpenseCategory.OTHER
    }

    fun checkForm(): Boolean {
        if(validateForm(this.description.value, currencyToDouble(this.value.value))){
            addNewTransaction(
                date = Instant.ofEpochMilli(date.value)
                    .atZone(ZoneId.of("UTC"))
                    .toLocalDate(),
                value = currencyToDouble(value.value),
                category = category.value,
                description = description.value
            )
            clearState()
            return true
        }else{
            return false
        }
    }
}