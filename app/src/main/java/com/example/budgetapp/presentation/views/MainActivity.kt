package com.example.budgetapp.presentation.views

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.constraintlayout.compose.ConstraintLayout
import com.example.budgetapp.presentation.components.TransactionsCard

import com.example.budgetapp.presentation.components.CardSaldo
import com.example.budgetapp.domain.models.transaction.Transaction
import com.example.budgetapp.services.repository.expense.LocalExpenseRepository
import com.example.budgetapp.services.repository.income.LocalIncomeRepository
import com.example.budgetapp.presentation.ui.theme.BudgetAppTheme
import com.example.budgetapp.presentation.ui.theme.Green80


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BudgetAppTheme {
                App(modifier = Modifier)
            }
        }
    }
}

@Composable
fun App(modifier: Modifier = Modifier){
    Column(modifier  = modifier
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
                    text = "Olá, Vinícius!",
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
                    totalBalance = 1500.00,
                    incomeBalance = 1750.00,
                    expenseBalance = 250.00,
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
                    transactions = LocalExpenseRepository().fetchAll() as MutableList<Transaction>,
                    modifier = modifier,
                    onNewTransactionClicked = { println("Novo Gasto") },
                    onSeeMoreClicked = {println("Ver Mais Gastos")}
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
                    transactions = LocalIncomeRepository().fetchAll() as MutableList<Transaction>,
                    modifier = modifier,
                    expanded = false
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview(){
    BudgetAppTheme {
        App()
    }
}



