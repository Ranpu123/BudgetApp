package com.example.budgetapp.presentation.views.records

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.budgetapp.domain.models.transaction.Transaction
import com.example.budgetapp.presentation.components.DoubleSwitch
import com.example.budgetapp.presentation.components.RecordCard
import com.example.budgetapp.presentation.ui.theme.BudgetAppTheme
import com.example.budgetapp.presentation.ui.theme.Green80
import com.example.budgetapp.presentation.viewModels.records.RecordsViewModel

@Composable
fun RecordsOverview(
    modifier: Modifier = Modifier,
    viewModel: RecordsViewModel = RecordsViewModel()
){

    val UiState by viewModel.uiState.collectAsState()

    var transactions: List<Transaction<*>> = UiState.expenses + UiState.incomes
    var fixedTransactions: List<Transaction<*>> = UiState.fixedExpense + UiState.fixedIncome

    var isFixos by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        ConstraintLayout(
            modifier = Modifier
        ) {

            val (
                switch,
                title,
                card,
                backbtn,
                back,
                spacer,
            ) = createRefs()

            Surface(
                color = Green80,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
                    .constrainAs(back) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {}

            Surface(
                color = Color.Transparent,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .constrainAs(switch) {
                        bottom.linkTo(card.top, margin = 8.dp)
                    }
            ) {
                DoubleSwitch(
                    modifier = Modifier,
                    onRightClick = { isFixos = false },
                    onLeftClick = { isFixos = true }
                )
            }

            Icon(
                modifier = Modifier
                    .constrainAs(backbtn) {
                        start.linkTo(parent.start, margin = 16.dp)
                        bottom.linkTo(title.bottom)
                        top.linkTo(title.top)
                    },
                imageVector = Icons.Filled.KeyboardArrowLeft,
                contentDescription = null,
                tint = Color.White
            )
            Text(
                modifier = Modifier
                    .constrainAs(title) {
                        centerHorizontallyTo(parent)
                        bottom.linkTo(switch.top, margin = 3.dp)
                    },
                text = "Relatórios",
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
            )

            Surface(
                color = Color.Transparent,
                modifier = Modifier
                    .height(35.dp)
                    .fillMaxWidth()
                    .constrainAs(spacer) {
                        bottom.linkTo(back.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {}

            Surface(
                color = Color.Transparent,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .constrainAs(card) {
                        top.linkTo(spacer.top)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.preferredWrapContent
                    },

            ) {
                if (isFixos) {
                    RecordCard(
                        modifier = Modifier
                            .fillMaxHeight(),
                        transactions = fixedTransactions,
                        onDelete = {
                            viewModel.removeTransaction(it)
                            viewModel.updateAll()
                        }
                    )
                } else {
                    RecordCard(
                        modifier = Modifier
                            .fillMaxHeight(),
                        transactions = transactions,
                        onDelete = {
                            viewModel.removeTransaction(it)
                            viewModel.updateAll()
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRecordsOverview(){
    BudgetAppTheme {
        RecordsOverview()
    }
}