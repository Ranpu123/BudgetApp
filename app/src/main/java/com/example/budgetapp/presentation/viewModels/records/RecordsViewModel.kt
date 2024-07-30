package com.example.budgetapp.presentation.viewModels.records

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.domain.models.expense.FixedExpense
import com.example.budgetapp.domain.models.income.FixedIncome
import com.example.budgetapp.domain.models.income.Income
import com.example.budgetapp.domain.models.transaction.Transaction
import com.example.budgetapp.domain.repository_interfaces.IExpenseRepository
import com.example.budgetapp.domain.repository_interfaces.IFixedExpenseRepository
import com.example.budgetapp.domain.repository_interfaces.IFixedIncomeRepository
import com.example.budgetapp.domain.repository_interfaces.IIncomeRepository
import com.example.budgetapp.presentation.viewModels.home.HomeUiState
import com.example.budgetapp.services.repository.fixed_expense.LocalFixedExpenseRepository
import com.example.budgetapp.services.repository.fixed_income.LocalFixedIncomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RecordsViewModel(
    private val expenseRepository: IExpenseRepository,
    private val incomeRepository: IIncomeRepository,
    private val fixedExpenseRepository: IFixedExpenseRepository,
    private val fixedIncomeRepository: IFixedIncomeRepository,
): ViewModel() {

    private val _uiState = MutableStateFlow(RecordsUiState(isLoading = true))
    val uiState: StateFlow<RecordsUiState> = combine(
        _uiState,
        incomeRepository.fetchAll(),
        expenseRepository.fetchAll(),
        fixedIncomeRepository.fetchAll(),
        fixedExpenseRepository.fetchAll()
    ){state, incomes, expenses, fixedIncome, fixedExpense ->
        state.copy(
            expenses = expenses,
            incomes = incomes,
            fixedExpense = fixedExpense,
            fixedIncome = fixedIncome,
            isLoading = false,
        )
    }.catch {e->
        emit(
            RecordsUiState(
                isLoading = false,
                errorMsg = "${e.message}"
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = RecordsUiState(isLoading = true)
    )

    fun removeTransaction(transaction: Transaction<*>){
        viewModelScope.launch {
            when (transaction) {
                is Income -> incomeRepository.removeIncome(transaction)
                is Expense -> expenseRepository.removeExpense(transaction)
                is FixedExpense -> fixedExpenseRepository.removeFixedExpense(transaction)
                is FixedIncome -> fixedIncomeRepository.removeFixedIncome(transaction)
            }
        }
    }
}