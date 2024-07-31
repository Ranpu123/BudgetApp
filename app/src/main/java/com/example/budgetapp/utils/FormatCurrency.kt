package com.example.budgetapp.utils

import android.util.Log
import com.example.budgetapp.utils.BudgetAppConstants.REGEX_CURRENCY
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

fun formatCurrency(it: String): String {

    val currency: Currency = Currency.getInstance(Locale.getDefault())
    val symbol: String = currency.getSymbol()

    try {
        val cleanS: String = it.replace("[$symbol$REGEX_CURRENCY]".toRegex(), "")

        val parsed: BigDecimal = BigDecimal(cleanS)
            .setScale(2, RoundingMode.DOWN)
            .divide(BigDecimal(100), RoundingMode.DOWN)

        val result: String = NumberFormat.getCurrencyInstance(Locale.getDefault())
            .format(parsed)

        return result
    } catch (e: Exception) {
        Log.e("ERROR", e.toString())
        return "0"
    }
}
fun formatCurrency(it: Double): String {
    try {

        val parsed: BigDecimal = BigDecimal(it)
            .setScale(2, RoundingMode.HALF_UP)


        val result: String = NumberFormat.getCurrencyInstance(Locale.getDefault())
            .format(parsed)

        return result
    } catch (e: Exception) {
        Log.e("ERROR", e.toString())
        return "0"
    }
}