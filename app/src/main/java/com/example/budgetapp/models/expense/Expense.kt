package com.example.budgetapp.models.expense

import com.example.budgetapp.models.ICategories
import com.example.budgetapp.models.transaction.Transaction
import java.time.LocalDateTime

open class Expense(
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
    companion object: ICategories{
        enum class CATEGORIES(val displayName: String) {
            FOOD("Alimentação"),
            GROCERY("Mercado"),
            HEALTH("Saúde"),
            LIGHTING("Luz"),
            RENT("Alguel"),
            RESTAURANT("Restaurante"),
            TAX("Imposto"),
            WATER("Água"),
            GAS("Gás"),
            FUEL("Combustível"),
            OTHER("Outro")
        }

        override fun getCategories(): List<String> {
            return CATEGORIES.values().map { it.displayName }
        }
    }
}