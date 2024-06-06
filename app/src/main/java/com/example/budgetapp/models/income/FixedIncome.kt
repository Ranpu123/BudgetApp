package com.example.budgetapp.models.income

import com.example.budgetapp.models.ICategories
import com.example.budgetapp.models.transaction.FixedTransaction
import java.time.LocalDate
import java.time.LocalDateTime

class FixedIncome(
    date: LocalDateTime,
    value: Double,
    category: String,
    description: String,
    active: Boolean = true,
    endDate: LocalDate? = null,
    lastDate: LocalDate = if (date.toLocalDate().isBefore(LocalDate.now())) date.toLocalDate() else LocalDate.now()

) : FixedTransaction(
    date = date,
    value = value,
    category = category,
    description = description,
    lastDate = lastDate,
    active = active,
    endDate = endDate
){
    init {
        require(value > 0.0){"Fixed Income requires positive value!"}
    }

    companion object: ICategories{
        enum class CATEGORIES(val displayName: String) {
            SALARY("Salário"),
            RENT("Alguel"),
            OTHERS("Outros")
        }
        override fun getCategories(): List<String> {
            return CATEGORIES.values().map { it.displayName }
        }
    }
}