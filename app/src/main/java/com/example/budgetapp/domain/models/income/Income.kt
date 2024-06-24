package com.example.budgetapp.domain.models.income

import com.example.budgetapp.domain.models.transaction.Transaction
import java.time.LocalDateTime

class Income(
    date: LocalDateTime,
    value: Double,
    category: IncomeCategory,
    description: String,

    ) : Transaction<IncomeCategory>(
    date = date,
    value = value,
    category = category,
    description = description
){
    init {
        require(value > 0.0) {"Income requires positive value!"}
    }
}