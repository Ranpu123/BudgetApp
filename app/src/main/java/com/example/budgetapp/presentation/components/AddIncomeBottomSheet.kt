@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.budgetapp.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.budgetapp.R
import com.example.budgetapp.domain.models.income.IncomeCategory
import com.example.budgetapp.presentation.viewModels.transactionBottomSheet.IncomeBottomSheetViewModel
import com.example.budgetapp.utils.currencyToDouble
import com.example.budgetapp.utils.formatCurrency
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIncomeBottomSheet(
    bottomSheetViewModel: IncomeBottomSheetViewModel,
    modifier: Modifier =  Modifier,
    onDismiss: () -> Unit = {},
    onAdd: ()-> Unit = {}
){

    val addExpensetSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val datePickerState = rememberDatePickerState()

    var isDatePickerVisible by remember {
        mutableStateOf(false)
    }

    val uiState by bottomSheetViewModel.uiState.collectAsState()
    var category by bottomSheetViewModel.category
    var description by bottomSheetViewModel.description
    var value by bottomSheetViewModel.value
    var date by bottomSheetViewModel.date

    LaunchedEffect(key1 = Unit) {
        addExpensetSheetState.expand()
    }

    if(addExpensetSheetState.isVisible) {
        ModalBottomSheet(
            sheetState = addExpensetSheetState,
            onDismissRequest = {
                onDismiss()
                bottomSheetViewModel.clearState()
            },
        ) {
            Text(
                modifier = modifier.fillMaxWidth(),
                text = stringResource(id = R.string.cd_add_transaction, stringResource(id = R.string.income)),
                color = Color.Black,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
            )
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Text(
                    modifier = modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.field_category),
                    color = Color.Gray,
                    fontSize = 13.sp
                )
                dropDownMenu(
                    suggestions = IncomeCategory.entries.toList().sortedBy { it.asString() },
                    onChoice = {
                        category = it as IncomeCategory
                        description = category.asString()
                    },
                    defaultSelected = category.asString()
                )
                Text(
                    modifier = modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.field_description),
                    color = Color.Gray,
                    fontSize = 13.sp
                )
                OutlinedTextField(
                    modifier = modifier.fillMaxWidth(),
                    value = description,
                    isError = !uiState.descriptionError.isNullOrEmpty(),
                    onValueChange = { description = it },
                    textStyle = TextStyle(color = Color.Black),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = stringResource(id = R.string.cd_clear_field),
                            modifier = Modifier.clickable { description = "" }
                        )
                    }
                )
                if (!uiState.descriptionError.isNullOrBlank()){
                    Text(
                        modifier = modifier.fillMaxWidth(),
                        text = uiState.descriptionError.orEmpty(),
                        color = Color.Red,
                        fontSize = 13.sp,
                    )
                }

                Text(
                    modifier = modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.field_date),
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
                            contentDescription = stringResource(id = R.string.field_date),
                            tint = Color.Gray
                        )
                    }
                )

                Text(
                    modifier = modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.field_value),
                    color = Color.Gray,
                    fontSize = 13.sp,
                )
                OutlinedTextField(
                    modifier = modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    value = value,
                    isError = !uiState.valueError.isNullOrEmpty(),
                    onValueChange = { value = formatCurrency(it) },
                    textStyle = TextStyle(color = Color.Black),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = stringResource(id = R.string.cd_clear_field),
                            modifier = Modifier.clickable { value = formatCurrency("0") }
                        )
                    }
                )
                if (!uiState.valueError.isNullOrBlank()){
                    Text(
                        modifier = modifier.fillMaxWidth(),
                        text = uiState.valueError.orEmpty(),
                        color = Color.Red,
                        fontSize = 13.sp,
                    )
                }

                Button(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF009A33)),
                    shape = RoundedCornerShape(15.dp),
                    onClick = {
                        if(bottomSheetViewModel.validadeForm(description, currencyToDouble(value))){
                            bottomSheetViewModel.addNewTransaction(
                                description = description,
                                value = currencyToDouble(value),
                                category = category,
                                date = Instant.ofEpochMilli(date)
                                    .atZone(ZoneId.of("UTC"))
                                    .toLocalDate(),
                            )
                            onAdd()
                            bottomSheetViewModel.clearState()
                        }
                    },
                ) {
                    Text(
                        text = stringResource(id = R.string.btn_add),
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
                                    .toEpochMilli()
                                isDatePickerVisible = false
                            }) {
                                Text(text = stringResource(id = R.string.btn_confirm))
                            }
                        },
                        dismissButton = {
                            OutlinedButton(onClick = { isDatePickerVisible = false }) {
                                Text(text = stringResource(id = R.string.btn_cancel))
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
