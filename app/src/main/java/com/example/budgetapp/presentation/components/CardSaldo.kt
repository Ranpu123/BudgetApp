package com.example.budgetapp.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.budgetapp.R
import com.example.budgetapp.presentation.ui.theme.BudgetAppTheme
import com.example.budgetapp.presentation.ui.theme.Green20
import com.example.budgetapp.presentation.ui.theme.Green40
import com.example.budgetapp.utils.formatCurrency

@Composable
fun CardSaldo(
    totalBalance: Double,
    incomeBalance: Double,
    expenseBalance: Double,
    modifier: Modifier = Modifier,
    onReloadClicked: () -> Unit = {},
    isLoading: Boolean = false
) {
    Surface(
        color = Green40,
        modifier = modifier,
        shape = RoundedCornerShape(29.dp)
    ) {
        if(isLoading) {
            loadingContent(modifier, onReloadClicked)
        }else{
            loadedContent(modifier, totalBalance, onReloadClicked, incomeBalance, expenseBalance)
        }
    }
}

@Composable
private fun loadingContent(modifier: Modifier, onReloadClicked: () -> Unit) {
    Column {
        Row(modifier = modifier.fillMaxWidth()) {
            Column(
                modifier = modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(id = R.string.balance),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    modifier = Modifier.shimmerEffect(),
                    text = "R$ 00.000,00",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.Transparent
                )
            }
            Column(
                modifier = modifier
                    .padding(end = 16.dp, top = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = stringResource(id = R.string.cd_update),
                    tint = Color.White,
                    modifier = Modifier.clickable { onReloadClicked() }
                )
            }
        }
        Surface(
            modifier = modifier.fillMaxWidth(),
            color = Green20,
            shape = RoundedCornerShape(29.dp)
        ) {
            Row(
                modifier = modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = stringResource(id = R.string.incomes),
                            tint = Color(0xFF52FF00)
                        )
                        Text(
                            stringResource(id = R.string.incomes),
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp
                        )
                    }
                    Text(
                        modifier = Modifier.shimmerEffect(),
                        text = "R$ 00.000,00",
                        textAlign = TextAlign.Start,
                        fontSize = 16.sp,
                        color = Color.Transparent
                    )
                }
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowUp,
                            contentDescription = stringResource(id = R.string.expenses),
                            tint = Color(0xFFFF0000)
                        )
                        Text(
                            stringResource(id = R.string.expenses),
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp
                        )
                    }
                    Text(
                        modifier = Modifier.shimmerEffect(),
                        text = "R$ 00.000,00",
                        textAlign = TextAlign.Start,
                        fontSize = 16.sp,
                        color = Color.Transparent
                    )
                }
            }
        }
    }
}

@Composable
private fun loadedContent(
    modifier: Modifier,
    totalBalance: Double,
    onReloadClicked: () -> Unit,
    incomeBalance: Double,
    expenseBalance: Double
) {
    Column {
        Row(modifier = modifier.fillMaxWidth()) {
            Column(
                modifier = modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(id = R.string.balance),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = formatCurrency(totalBalance),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }
            Column(
                modifier = modifier
                    .padding(end = 16.dp, top = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = stringResource(id = R.string.cd_update),
                    tint = Color.White,
                    modifier = Modifier.clickable { onReloadClicked() }
                )
            }

        }
        Surface(
            modifier = modifier.fillMaxWidth(),
            color = Green20,
            shape = RoundedCornerShape(29.dp)
        ) {
            Row(
                modifier = modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = stringResource(id = R.string.incomes),
                            tint = Color(0xFF52FF00)
                        )
                        Text(
                            stringResource(id = R.string.incomes),
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp
                        )
                    }
                    Text(
                        formatCurrency(incomeBalance),
                        textAlign = TextAlign.Start,
                        fontSize = 16.sp
                    )
                }
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowUp,
                            contentDescription = stringResource(id = R.string.expenses),
                            tint = Color(0xFFFF0000)
                        )
                        Text(
                            stringResource(id = R.string.expenses),
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp
                        )
                    }
                    Text(
                        formatCurrency(expenseBalance),
                        textAlign = TextAlign.End,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun cardSaldoPreview() {
    BudgetAppTheme {
        CardSaldo(
            totalBalance = 1500.00,
            expenseBalance = 250.00,
            incomeBalance = 1750.00,
            isLoading = false
        )
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun cardSaldoLoadingPreview() {
    BudgetAppTheme {
        CardSaldo(
            totalBalance = 1500.00,
            expenseBalance = 250.00,
            incomeBalance = 1750.00,
            isLoading = true
        )
    }
}