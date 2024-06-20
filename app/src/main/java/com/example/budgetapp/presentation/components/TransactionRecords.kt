@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.budgetapp.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.budgetapp.domain.models.ICategories
import com.example.budgetapp.domain.models.income.Income
import com.example.budgetapp.domain.models.transaction.Transaction
import com.example.budgetapp.presentation.ui.theme.BudgetAppTheme
import com.example.budgetapp.services.repository.income.LocalIncomeRepository


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T>RecordCard(
    transactions: List<Transaction<T>>,
    onDelete: (Transaction<T>) -> Unit = {},
    modifier: Modifier = Modifier
)where T: Enum<T>, T: ICategories{

    var scrollstate = rememberScrollState()
    val filters = transactions.distinctBy { it.category }

    Surface(
        border = BorderStroke(color = Color.Gray, width = 1.dp),
        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
    ){
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .height(150.dp)
                .scrollable(state = scrollstate, orientation = Orientation.Vertical)
        ) {


            filters.forEach { filter ->

                val filtered = transactions.filter { it.category == filter.category }

                stickyHeader {
                    TransactionDateHeader(
                        total = filtered.sumOf { it.value },
                        title = filter.category.displayName
                    )
                }
                items(
                    items = filtered,
                    key = { it.id }
                ) { item ->
                    SwipeToDeleteContainer(
                        item = item,
                        onDelete = { onDelete(it) },
                        content = {
                            Surface(
                            ) {
                                ItemTransactionsCard(
                                    transaction = it,
                                    modifier = Modifier.padding(vertical = 5.dp)
                                )
                            }
                        }
                    )
                    Divider()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteBackground(
    swipeDismissState: DismissState
){
    val color = if(swipeDismissState.dismissDirection == DismissDirection.EndToStart){
        Color.Red
    }else{
        Color.Transparent
    }
    Box(
       modifier = Modifier
           .fillMaxSize()
           .background(color),
       contentAlignment = Alignment.CenterEnd
    ){
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = null,
            tint = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRecordCard(){
    BudgetAppTheme {

        var transactions by remember {
            mutableStateOf(LocalIncomeRepository.fetchAll())
        }

        RecordCard(
            transactions = transactions,
            onDelete = {
                LocalIncomeRepository.removeIncome(it as Income)
                transactions = LocalIncomeRepository.fetchAll()
            }
        )
    }
}