package com.example.budgetapp.services.local.models

import com.example.budgetapp.domain.models.expense.ExpenseCategory
import com.example.budgetapp.domain.models.expense.FixedExpense
import java.time.LocalDate
import java.time.LocalDateTime

class RoomFixedExpense(
    date: LocalDateTime,
    value: Double,
    category: ExpenseCategory,
    description: String,
    active : Boolean = true,
    endDate: LocalDate? = null,
    lastDate: LocalDate = if (date.toLocalDate().isBefore(LocalDate.now())) date.toLocalDate() else date.toLocalDate().minusMonths(1),
    var dirty: Boolean = true
): FixedExpense(
    date = date,
    value = value,
    category = category,
    description = description,
    active = active,
    endDate = endDate,
    lastDate = lastDate
) {

    companion object{
        fun fromFixedExpense(fixedExpense: FixedExpense, dirty: Boolean = true): RoomFixedExpense {
            var roomFixedExpense = (fixedExpense as RoomFixedExpense)
            roomFixedExpense.dirty = dirty

            return roomFixedExpense
        }
    }
}