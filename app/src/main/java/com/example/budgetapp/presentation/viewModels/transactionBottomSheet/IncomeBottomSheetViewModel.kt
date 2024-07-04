package com.example.budgetapp.presentation.viewModels.transactionBottomSheet

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetapp.domain.models.income.IncomeCategory
import com.example.budgetapp.domain.models.income.Income
import com.example.budgetapp.domain.repository_interfaces.IIncomeRepository
import com.example.budgetapp.domain.use_cases.ValidateTransactionDescription
import com.example.budgetapp.domain.use_cases.ValidateTransactionValue
import com.example.budgetapp.utils.formatCurrency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

class IncomeBottomSheetViewModel(
    private val repository: IIncomeRepository,
    private val validateDescription: ValidateTransactionDescription = ValidateTransactionDescription(),
    private val validateValue: ValidateTransactionValue = ValidateTransactionValue(),
    private val transaction: Income? = null,
): ViewModel() {

    private var _uiState = MutableStateFlow(TransactionBottomSheetUIState())
    val uiState: StateFlow<TransactionBottomSheetUIState> = _uiState.asStateFlow()

    var category = mutableStateOf( IncomeCategory.OTHER)
    var description = mutableStateOf(IncomeCategory.OTHER.asString())
    var value = mutableStateOf(formatCurrency("0"))
    var date = mutableStateOf(Instant.now().toEpochMilli())

    fun addNewTransaction(
        date: LocalDate,
        value: Double,
        category: IncomeCategory,
        description: String,
    ){
        viewModelScope.launch {
            repository.addIncome(
                Income(
                    date = date.atTime(LocalTime.now()),
                    value = if (value < 0.0) +value else value,
                    category = category,
                    description = description
                )
            )
        }
    }

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

    fun clearState(){
        _uiState.value = TransactionBottomSheetUIState()
        description.value = IncomeCategory.OTHER.asString()
        value.value = formatCurrency("0")
        date.value = Instant.now().toEpochMilli()
        category.value = IncomeCategory.OTHER
    }
}
