package com.example.budgetapp.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.budgetapp.presentation.ui.theme.BudgetAppTheme

@Composable
fun GenericDropDownMenu(
    modifier: Modifier = Modifier,
    options: List<String> = emptyList(),
    onChoice: (it: String) -> Unit = {},
    defaultSelected: String = "Select an Option"
) {

    var expanded by remember { mutableStateOf(false) }

    var selectedText by remember { mutableStateOf(defaultSelected) }

    var textfieldSize by remember { mutableStateOf(Size.Zero) }

    var itemSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown


    Column(modifier.wrapContentSize()) {
        /*OutlinedTextField(
            enabled = false,
            value = selectedText,
            shape = RoundedCornerShape(60.dp),
            textStyle = TextStyle(color = Color.Black, textAlign = TextAlign.End, fontSize = 8.sp),
            onValueChange = { selectedText = it },
            modifier = Modifier
                .height(40.dp)
                .defaultMinSize(1.dp, 1.dp)
                .clickable { expanded = !expanded }
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()
                },
            trailingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        )*/

        Surface(
            modifier = modifier
                .border(
                    shape = RoundedCornerShape(30.dp),
                    border = BorderStroke(
                        color = Color(0xFFCCCCCC),
                        width = 1.dp
                    )
                )
        ) {
            Row(
                modifier = modifier
                    .padding(horizontal = 5.dp, vertical = 2.dp)
                    .clickable { expanded = !expanded }
                    .onGloballyPositioned { coordinates ->
                        //This value is used to assign to the DropDown the same width
                        textfieldSize = coordinates.size.toSize()
                    },
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedText,
                    color = Color.Black,
                    fontSize = 10.sp
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color(0xFFABABAB)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .height(with(LocalDensity.current) { itemSize.height.toDp() * 7 })
                .width(with(LocalDensity.current) { textfieldSize.width.toDp() }),
        ){
            options.forEach { category ->
                DropdownMenuItem(
                    onClick = {
                        selectedText = category
                        expanded = false
                        onChoice(category)
                    },
                    text = {
                        Text(
                            text = category,
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
fun GenericDropDownMenuPreview(){
    BudgetAppTheme {
        GenericDropDownMenu()
    }
}