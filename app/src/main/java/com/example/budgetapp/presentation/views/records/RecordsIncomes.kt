package com.example.budgetapp.presentation.views.records

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.budgetapp.R
import com.example.budgetapp.domain.models.transaction.Transaction
import com.example.budgetapp.presentation.components.AddExpenseBottomSheet
import com.example.budgetapp.presentation.components.AddFExpenseBottomSheet
import com.example.budgetapp.presentation.components.AddFIncomeBottomSheet
import com.example.budgetapp.presentation.components.AddIncomeBottomSheet
import com.example.budgetapp.presentation.components.DoubleSwitch
import com.example.budgetapp.presentation.components.RecordCard
import com.example.budgetapp.presentation.ui.theme.Green80
import com.example.budgetapp.presentation.viewModels.records.RecordsViewModel
import com.example.budgetapp.presentation.viewModels.transactionBottomSheet.ExpenseBottomSheetViewModel
import com.example.budgetapp.presentation.viewModels.transactionBottomSheet.FExpenseBottomSheetViewModel
import com.example.budgetapp.presentation.viewModels.transactionBottomSheet.FIncomeBottomSheetViewModel
import com.example.budgetapp.presentation.viewModels.transactionBottomSheet.IncomeBottomSheetViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinContext

@Composable
fun RecordsIncomes(
    modifier: Modifier = Modifier,
    viewModel: RecordsViewModel,
    isFixed: Boolean = false,
    onReturnClicked: () -> Unit = {}
) {
    KoinContext {
        val incomeBottomSheetViewModel = koinViewModel<IncomeBottomSheetViewModel>()
        val fIncomeBottomSheetViewModel = koinViewModel<FIncomeBottomSheetViewModel>()

        var isAddIncomeOpen by rememberSaveable { mutableStateOf(false) }
        var isAddFixedIncomeOpen by rememberSaveable { mutableStateOf(false) }

        val UiState by viewModel.uiState.collectAsState()

        var transactions: List<Transaction<*>> = UiState.incomes
        var fixedTransactions: List<Transaction<*>> = UiState.fixedIncome

        var isFixos by remember {
            mutableStateOf(isFixed)
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
                        onLeftClick = { isFixos = true },
                        startOnRight = isFixed
                    )
                }

                Icon(
                    modifier = Modifier
                        .clickable { onReturnClicked() }
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
                    text = stringResource(R.string.records),
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
                            },
                            onNewTransactionClicked = {
                                isAddFixedIncomeOpen = !isAddFixedIncomeOpen
                            },
                            isLoading = UiState.isLoading
                        )
                    } else {
                        RecordCard(
                            modifier = Modifier
                                .fillMaxHeight(),
                            transactions = transactions,
                            onDelete = {
                                viewModel.removeTransaction(it)
                            },
                            onNewTransactionClicked = { isAddIncomeOpen = !isAddIncomeOpen },
                            isLoading = UiState.isLoading
                        )
                    }
                }
            }
        }
        if (isAddIncomeOpen) {

            AddIncomeBottomSheet(
                modifier = Modifier.fillMaxWidth(),
                onDismiss = {
                    isAddIncomeOpen = false
                },
                onAdd = {
                    isAddIncomeOpen = false
                },
                bottomSheetViewModel = incomeBottomSheetViewModel
            )
        }

        if (isAddFixedIncomeOpen) {

            AddFIncomeBottomSheet(
                modifier = Modifier.fillMaxWidth(),
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