package com.example.budgetapp.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.budgetapp.R
import com.example.budgetapp.utils.formatCurrency
import com.example.budgetapp.utils.toFormattedDate
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun TransactionDateHeader(
    total: Double,
    date: LocalDate? = null,
    title: String = "",
    modifier: Modifier = Modifier
){
    Surface(modifier = modifier.fillMaxWidth()) {
        ConstraintLayout(
            modifier = modifier
                .fillMaxWidth()
                .padding(2.dp),
        ) {
            val (data, text, valor) = createRefs()
            Text(
                modifier = modifier
                    .constrainAs(text) {
                        end.linkTo(valor.start);
                        centerVerticallyTo(parent)
                    },
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                text = stringResource(R.string.header_total)
            )
            Text(
                modifier = modifier
                    .constrainAs(valor) {
                        end.linkTo(parent.end)
                        centerVerticallyTo(parent)
                    },
                color = if(total < 0.0) Color(0xFFFF0000) else Color(0xFF00BD40),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                text = formatCurrency(total)
            )

            Text(
                modifier = modifier
                    .constrainAs(data) { centerTo(parent) },
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                text = if(date != null) toFormattedDate(date) else title
            )
        }
    }
    Divider(color = Color.Black)
}

