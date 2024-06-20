package com.example.budgetapp.presentation.views

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.budgetapp.domain.models.transaction.FixedTransaction
import com.example.budgetapp.domain.models.transaction.Transaction
import com.example.budgetapp.presentation.components.AddExpenseBottomSheet
import com.example.budgetapp.presentation.components.AddFExpenseBottomSheet
import com.example.budgetapp.presentation.components.AddFIncomeBottomSheet
import com.example.budgetapp.presentation.components.AddIncomeBottomSheet
import com.example.budgetapp.presentation.components.CardSaldo
import com.example.budgetapp.presentation.components.FixedTransactionsCard
import com.example.budgetapp.presentation.components.RecordCard
import com.example.budgetapp.presentation.components.TransactionsCard
import com.example.budgetapp.presentation.ui.theme.Green80
import com.example.budgetapp.presentation.viewModels.ExpenseBottomSheetViewModel
import com.example.budgetapp.presentation.viewModels.FExpenseBottomSheetViewModel
import com.example.budgetapp.presentation.viewModels.FIncomeBottomSheetViewModel
import com.example.budgetapp.presentation.viewModels.HomeViewModel
import com.example.budgetapp.presentation.viewModels.IncomeBottomSheetViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinContext

@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel
){
    KoinContext {
        val homeUiState by homeViewModel.uiState.collectAsState()

        val expenseBottomSheetViewModel = koinViewModel<ExpenseBottomSheetViewModel>()
        val incomeBottomSheetViewModel = koinViewModel<IncomeBottomSheetViewModel>()
        val fExpenseBottomSheetViewModel = koinViewModel<FExpenseBottomSheetViewModel>()
        val fIncomeBottomSheetViewModel = koinViewModel<FIncomeBottomSheetViewModel>()

        var isAddExpenseOpen by rememberSaveable { mutableStateOf(false) }
        var isAddIncomeOpen by rememberSaveable { mutableStateOf(false) }
        var isAddFixedIncomeOpen by rememberSaveable { mutableStateOf(false) }
        var isAddFixedExpenseOpen by rememberSaveable { mutableStateOf(false) }


        Surface(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            ConstraintLayout {
                val (
                    card,
                    back,
                    text,
                    expenses,
                    incomes,
                    fixedExpenses,
                    fixedIncomes,
                ) = createRefs()

                Surface(
                    color = Green80,
                    modifier = modifier
                        .height(180.dp)
                        .fillMaxWidth()
                        .constrainAs(back) {
                            top.linkTo(parent.top)
                        }

                ) {}
                Surface(
                    color = Color.Transparent,
                    modifier = modifier
                        .padding(horizontal = 32.dp)
                        .constrainAs(text) {
                            bottom.linkTo(card.top)
                        }
                ) {
                    Text(
                        text = "Olá, ${homeUiState.userName}!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                Surface(
                    color = Color.Transparent,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .constrainAs(card) {
                            top.linkTo(back.bottom, margin = -(95.dp))
                        }
                ) {
                    CardSaldo(
                        totalBalance = homeUiState.balance,
                        incomeBalance = homeUiState.incomeBalance,
                        expenseBalance = homeUiState.expenseBalance,
                        modifier = modifier,
                        onReloadClicked = { homeViewModel.updateAll() }
                    )
                }
                Surface(
                    color = Color.Transparent,
                    modifier = modifier
                        .padding(horizontal = 16.dp)
                        .constrainAs(expenses) {
                            top.linkTo(card.bottom, margin = 16.dp)
                        }
                ) {
                    TransactionsCard(
                        cardName = "Gastos",
                        transactions = homeUiState.expenses as List<Transaction<*>>,
                        modifier = modifier,
                        onNewTransactionClicked = { isAddExpenseOpen = true },
                        onSeeMoreClicked = {/*TODO*/ }
                    )
                }
                Surface(
                    color = Color.Transparent,
                    modifier = modifier
                        .padding(horizontal = 16.dp)
                        .constrainAs(incomes) {
                            top.linkTo(expenses.bottom, margin = 16.dp)
                        }
                ) {
                    TransactionsCard(
                        cardName = "Receitas",
                        transactions = homeUiState.incomes as List<Transaction<*>>,
                        modifier = modifier,
                        expanded = false,
                        onNewTransactionClicked = { isAddIncomeOpen = true }
                    )
                }
                Surface(
                    color = Color.Transparent,
                    modifier = modifier
                        .padding(horizontal = 16.dp)
                        .constrainAs(fixedExpenses) {
                            top.linkTo(incomes.bottom, margin = 16.dp)
                        }
                ) {
                    FixedTransactionsCard(
                        cardName = "Gastos Fixos",
                        transactions = homeUiState.fixedExpense as List<FixedTransaction<*>>,
                        modifier = modifier,
                        expanded = false,
                        onNewTransactionClicked = { isAddFixedExpenseOpen = true }
                    )
                }
                Surface(
                    color = Color.Transparent,
                    modifier = modifier
                        .padding(horizontal = 16.dp)
                        .constrainAs(fixedIncomes) {
                            top.linkTo(fixedExpenses.bottom, margin = 16.dp)
                        }
                ) {
                    FixedTransactionsCard(
                        cardName = "Receitas Fixas",
                        transactions = homeUiState.fixedIncome as List<FixedTransaction<*>>,
                        modifier = modifier,
                        expanded = false,
                        onNewTransactionClicked = { isAddFixedIncomeOpen = true }
                    )
                }
            }

            if (isAddExpenseOpen) {
                AddExpenseBottomSheet(
                    modifier = modifier.fillMaxWidth(),
                    onDismiss = {
                        isAddExpenseOpen = false
                    },
                    onAdd = {
                        homeViewModel.updateAll()
                        isAddExpenseOpen = false
                    },
                    bottomSheetViewModel = expenseBottomSheetViewModel,
                )
            }

            if (isAddIncomeOpen) {

                AddIncomeBottomSheet(
                    modifier = modifier.fillMaxWidth(),
                    onDismiss = {
                        isAddIncomeOpen = false
                    },
                    onAdd = {
                        homeViewModel.updateAll()
                        isAddIncomeOpen = false
                    },
                    bottomSheetViewModel = incomeBottomSheetViewModel
                )
            }

            if (isAddFixedExpenseOpen) {

                AddFExpenseBottomSheet(
                    modifier = modifier.fillMaxWidth(),
                    onDismiss = {
                        isAddFixedExpenseOpen = false
                    },
                    onAdd = {
                        homeViewModel.updateAll()
                        isAddFixedExpenseOpen = false
                    },
                    bottomSheetViewModel = fExpenseBottomSheetViewModel
                )
            }

            if (isAddFixedIncomeOpen) {

                AddFIncomeBottomSheet(
                    modifier = modifier.fillMaxWidth(),
                    onDismiss = {
                        isAddFixedIncomeOpen = false
                    },
                    onAdd = {
                        homeViewModel.updateAll()
                        isAddFixedIncomeOpen = false
                    },
                    bottomSheetViewModel = fIncomeBottomSheetViewModel
                )
            }

        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun HomePreview(){
    BudgetAppTheme {
        HomeView()
    }
}*/