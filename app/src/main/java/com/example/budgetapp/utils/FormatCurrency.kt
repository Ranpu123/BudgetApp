package com.example.budgetapp.utils

import android.util.Log
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

fun formatCurrency(it: String): String {

    val currency: Currency = Currency.getInstance(Locale.getDefault())
    val symbol: String = currency.getSymbol()

    try {
        val cleanS: String = it.replace("[$symbol,.\u00A0]".toRegex(), "")

        val parsed: BigDecimal = BigDecimal(cleanS)
            .setScale(2, RoundingMode.FLOOR)
            .divide(BigDecimal(100), RoundingMode.FLOOR)

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
            .setScale(2, RoundingMode.DOWN)


        val result: String = NumberFormat.getCurrencyInstance(Locale.getDefault())
            .format(parsed)

        return result
    } catch (e: Exception) {
        Log.e("ERROR", e.toString())
        return "0"
    }
}