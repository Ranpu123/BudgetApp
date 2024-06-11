package com.example.budgetapp.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.domain.models.income.Income
import com.example.budgetapp.domain.models.transaction.Transaction

@Composable
fun ItemTransactionsCard(transaction: Transaction<*>, modifier: Modifier = Modifier){
    var nTransaction: Transaction<*>

    when(transaction){
        is Expense -> nTransaction = transaction as Expense
        is Income -> nTransaction = transaction as Income
        else -> throw IllegalArgumentException("Unsupported Transaction Type")
    }

    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp),
    ) {
        val (desc, valor) = createRefs()
        Text(
            modifier = modifier
                .constrainAs(valor) {
                    end.linkTo(parent.end)
                    centerVerticallyTo(parent)
                },
            color = if(nTransaction.value < 0.0) Color(0xFFFF0000) else Color(0xFF00BD40),
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            text = "${nTransaction.value}"
        )
        Text(
            modifier = modifier
                .constrainAs(desc) {
                    start.linkTo(parent.start)
                    centerVerticallyTo(parent)
                },
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            text = nTransaction.description
        )
    }
}