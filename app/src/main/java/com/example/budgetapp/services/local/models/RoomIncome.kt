package com.example.budgetapp.services.local.models

import com.example.budgetapp.domain.models.income.Income
import com.example.budgetapp.domain.models.income.IncomeCategory
import java.time.LocalDateTime

class RoomIncome(
    date: LocalDateTime,
    value: Double,
    category: IncomeCategory,
    description: String,
    var dirty: Boolean = true
):Income(
    date = date,
    value = value,
    category = category,
    description = description
) {

    companion object{
        fun fromIncome(income: Income, dirty: Boolean = true): RoomIncome{
            var roomIncome = (income as RoomIncome)
            roomIncome.dirty = dirty

            return roomIncome
        }
    }
}