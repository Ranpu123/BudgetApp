@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.budgetapp.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.budgetapp.R
import com.example.budgetapp.domain.models.ICategories
import com.example.budgetapp.domain.models.income.Income
import com.example.budgetapp.domain.models.transaction.Transaction
import com.example.budgetapp.presentation.ui.theme.BudgetAppTheme
import com.example.budgetapp.services.repository.income.LocalIncomeRepository
import androidx.compose.ui.res.stringResource
import com.example.budgetapp.services.dao.income.IncomeDao
import com.example.budgetapp.utils.sortByCategory
import com.example.budgetapp.utils.sortByMonth
import com.example.budgetapp.utils.toFormattedMonthYear

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecordCard(
    transactions: List<Transaction<*>>,
    onDelete: (Transaction<*>) -> Unit = {},
    modifier: Modifier = Modifier,
    onNewTransactionClicked: ()->Unit = { },
    showAdd: Boolean = true
){
    val dropMenuOptions = stringArrayResource(R.array.records_drop_menu_options)

    var filterBy by remember{ mutableStateOf(true) }

    var filters:Map<Any?, List<Transaction<*>>> by remember { mutableStateOf(mapOf()) }
    filters = sortByMonth(transactions)

    var dropDownSize by remember { mutableStateOf(Size.Zero) }

    Surface(
        modifier = modifier,
        border = BorderStroke(color = Color.Gray, width = 1.dp),
        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
    ){
        Column (
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    dropDownSize = coordinates.size.toSize()
                }
        ){
            Row(
                horizontalArrangement = if(showAdd) Arrangement.SpaceBetween else Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
            ) {

                if(showAdd) {
                    Button(
                        modifier = Modifier.size(24.dp),
                        onClick = { onNewTransactionClicked() },
                        shape = CircleShape,
                        contentPadding = PaddingValues(2.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = stringResource(R.string.cd_add_transaction),
                            tint = Color.Black
                        )
                    }
                }

                GenericDropDownMenu(
                    modifier = Modifier
                        .width(with(LocalDensity.current) { dropDownSize.width.toDp() / 3 }),
                    options = dropMenuOptions.toList(),
                    defaultSelected = dropMenuOptions[0],
                    onChoice = {
                        if(it == dropMenuOptions[0]){
                            filterBy = true
                            filters = sortByMonth(transactions)
                        }else{
                            filterBy = false
                            filters = sortByCategory(transactions)
                        }
                    }
                )
            }
            Divider()
            LazyColumn(
                modifier = Modifier
            ) {
                if (transactions.isNotEmpty()) {
                    filters.forEach { (_, items) ->

                        stickyHeader {
                            TransactionDateHeader(
                                total = items.sumOf { it.value },
                                title = if(filterBy) toFormattedMonthYear(items.first().date.toLocalDate())
                                            else (items.first().category as ICategories).asString()
                            )
                        }
                        items(
                            items = items,
                            key = { it.id }
                        ) { item ->
                            SwipeToDeleteContainer<Transaction<*>>(
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
                } else {
                    item(){
                        Text(
                            text = stringResource(R.string.nothing_found),
                            color = Color.Gray,
                            textAlign = TextAlign.Center)
                        Divider()
                    }
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
/*
@Preview(showBackground = true)
@Composable
fun PreviewRecordCard(){
    BudgetAppTheme {

        var transactions by remember {
            mutableStateOf(LocalIncomeRepository().fetchAll())
        }

        RecordCard(
            modifier = Modifier,
            transactions = transactions,
            onDelete = {
                LocalIncomeRepository.removeIncome(it as Income)
                transactions = LocalIncomeRepository.fetchAll()
            }
        )
    }
}*/