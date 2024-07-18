package com.example.budgetapp.presentation.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.example.budgetapp.R
import com.example.budgetapp.domain.models.transaction.FixedTransaction
import com.example.budgetapp.domain.models.transaction.Transaction
import com.example.budgetapp.presentation.components.AddExpenseBottomSheet
import com.example.budgetapp.presentation.components.AddFExpenseBottomSheet
import com.example.budgetapp.presentation.components.AddFIncomeBottomSheet
import com.example.budgetapp.presentation.components.AddIncomeBottomSheet
import com.example.budgetapp.presentation.components.CardSaldo
import com.example.budgetapp.presentation.components.FixedTransactionsCard
import com.example.budgetapp.presentation.components.TransactionsCard
import com.example.budgetapp.presentation.graphs.Graph
import com.example.budgetapp.presentation.ui.theme.Green80
import com.example.budgetapp.presentation.viewModels.transactionBottomSheet.ExpenseBottomSheetViewModel
import com.example.budgetapp.presentation.viewModels.transactionBottomSheet.FExpenseBottomSheetViewModel
import com.example.budgetapp.presentation.viewModels.transactionBottomSheet.FIncomeBottomSheetViewModel
import com.example.budgetapp.presentation.viewModels.home.HomeViewModel
import com.example.budgetapp.presentation.viewModels.transactionBottomSheet.IncomeBottomSheetViewModel
import com.example.budgetapp.presentation.views.records.BottomBarScreen
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeView(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel
){
    KoinContext {

        val homeUiState by viewModel.uiState.collectAsStateWithLifecycle()
        val snackbarHostState = remember { SnackbarHostState() }

        val expenseBottomSheetViewModel = koinViewModel<ExpenseBottomSheetViewModel>()
        val incomeBottomSheetViewModel = koinViewModel<IncomeBottomSheetViewModel>()
        val fExpenseBottomSheetViewModel = koinViewModel<FExpenseBottomSheetViewModel>()
        val fIncomeBottomSheetViewModel = koinViewModel<FIncomeBottomSheetViewModel>()

        var isAddExpenseOpen by rememberSaveable { mutableStateOf(false) }
        var isAddIncomeOpen by rememberSaveable { mutableStateOf(false) }
        var isAddFixedIncomeOpen by rememberSaveable { mutableStateOf(false) }
        var isAddFixedExpenseOpen by rememberSaveable { mutableStateOf(false) }

        val scope = rememberCoroutineScope()

        LaunchedEffect(key1 = homeUiState) {
            //if(!homeUiState.errorMsg.isNullOrEmpty()) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        "${homeUiState.errorMsg}",
                        duration = SnackbarDuration.Long
                    )
                }
            //}
        }

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
                    snackbar,
                ) = createRefs()

                SnackbarHost(
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(snackbar) {
                            bottom.linkTo(parent.bottom, margin = 20.dp)
                        },
                    hostState = snackbarHostState){
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    )
                    {
                        Text(
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxWidth()
                                .padding(vertical = 30.dp)
                                .graphicsLayer {
                                    shadowElevation = 5f
                                }
                                .background(color = Color.Red),
                            text = "${ homeUiState.errorMsg }",
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

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
                        text = stringResource(R.string.greetings, "Vinícius"),
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
                        onReloadClicked = { viewModel.refresh() },
                        isLoading = homeUiState.isLoading
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
                        cardName = stringResource(R.string.expenses),
                        transactions = homeUiState.expenses as List<Transaction<*>>,
                        modifier = modifier,
                        onNewTransactionClicked = { isAddExpenseOpen = true },
                        onSeeMoreClicked = {navController.navigate(Graph.RECORDS + "?page=${BottomBarScreen.Expenses.route}")},
                        isLoading = homeUiState.isLoading
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
                        cardName = stringResource(R.string.incomes),
                        transactions = homeUiState.incomes as List<Transaction<*>>,
                        modifier = modifier,
                        expanded = false,
                        onNewTransactionClicked = { isAddIncomeOpen = true },
                        onSeeMoreClicked = { navController.navigate(Graph.RECORDS + "?page=${BottomBarScreen.Incomes.route}") },
                        isLoading = homeUiState.isLoading
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
                        cardName = stringResource(R.string.fixed_expenses),
                        transactions = homeUiState.fixedExpense as List<FixedTransaction<*>>,
                        modifier = modifier,
                        expanded = false,
                        onNewTransactionClicked = { isAddFixedExpenseOpen = true },
                        onSeeMoreClicked = { navController.navigate(Graph.RECORDS + "?page=${BottomBarScreen.Expenses.route}&fixed=${true}") }
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
                        cardName = stringResource(R.string.fixed_incomes),
                        transactions = homeUiState.fixedIncome as List<FixedTransaction<*>>,
                        modifier = modifier,
                        expanded = false,
                        onNewTransactionClicked = { isAddFixedIncomeOpen = true },
                        onSeeMoreClicked = { navController.navigate(Graph.RECORDS + "?page=${BottomBarScreen.Incomes.route}&fixed=${true}") }
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
                        isAddFixedIncomeOpen = false
                    },
                    bottomSheetViewModel = fIncomeBottomSheetViewModel
                )
            }

        }
    }
}
