package com.example.budgetapp.domain.models.income

import androidx.room.Entity
import com.example.budgetapp.domain.models.transaction.Transaction
import java.time.LocalDateTime
import java.util.UUID


open class Income(
    date: LocalDateTime,
    value: Double,
    category: IncomeCategory,
    description: String,
    id: UUID = UUID.randomUUID(),

    ) : Transaction<IncomeCategory>(
    id = id,
    date = date,
    value = value,
    category = category,
    description = description
){
    init {
        require(value > 0.0) {"Income requires positive value!"}
    }

}