package com.example.budgetapp.services.local.models

import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.domain.models.expense.ExpenseCategory
import java.time.LocalDateTime
import kotlin.math.exp

class RoomExpense(
    date: LocalDateTime,
    value: Double,
    category: ExpenseCategory,
    description: String,
    var dirty: Boolean = true
): Expense(
    date = date,
    value = value,
    category = category,
    description = description
) {

    companion object{
        fun fromExpense(expense: Expense, dirty: Boolean = true): RoomExpense{
            var roomExpense = (expense as RoomExpense)
            roomExpense.dirty = dirty

            return  roomExpense
        }
    }
}