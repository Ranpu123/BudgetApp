package com.example.budgetapp.presentation.viewModels.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.domain.models.expense.FixedExpense
import com.example.budgetapp.domain.models.income.FixedIncome
import com.example.budgetapp.domain.models.income.Income
import com.example.budgetapp.domain.models.transaction.FixedTransaction
import com.example.budgetapp.domain.repository_interfaces.IExpenseRepository
import com.example.budgetapp.domain.repository_interfaces.IFixedExpenseRepository
import com.example.budgetapp.domain.repository_interfaces.IFixedIncomeRepository
import com.example.budgetapp.domain.repository_interfaces.IIncomeRepository
import com.example.budgetapp.utils.validDayofMonth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.time.LocalTime

class HomeViewModel(
    private val expenseRepository: IExpenseRepository,
    private val incomeRepository: IIncomeRepository,
    private val fixedExpenseRepository: IFixedExpenseRepository,
    private val fixedIncomeRepository: IFixedIncomeRepository,
    ): ViewModel() {

    private val trigger = MutableSharedFlow<Unit>(replay = 1)

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<HomeUiState> = trigger.flatMapLatest {
        _uiState.value.copy(isLoading = true)
        combine(
            _uiState,
            incomeRepository.fetchAll().flowOn(Dispatchers.IO),
            expenseRepository.fetchAll().flowOn(Dispatchers.IO),
            fixedIncomeRepository.fetchAll().flowOn(Dispatchers.IO),
            fixedExpenseRepository.fetchAll().flowOn(Dispatchers.IO)
        ){ state, incomes, expenses, fixedIncome, fixedExpense ->
            state.copy(
                expenses = expenses,
                incomes = incomes,
                fixedExpense = fixedExpense,
                fixedIncome = fixedIncome,
                balance = getTotalBalance(incomes, expenses),
                incomeBalance = getIncomeBalance(incomes),
                expenseBalance = getExpenseBalance(expenses),
                userName = "Vinícius",
                isLoading = false
            )
        }.catch {
                _uiState.value.copy(
                    isLoading = false,
                    errorMsg = "${it.message}"
                )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = HomeUiState(isLoading = true)
    )

    init {
        triggerReload()
    }

    private var observeFixedTransaction = getObserveFixedTransaction()

    private fun getObserveFixedTransaction() = viewModelScope.launch {
            combine(
                fixedExpenseRepository.fetchAll().cancellable().flowOn(Dispatchers.IO)
                    .distinctUntilChanged(),
                fixedIncomeRepository.fetchAll().flowOn(Dispatchers.IO).distinctUntilChanged(),
            ) { fixedExpense, fixedIncome ->
                (fixedExpense + fixedIncome)
            }.distinctUntilChanged().collectLatest{
                checkDueTransactions(it)
            }

    }


    fun refresh(){
        triggerReload()
        observeFixedTransaction.cancel()
        observeFixedTransaction = getObserveFixedTransaction()
    }

    private fun triggerReload() {
        viewModelScope.launch {
            trigger.emit(Unit)
        }
    }

    private fun checkDueTransactions(fixedTransactions: List<FixedTransaction<*>>) {
        viewModelScope.launch {
            var updatedFixedExpense = mutableListOf<FixedExpense>()
            var updatedFixedIncome = mutableListOf<FixedIncome>()
            fixedTransactions.forEach() { transaction ->
                val delayedMonths = transaction.isDue()
                if (delayedMonths > 0) {
                    val curTime = LocalTime.now()
                    for (i in 1..delayedMonths) {
                        var date = transaction.lastDate
                            .plusMonths(1)
                            .withDayOfMonth(
                                validDayofMonth(
                                    transaction.date.dayOfMonth,
                                    transaction.lastDate
                                )
                            );

                        when (transaction) {
                            is FixedExpense -> expenseRepository.addExpense(
                                Expense(
                                    date = date.atTime(curTime),
                                    value = transaction.value,
                                    category = transaction.category,
                                    description = transaction.description
                                )
                            )

                            is FixedIncome -> incomeRepository.addIncome(
                                Income(
                                    date = date.atTime(curTime),
                                    value = transaction.value,
                                    category = transaction.category,
                                    description = transaction.description
                                )
                            )
                        }
                        transaction.lastDate = date
                    }
                    when (transaction) {
                        is FixedExpense -> updatedFixedExpense.add(transaction)
                        is FixedIncome -> updatedFixedIncome.add(transaction)
                    }
                }
            }
            fixedIncomeRepository.updateFixedIncome(updatedFixedIncome)
            fixedExpenseRepository.updateFixedExpense(updatedFixedExpense)
        }
    }

    private fun getTotalBalance(incomes: List<Income>, expenses: List<Expense>): Double {
        var total: Double = 0.0
        total += getIncomeBalance(incomes)
        total += getExpenseBalance(expenses)
        return total
    }

    private fun getIncomeBalance(incomes: List<Income>): Double{
        var total: Double = 0.0
        total += incomes.sumOf { it.value }
        return total
    }

    private fun getExpenseBalance(expenses: List<Expense>): Double{
        var total: Double = 0.0
        total += expenses.sumOf { it.value }
        return total
    }
}