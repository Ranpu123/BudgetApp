package com.example.budgetapp.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.toSize
import com.example.budgetapp.R
import com.example.budgetapp.domain.models.ICategories
import com.example.budgetapp.presentation.ui.theme.BudgetAppTheme

@Composable
fun dropDownMenu(
    suggestions: List<ICategories> = emptyList<ICategories>(),
    onChoice: (it:ICategories) -> Unit = {},
    defaultSelected: String = stringResource(R.string.default_option)
) {

    var expanded by remember { mutableStateOf(false) }

    var selectedText by remember { mutableStateOf(defaultSelected) }

    var textfieldSize by remember { mutableStateOf(Size.Zero) }

    var itemSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown


    Column(Modifier.fillMaxWidth()) {
        OutlinedTextField(
            enabled = false,
            value = selectedText,
            textStyle = TextStyle(color = Color.Black),
            onValueChange = { selectedText = it },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .onGloballyPositioned { coordinates ->
                    textfieldSize = coordinates.size.toSize()
                },
            trailingIcon = {
                Icon(icon, null)
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .height(with(LocalDensity.current) { itemSize.height.toDp() * 7 })
                .width(with(LocalDensity.current) { textfieldSize.width.toDp() }),
        ){
            suggestions.forEach { category ->
                DropdownMenuItem(
                    onClick = {
                        selectedText = category.asString()
                        expanded = false
                        onChoice(category)
                    },
                    text = {
                        Text(
                            text = category.asString(),
                            color = Color.Black,
                            modifier = Modifier
                                .onGloballyPositioned { coordinates ->
                                    itemSize = coordinates.size.toSize()
                            },
                        )
                    }
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun dropDownMenuPreview(){
    BudgetAppTheme {
        dropDownMenu()
    }
}
