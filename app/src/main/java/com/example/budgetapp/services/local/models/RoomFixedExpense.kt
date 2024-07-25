package com.example.budgetapp.services.local.models

import androidx.room.Entity
import com.example.budgetapp.domain.models.expense.ExpenseCategory
import com.example.budgetapp.domain.models.expense.FixedExpense
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Entity
class RoomFixedExpense(
    date: LocalDateTime,
    value: Double,
    category: ExpenseCategory,
    description: String,
    id: UUID = UUID.randomUUID(),
    active : Boolean = true,
    endDate: LocalDate? = null,
    lastDate: LocalDate = if (date.toLocalDate().isBefore(LocalDate.now())) date.toLocalDate() else date.toLocalDate().minusMonths(1),
    var userId: Int,
    var dirty: Boolean = true,
    var deleted: Boolean = false
): FixedExpense(
    id = id,
    date = date,
    value = value,
    category = category,
    description = description,
    active = active,
    endDate = endDate,
    lastDate = lastDate
) {

    companion object{
        fun fromFixedExpense(fixedExpense: FixedExpense, userId: Int, dirty: Boolean = true, deleted: Boolean = false): RoomFixedExpense {
            var roomFixedExpense = RoomFixedExpense(
                id = fixedExpense.id,
                date = fixedExpense.date,
                value = fixedExpense.value,
                category = fixedExpense.category,
                description = fixedExpense.description,
                active = fixedExpense.active,
                endDate = fixedExpense.endDate,
                lastDate = fixedExpense.lastDate,
                userId = userId,
                dirty = dirty,
                deleted = deleted
            )

            return roomFixedExpense
        }
    }
}