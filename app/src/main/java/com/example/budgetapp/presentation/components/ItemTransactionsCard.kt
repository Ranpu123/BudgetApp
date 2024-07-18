package com.example.budgetapp.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.domain.models.expense.ExpenseCategory
import com.example.budgetapp.domain.models.transaction.Transaction
import com.example.budgetapp.presentation.ui.theme.BudgetAppTheme
import com.example.budgetapp.utils.formatCurrency
import java.time.LocalDateTime

@Composable
fun ItemTransactionsCard(transaction: Transaction<*>, modifier: Modifier = Modifier){

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
            color = if(transaction.value < 0.0) Color(0xFFFF0000) else Color(0xFF00BD40),
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            text = formatCurrency(transaction.value)
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
            text = transaction.description
        )
    }
}

@Composable
fun ShimmerItemTransactionsCard(modifier: Modifier = Modifier){
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp),
    ) {
        val (desc, valor) = createRefs()
        Text(
            modifier = modifier
                .shimmerEffect()
                .constrainAs(valor) {
                    end.linkTo(parent.end)
                    centerVerticallyTo(parent)
                },
            color = Color.Transparent,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            text = "R$ 00000,00"
        )
        Text(
            modifier = modifier
                .shimmerEffect()
                .constrainAs(desc) {
                    start.linkTo(parent.start)
                    centerVerticallyTo(parent)
                },
            color = Color.Transparent,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            text = "Description PlaceHolder"
        )
    }
}

@Preview
@Composable
fun PreviewItemTransaction(){
    BudgetAppTheme {
        ShimmerItemTransactionsCard()

    }
}