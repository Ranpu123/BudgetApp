package com.example.budgetapp.utils

import android.util.Log
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Currency
import java.util.Locale

fun currencyToDouble(it: String): Double {
    val currency: Currency = Currency.getInstance(Locale.getDefault())
    val symbol: String = currency.getSymbol()

    try {
        val cleanS = it.replace("[  $symbol,.\u00A0]".toRegex(), "")
        val parsed = BigDecimal(cleanS)
            .setScale(2, RoundingMode.FLOOR)
            .divide(BigDecimal(100), RoundingMode.FLOOR)
        return parsed.toDouble()
    } catch (e: Exception) {
        Log.e("ERROR", e.toString())
        return 0.0
    }
}