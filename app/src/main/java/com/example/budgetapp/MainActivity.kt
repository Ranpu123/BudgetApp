package com.example.budgetapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.budgetapp.components.CardSaldo
import com.example.budgetapp.ui.theme.BudgetAppTheme
import com.example.budgetapp.ui.theme.Green80

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BudgetAppTheme {
                App(modifier = Modifier)
            }
        }
    }
}

@Composable
fun App(modifier: Modifier = Modifier){
    ConstraintLayout {
        val (card, back, text) = createRefs()
        val startGuideline = createGuidelineFromTop(0.25f)
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
                    top.linkTo(back.bottom, margin = -95.dp)
                }
        ) {
            CardSaldo(modifier)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview(){
    BudgetAppTheme {
        App()
    }
}

