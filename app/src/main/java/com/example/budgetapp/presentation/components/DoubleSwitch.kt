package com.example.budgetapp.presentation.components

import android.provider.CalendarContract.Colors
import androidx.compose.foundation.DefaultMarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
    startOnLeft: Boolean = true,
    onLeftClick: () -> Unit = {},
    onRightClick: () -> Unit = {},
){

    var switchLeft by remember {
        mutableStateOf(startOnLeft)
    }
    val localDensity = LocalDensity.current
    var buttonSize by remember {
        mutableStateOf(0.dp)
    }

    Box (
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = Color(0xFF009A33),
                shape = RoundedCornerShape(30.dp),
            )
            .onGloballyPositioned { coordinates ->
                buttonSize = with(localDensity) { coordinates.size.width.toDp() / 2 }
            },
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
        ){
            Button(
                modifier = modifier
                    .width(buttonSize)
                    .padding(start = 5.dp),
                colors = ButtonDefaults
                    .buttonColors(Color(0X00FFFFFF)),
                onClick = {
                    onLeftClick()
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
                modifier = modifier
                    .width(buttonSize)
                    .padding(end = 5.dp),
                colors = ButtonDefaults
                    .buttonColors(Color(0X00FFFFFF)),
                onClick = {
                    onRightClick()
                    switchLeft = !switchLeft
                },
            )
            {
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

            if(switchLeft) {
                Button(
                    modifier = modifier
                        .width(buttonSize)
                        .padding(start = 5.dp),
                    colors = ButtonDefaults
                        .buttonColors(Color(0XFFFFFFFF)),
                    onClick = {},
                )
                {
                    Text(
                        "Variáveis",
                        color = Color(0xFF009A33)
                    )
                }
            }
            if(!switchLeft) {
                Spacer(modifier = modifier.weight(1f))
                Button(
                    modifier = modifier
                        .width(buttonSize)
                        .padding(end = 5.dp),
                    colors = ButtonDefaults
                        .buttonColors(Color(0XFFFFFFFF)),
                    onClick = {},
                )
                {
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