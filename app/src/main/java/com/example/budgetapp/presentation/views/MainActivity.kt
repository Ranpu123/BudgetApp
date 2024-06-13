@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.budgetapp.presentation.views

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api

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
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.example.budgetapp.domain.models.expense.EXPENSE_CATEGORIES
import com.example.budgetapp.domain.models.income.INCOME_CATEGORIES
import com.example.budgetapp.domain.models.transaction.FixedTransaction
import com.example.budgetapp.presentation.components.TransactionsCard

import com.example.budgetapp.presentation.components.CardSaldo
import com.example.budgetapp.domain.models.transaction.Transaction
import com.example.budgetapp.domain.modules.ExpenseBottomSheetModule
import com.example.budgetapp.domain.modules.IncomeBottomSheetModule
import com.example.budgetapp.domain.modules.homePageModule
import com.example.budgetapp.presentation.components.AddExpenseBottomSheet
import com.example.budgetapp.presentation.components.AddIncomeBottomSheet
import com.example.budgetapp.presentation.components.FixedTransactionsCard
import com.example.budgetapp.presentation.ui.theme.BudgetAppTheme
import com.example.budgetapp.presentation.ui.theme.Green80
import com.example.budgetapp.presentation.viewModels.ExpenseBottomSheetViewModel
import com.example.budgetapp.presentation.viewModels.HomeViewModel
import com.example.budgetapp.presentation.viewModels.IncomeBottomSheetViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf
import java.time.Instant
import java.time.ZoneId


class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startModules()

        enableEdgeToEdge()
        setContent {
            BudgetAppTheme {
                HomeView(modifier = Modifier, homeViewModel)
            }
        }
    }

    fun startModules(){
        startKoin(){
            androidLogger()
            androidContext(this@MainActivity)
            modules(homePageModule, ExpenseBottomSheetModule, IncomeBottomSheetModule)
        }
    }
}



@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel
){
    val homeUiState by homeViewModel.uiState.collectAsState()


    var isAddExpenseOpen by rememberSaveable {mutableStateOf(false)}
    var isAddIncomeOpen by rememberSaveable {mutableStateOf(false)}
    var isAddFixedIncomeOpen by rememberSaveable {mutableStateOf(false)}
    var isAddFixedExpenseOpen by rememberSaveable {mutableStateOf(false)}


    Surface(modifier  = modifier
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
                fixedIncomes,) = createRefs()

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
                    onReloadClicked = {homeViewModel.updateAll()}
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
                    onSeeMoreClicked = {/*TODO*/}
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
                TransactionsCard(
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
        if(isAddExpenseOpen){
            val expenseBottomSheetViewModel = koinViewModel<ExpenseBottomSheetViewModel>()
            AddExpenseBottomSheet(
                modifier = modifier.fillMaxWidth(),
                onDismiss = {
                    isAddExpenseOpen = false
                },
                onAdd = { description: String, value: Double, date: Long, category: EXPENSE_CATEGORIES ->
                    homeViewModel.addNewExpense(
                        description = description,
                        value = value,
                        category = category,
                        date = Instant.ofEpochMilli(date)
                            .atZone(ZoneId.of("UTC")).toLocalDate()
                    )
                    isAddExpenseOpen = false

                },
                bottomSheetViewModel = expenseBottomSheetViewModel
            )
        }

        if(isAddIncomeOpen){
            val incomeBottomSheetViewModel = koinViewModel<IncomeBottomSheetViewModel>()
            AddIncomeBottomSheet(
                modifier = modifier.fillMaxWidth(),
                onDismiss = {
                    isAddIncomeOpen = false
                },
                onAdd = { description: String, value: Double, date: Long, category: INCOME_CATEGORIES ->
                    homeViewModel.addNewIncome(
                        description = description,
                        value = value,
                        category = category,
                        date = Instant.ofEpochMilli(date)
                            .atZone(ZoneId.of("UTC")).toLocalDate()
                    )
                    isAddIncomeOpen = false
                },
                bottomSheetViewModel = incomeBottomSheetViewModel
            )
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



