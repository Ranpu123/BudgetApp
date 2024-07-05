package com.example.budgetapp.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.budgetapp.R
import com.example.budgetapp.domain.models.transaction.FixedTransaction
import com.example.budgetapp.presentation.ui.theme.BudgetAppTheme
import com.example.budgetapp.services.dao.fixedIncome.FixedIncomeDao
import com.example.budgetapp.services.dao.fixedIncome.FixedIncomeDao_Impl
import com.example.budgetapp.services.repository.fixed_income.LocalFixedIncomeRepository

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FixedTransactionsCard(
    cardName: String,
    transactions: List<FixedTransaction<*>>,
    modifier: Modifier = Modifier,
    expanded: Boolean = true,
    onNewTransactionClicked: () -> Unit = {},
    onSeeMoreClicked: () -> Unit = {},
    isLoading: Boolean = false
){

    var expanded by rememberSaveable { mutableStateOf(expanded) }

    Surface(
        modifier = modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, color = Color(0xFFABABAB))
    ) {
        Column {
            Row(
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    modifier = modifier.size(24.dp),
                    onClick = { onNewTransactionClicked() },
                    shape = CircleShape,
                    contentPadding = PaddingValues(2.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(R.string.cd_add_transaction, cardName),
                        tint = Color.Black
                    )
                }
                Text(
                    text = cardName,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = stringResource(R.string.cd_expand_card),
                    tint = Color(0xFFABABAB),
                    modifier = modifier.clickable { expanded = !expanded }
                )
            }
            if (expanded) {
                Column(
                    modifier = modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Divider()
                    if(isLoading){
                        LazyColumn(modifier = modifier.height(110.dp)) {
                            stickyHeader {
                                ShimmerTransactionDateHeader()
                            }
                            items(3) {
                                ShimmerItemTransactionsCard()
                                Divider()
                            }
                        }
                    }else {
                        if (transactions.isNotEmpty()) {
                            LazyColumn(modifier = modifier.height(110.dp)) {
                                item {
                                    TransactionDateHeader(
                                        date = null,
                                        total = transactions.sumOf { it.value })
                                }
                                items(transactions.sortedBy { it.date }) { transaction ->
                                    ItemTransactionsCard(transaction = transaction)
                                    Divider()
                                }
                            }
                        } else {
                            Text(
                                modifier = modifier.clickable { onNewTransactionClicked() },
                                text = stringResource(R.string.nothing_found),
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                            Divider()
                        }
                    }

                    Text(
                        modifier = modifier.clickable { onSeeMoreClicked() },
                        text = stringResource(R.string.see_more),
                        color = Color(0xFF0077CD),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FixedTransactionCardPreview(){
    BudgetAppTheme {
        FixedTransactionsCard(
            cardName = "Receitas Fixas",
            transactions = FixedIncomeDao_Impl.getRequiredConverters() as List<FixedTransaction<*>>)
    }
}