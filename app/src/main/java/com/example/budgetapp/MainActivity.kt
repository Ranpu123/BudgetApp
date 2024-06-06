package com.example.budgetapp

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.constraintlayout.compose.ConstraintLayout

import com.example.budgetapp.components.cardSaldo
import com.example.budgetapp.ui.theme.BudgetAppTheme
import com.example.budgetapp.ui.theme.Green80

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BudgetAppTheme {
                app(modifier = Modifier)
            }
        }
    }
}

@Composable
fun app(modifier: Modifier = Modifier){
    ConstraintLayout {
        val (card, back, text) = createRefs()
        Surface(
            color = Green80,
            modifier = modifier
                .height(180.dp)
                .fillMaxWidth()
                .constrainAs(back) {
                    top.linkTo(parent.top)
                }

        ){}
        Surface (
            color = Color.Transparent,
            modifier = modifier
                .padding(horizontal = 32.dp)
                .constrainAs(text) {
                    bottom.linkTo(card.top)
                }
        ){
            Text(
                text = "Olá, Vinícius!",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
        Surface (
            color = Color.Transparent,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .constrainAs(card) {
                    top.linkTo(back.bottom, margin = -(95.dp))
                }
        ) {
            cardSaldo(modifier)
        }
    }
}

@Composable
fun cardGastosVar(modifier: Modifier = Modifier){

    var expanded by rememberSaveable { mutableStateOf(true) }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, color = Color(0xFFABABAB))
    ){
        Column {
            Row(
                modifier = modifier.padding(8.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    modifier = modifier.size(24.dp),
                    onClick = { /*TODO*/ },
                    shape = CircleShape,
                    contentPadding = PaddingValues(2.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Adicionar Gasto",
                        tint = Color.Black
                    )
                }
                Text(
                    text = "Gastos Variaveis",
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Expandir Cartão",
                    tint = Color(0xFFABABAB)
                )
            }
            if(expanded){
                Divider()
                Text(text= "Ver mais", color = Color(0xFF0077CD), textAlign = TextAlign.Center)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun cardGastosVarPreview(){
    BudgetAppTheme {
        cardGastosVar()
    }
}

@Preview(showBackground = true)
@Composable
fun appPreview(){
    BudgetAppTheme {
        app()
    }
}



