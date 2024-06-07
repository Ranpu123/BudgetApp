package com.example.budgetapp.domain.models.income

import com.example.budgetapp.domain.models.ICategories
import com.example.budgetapp.domain.models.transaction.Transaction
import java.time.LocalDateTime

class Income(
    date: LocalDateTime,
    value: Double,
    category: String,
    description: String,

) : Transaction(
    date = date,
    value = value,
    category = category,
    description = description
){
    init {
        require(value > 0.0) {"Income requires positive value!"}
    }

    companion object: ICategories {
        enum class CATEGORIES(val displayName: String) {
            COMISSION("Comissão"),
            GIFT("Presente"),
            INTEREST("Juros"),
            INVESTMENT("Investimentos"),
            SALARY("Salário"),
            SELL("Venda"),
            TIPS("Gorjeta"),
            OTHERS("Outros")
        }

        override fun getCategories(): List<String> {
            return CATEGORIES.values().map { it.displayName }
        }
    }
}