package com.example.budgetapp.presentation.viewModels.records

import androidx.lifecycle.ViewModel
import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.domain.models.expense.FixedExpense
import com.example.budgetapp.domain.models.income.FixedIncome
import com.example.budgetapp.domain.models.income.Income
import com.example.budgetapp.domain.models.transaction.Transaction
import com.example.budgetapp.domain.repository_interfaces.IExpenseRepository
import com.example.budgetapp.domain.repository_interfaces.IFixedExpenseRepository
import com.example.budgetapp.domain.repository_interfaces.IFixedIncomeRepository
import com.example.budgetapp.domain.repository_interfaces.IIncomeRepository
import com.example.budgetapp.services.repository.fixed_expense.LocalFixedExpenseRepository
import com.example.budgetapp.services.repository.fixed_income.LocalFixedIncomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecordsViewModel(
    private val expenseRepository: IExpenseRepository,
    private val incomeRepository: IIncomeRepository,
    private val fixedExpenseRepository: IFixedExpenseRepository = LocalFixedExpenseRepository,
    private val fixedIncomeRepository: IFixedIncomeRepository = LocalFixedIncomeRepository,
): ViewModel() {

    private var _expenses = mutableListOf<Expense>()
    private var _incomes = mutableListOf<Income>()

    private var _fixedIncomes = mutableListOf<FixedIncome>()
    private var _fixedExpenses = mutableListOf<FixedExpense>()

    private val _uiState = MutableStateFlow(RecordsUiState())
    val uiState: StateFlow<RecordsUiState> = _uiState.asStateFlow()

    init {
        updateAll()
    }

    private fun fetchData(){
        _expenses = expenseRepository.fetchAll()
        _incomes = incomeRepository.fetchAll()
        _fixedExpenses = fixedExpenseRepository.fetchAll()
        _fixedIncomes = fixedIncomeRepository.fetchAll()
    }

    private fun updateState(){
        _uiState.value = RecordsUiState(
            expenses = _expenses,
            incomes = _incomes,
            fixedExpense = _fixedExpenses,
            fixedIncome = _fixedIncomes,
        )
    }

    fun updateAll(){
        fetchData()
        updateState()
    }

    fun removeTransaction(transaction: Transaction<*>){
        when(transaction){
            is Income -> incomeRepository.removeIncome(transaction)
            is Expense -> expenseRepository.removeExpense(transaction)
            is FixedExpense -> fixedExpenseRepository.removeFixedExpense(transaction)
            is FixedIncome -> fixedIncomeRepository.removeFixedIncome(transaction)
        }
    }
}