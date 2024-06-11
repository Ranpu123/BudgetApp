@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.budgetapp.presentation.views

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.budgetapp.presentation.components.TransactionsCard

import com.example.budgetapp.presentation.components.CardSaldo
import com.example.budgetapp.domain.models.transaction.Transaction
import com.example.budgetapp.presentation.components.AddExpenseBottomSheet
import com.example.budgetapp.presentation.ui.theme.BudgetAppTheme
import com.example.budgetapp.presentation.ui.theme.Green80
import com.example.budgetapp.presentation.viewModels.HomeViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BudgetAppTheme {
                HomeView(modifier = Modifier)
            }
        }
    }
}

@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = viewModel()
){
    val homeUiState by homeViewModel.uiState.collectAsState()

    var isAddExpenseOpen by rememberSaveable {mutableStateOf(false)}

    var isAddIncomeOpen by rememberSaveable {mutableStateOf(false)}

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
                incomes) = createRefs()

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
                    transactions = homeUiState.expenses as MutableList<Transaction>,
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
                    transactions = homeUiState.incomes as MutableList<Transaction>,
                    modifier = modifier,
                    expanded = false,
                    onNewTransactionClicked = { isAddIncomeOpen = true }
                )
            }
        }
        if(isAddExpenseOpen){
            AddExpenseBottomSheet(
                modifier = modifier.fillMaxWidth(),
                onDissmiss = {
                    isAddExpenseOpen = false
                },
                onAdd = { description: String, value: Double, date: Long, category: String ->
                    homeViewModel.addNewExpense(
                        description = description,
                        value = value,
                        category = category,
                        date = Instant.ofEpochMilli(date)
                            .atZone(ZoneId.of("UTC")).toLocalDate()
                    )
                    isAddExpenseOpen = false
                })
        }

        if(isAddIncomeOpen){
            AddExpenseBottomSheet(
                modifier = modifier.fillMaxWidth(),
                onDissmiss = {
                    isAddIncomeOpen = false
                },
                onAdd = { description: String, value: Double, date: Long, category: String ->
                    homeViewModel.addNewExpense(
                        description = description,
                        value = value,
                        category = category,
                        date = Instant.ofEpochMilli(date)
                            .atZone(ZoneId.of("UTC")).toLocalDate()
                    )
                    isAddIncomeOpen = false
                })
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



