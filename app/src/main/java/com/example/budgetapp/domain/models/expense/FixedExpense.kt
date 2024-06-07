package com.example.budgetapp.domain.models.expense

import com.example.budgetapp.domain.models.ICategories
import com.example.budgetapp.domain.models.transaction.FixedTransaction
import java.time.LocalDate
import java.time.LocalDateTime

class FixedExpense(
    date: LocalDateTime,
    value: Double,
    category: String,
    description: String,
    active : Boolean = true,
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
        require(value < 0.0){"Fixed Income requires negative value!"}
    }

    companion object: ICategories {
        enum class CATEGORIES(val displayName: String) {
            STREAMING("Streaming"),
            INTERNET("Internet"),
            PHONE_PLAN("Plano de celular"),
            INSURANCE("Seguro"),
            INSTALLMENT("Prestação"),
            OTHERS("Outros")
        }

        override fun getCategories(): List<String> {
            return CATEGORIES.values().map { it.displayName }
        }
    }
}