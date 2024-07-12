package com.example.budgetapp.services.local.models

import com.example.budgetapp.domain.models.income.FixedIncome
import com.example.budgetapp.domain.models.income.IncomeCategory
import java.time.LocalDate
import java.time.LocalDateTime

class RoomFixedIncome(
    date: LocalDateTime,
    value: Double,
    category: IncomeCategory,
    description: String,
    active : Boolean = true,
    endDate: LocalDate? = null,
    lastDate: LocalDate = if (date.toLocalDate().isBefore(LocalDate.now())) date.toLocalDate() else date.toLocalDate().minusMonths(1),
    var dirty: Boolean = true
):FixedIncome(
    date = date,
    value = value,
    category = category,
    description = description,
    active = active,
    endDate = endDate,
    lastDate = lastDate
) {

    companion object{
        fun fromFixedIncome(fixedIncome: FixedIncome, dirty: Boolean = true): RoomFixedIncome {
            var roomFixedIncome = (fixedIncome as RoomFixedIncome)
            roomFixedIncome.dirty = dirty

            return roomFixedIncome
        }
    }
}