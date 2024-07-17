package com.example.budgetapp.services.local.models

import androidx.room.Entity
import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.domain.models.expense.ExpenseCategory
import java.time.LocalDateTime
import java.util.UUID
import kotlin.math.exp

@Entity
class RoomExpense(
    date: LocalDateTime,
    value: Double,
    category: ExpenseCategory,
    description: String,
    id: UUID = UUID.randomUUID(),
    var userId: Int,
    var dirty: Boolean = true,
    var deleted: Boolean = false,
): Expense(
    id = id,
    date = date,
    value = value,
    category = category,
    description = description
) {

    companion object{
        fun fromExpense(expense: Expense, userId: Int, dirty: Boolean = true, deleted: Boolean = false): RoomExpense{
            var roomExpense = RoomExpense(
                id = expense.id,
                date = expense.date,
                value = expense.value,
                category = expense.category,
                description = expense.description,
                userId = userId,
                dirty = dirty,
                deleted = deleted
            )

            return  roomExpense
        }
    }
}