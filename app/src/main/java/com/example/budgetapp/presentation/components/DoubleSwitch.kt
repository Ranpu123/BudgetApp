package com.example.budgetapp.presentation.components

import android.provider.CalendarContract.Colors
import androidx.compose.foundation.DefaultMarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.budgetapp.presentation.ui.theme.BudgetAppTheme

@Composable
fun DoubleSwitch(
    modifier: Modifier = Modifier,
    startOnRight: Boolean = false,
    onLeftClick: () -> Unit = {},
    onRightClick: () -> Unit = {},
){

    var switchLeft by remember {
        mutableStateOf(!startOnRight)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = Color(0xFF009A33),
                shape = RoundedCornerShape(30.dp),
            )
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Button(
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(start = 5.dp),
                colors = ButtonDefaults
                    .buttonColors(Color(0X00FFFFFF)),
                onClick = {
                    onRightClick()
                    switchLeft = !switchLeft
                },
            )
            {
                Text(
                    "Variáveis",
                    color = Color(0xFF014D19)
                )
            }

            Button(
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(end = 5.dp),
                colors = ButtonDefaults
                    .buttonColors(Color(0X00FFFFFF)),
                onClick = {
                    onLeftClick()
                    switchLeft = !switchLeft
                },
            ){
                Text(
                    "Fixos",
                    color = Color(0xFF014D19)
                )
            }
        }

        Row (
            modifier = Modifier
                .fillMaxWidth(),
        ){
            if (switchLeft) {
                Button(
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 5.dp),
                    colors = ButtonDefaults
                        .buttonColors(Color(0XFFFFFFFF)),
                    onClick = {},
                ){
                    Text(
                        "Variáveis",
                        color = Color(0xFF009A33)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
            if (!switchLeft) {
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 5.dp),
                    colors = ButtonDefaults
                        .buttonColors(Color(0XFFFFFFFF)),
                    onClick = {},
                ){
                    Text(
                        "Fixos",
                        color = Color(0xFF009A33)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDoubleSwitch(){
    BudgetAppTheme {
        DoubleSwitch(onLeftClick = {print("LEFT")},
            onRightClick = {print("RIGHT")})
    }
}