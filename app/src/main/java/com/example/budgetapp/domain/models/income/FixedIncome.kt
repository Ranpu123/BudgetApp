package com.example.budgetapp.domain.models.income

import com.example.budgetapp.domain.models.ICategories
import com.example.budgetapp.domain.models.transaction.FixedTransaction
import java.time.LocalDate
import java.time.LocalDateTime

class FixedIncome(
    date: LocalDateTime,
    value: Double,
    category: FIXED_INCOME_CATEGORIES,
    description: String,
    active: Boolean = true,
    endDate: LocalDate? = null,
    lastDate: LocalDate = if (date.toLocalDate().isBefore(LocalDate.now())) date.toLocalDate() else LocalDate.now()

) : FixedTransaction<FIXED_INCOME_CATEGORIES>(
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
}