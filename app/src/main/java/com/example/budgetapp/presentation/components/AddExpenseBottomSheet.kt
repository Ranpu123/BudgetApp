@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.budgetapp.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.presentation.ui.theme.BudgetAppTheme
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter



@Composable
fun AddExpenseBottomSheet(
    modifier: Modifier =  Modifier,
    onDissmiss: () -> Unit = {},
    onAdd: (description: String, value: Double, date: Long, category: String)-> Unit){

    val addExpensetSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val datePickerState = rememberDatePickerState()

    var isDatePickerVisible by remember {mutableStateOf(false)}

    var description by remember {
        mutableStateOf("")
    }

    var value by remember {
        mutableStateOf("")
    }

    var date by remember {
        mutableStateOf(Instant.now().toEpochMilli())
    }

    var category by remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = Unit) {
        addExpensetSheetState.expand()
    }

    if(addExpensetSheetState.isVisible) {
        ModalBottomSheet(
            sheetState = addExpensetSheetState,
            onDismissRequest = { onDissmiss() },
        ) {
            Text(
                modifier = modifier.fillMaxWidth(),
                text = "Adicionar Gasto",
                color = Color.Black,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
            )
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Text(
                    modifier = modifier.fillMaxWidth(),
                    text = "Categoria",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
                dropDownMenu(
                    suggestions = Expense.Companion.getCategories(),
                    onChoice = {category = it}
                )

                Text(
                    modifier = modifier.fillMaxWidth(),
                    text = "Descrição",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
                OutlinedTextField(
                    modifier = modifier.fillMaxWidth(),
                    value = description,
                    onValueChange = { description = it },
                    textStyle = TextStyle(color = Color.Black)
                )

                Text(
                    modifier = modifier.fillMaxWidth(),
                    text = "Data",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
                OutlinedTextField(
                    value = Instant.ofEpochMilli(date).atZone(ZoneId.of("UTC"))
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    enabled = false,
                    textStyle = TextStyle(color = Color.Black),
                    onValueChange = {},
                    modifier = modifier
                        .fillMaxWidth()
                        .clickable {
                            isDatePickerVisible = true
                        },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = "Data",
                            tint = Color.Gray
                        )
                    }
                )

                Text(
                    modifier = modifier.fillMaxWidth(),
                    text = "Valor",
                    color = Color.Gray,
                    fontSize = 13.sp,
                )
                OutlinedTextField(
                    modifier = modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    value = value,
                    onValueChange = { value = it.replace("/[^0-9]+/g", "") },
                    textStyle = TextStyle(color = Color.Black),
                )

                Button(
                    modifier = modifier.fillMaxWidth().padding(vertical = 16.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF009A33)),
                    shape = RoundedCornerShape(15.dp),
                    onClick = {
                        onAdd(
                            description,
                            value.toDouble(),
                            date,
                            category
                        )
                    },
                ) {
                    Text(
                        text = "Adicionar",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }



                if (isDatePickerVisible) {
                    DatePickerDialog(
                        onDismissRequest = { isDatePickerVisible = false },
                        confirmButton = {
                            Button(onClick = {
                                date = datePickerState.selectedDateMillis ?: Instant.now()
                                    .toEpochMilli();
                                isDatePickerVisible = false
                            }) {
                                Text(text = "Confirmar")
                            }
                        },
                        dismissButton = {
                            OutlinedButton(onClick = { isDatePickerVisible = false }) {
                                Text(text = "Cancelar")
                            }
                        },
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddExpenseBottomSheet(){
    BudgetAppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            var open by remember {
                mutableStateOf(false)
            }
            AddExpenseBottomSheet(
                onDissmiss = {open = false},
                onAdd =  { description: String, value: Double, date: Long, category: String ->
                    println(description + category)
                })
        }
    }
}

