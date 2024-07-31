package com.example.budgetapp.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.budgetapp.R
import com.example.budgetapp.utils.BudgetAppConstants.DATE_FORMAT
import com.example.budgetapp.utils.BudgetAppConstants.MONTH_YEAR_FORMAT
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun toFormattedDate(date: LocalDate): String{
    if (date.isEqual(LocalDate.now())) {
        return stringResource(R.string.today)
    }else {
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT)).toString()
    }
}

fun toFormattedMonthYear(date: LocalDate): String{
    return date.format(DateTimeFormatter.ofPattern(MONTH_YEAR_FORMAT)).toString()
}