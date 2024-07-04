package com.example.budgetapp.presentation.viewModels.records

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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class RecordsViewModel(
    private val expenseRepository: IExpenseRepository,
    private val incomeRepository: IIncomeRepository,
    private val fixedExpenseRepository: IFixedExpenseRepository,
    private val fixedIncomeRepository: IFixedIncomeRepository,
): ViewModel() {

    private val _uiState = MutableStateFlow(RecordsUiState())
    val uiState: StateFlow<RecordsUiState> = _uiState.asStateFlow()

    init {
        updateAll()
    }

    fun loadObserver(){
        viewModelScope.launch{
            combine(
                incomeRepository.fetchAll(),
                expenseRepository.fetchAll(),
                fixedIncomeRepository.fetchAll(),
                fixedExpenseRepository.fetchAll()
            ){incomes, expenses, fixedIncome, fixedExpense ->

                RecordsUiState(
                    expenses = expenses,
                    incomes = incomes,
                    fixedExpense = fixedExpense,
                    fixedIncome = fixedIncome,
                )

            }.catch {
                _uiState.value = RecordsUiState(errorMsg = it.message)
            }.collect{
                _uiState.value = it
            }
        }
    }

    fun updateAll(){
        loadObserver()
    }

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