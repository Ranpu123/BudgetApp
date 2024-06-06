package com.example.budgetapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.budgetapp.ui.theme.BudgetAppTheme
import com.example.budgetapp.ui.theme.Green20
import com.example.budgetapp.ui.theme.Green40

@Composable
fun cardSaldo(modifier: Modifier = Modifier) {
    Surface(
        color = Green40,
        modifier = modifier,
        shape = RoundedCornerShape(29.dp)
    ) {
        Column {
            Column(
                modifier = modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Saldo", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(text = "R$ 1.500,00", fontWeight = FontWeight.Bold, fontSize = 24.sp)
            }
            Surface(
                modifier = modifier.fillMaxWidth(),
                color = Green20,
                shape = RoundedCornerShape(29.dp)
            ) {
                Row(
                    modifier = modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowDown,
                                contentDescription = "Entradas",
                                tint = Color(0xFF52FF00)
                            )
                            Text("Entradas", fontWeight = FontWeight.Medium, fontSize = 15.sp)
                        }
                        Text("R$ 1.419,00", textAlign = TextAlign.Start, fontSize = 16.sp)
                    }
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowUp,
                                contentDescription = "Saídas",
                                tint = Color(0xFFFF0000)
                            )
                            Text("Saídas", fontWeight = FontWeight.Medium, fontSize = 15.sp)
                        }
                        Text("R$ 250,00", textAlign = TextAlign.End, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun cardSaldoPreview() {
    BudgetAppTheme {
        cardSaldo()
    }
}