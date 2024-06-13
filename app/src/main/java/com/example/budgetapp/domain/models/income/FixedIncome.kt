package com.example.budgetapp.domain.models.income

import com.example.budgetapp.domain.models.ICategories
import com.example.budgetapp.domain.models.transaction.FixedTransaction
import java.time.LocalDate
import java.time.LocalDateTime

class FixedIncome(
    date: LocalDateTime,
    value: Double,
    category: INCOME_CATEGORIES,
    description: String,
    active: Boolean = true,
    endDate: LocalDate? = null,
    lastDate: LocalDate = if (date.toLocalDate().isBefore(LocalDate.now())) date.toLocalDate() else date.toLocalDate().minusMonths(1)

) : FixedTransaction<INCOME_CATEGORIES>(
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