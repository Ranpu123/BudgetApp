package com.example.budgetapp.services.local.models

import androidx.room.Entity
import com.example.budgetapp.domain.models.income.Income
import com.example.budgetapp.domain.models.income.IncomeCategory
import java.time.LocalDateTime
import java.util.UUID

@Entity
class RoomIncome(
    date: LocalDateTime,
    value: Double,
    category: IncomeCategory,
    description: String,
    id: UUID = UUID.randomUUID(),
    var userId: Int,
    var dirty: Boolean = true,
    var deleted: Boolean = false
):Income(
    id = id,
    date = date,
    value = value,
    category = category,
    description = description
) {

    companion object{
        fun fromIncome(income: Income, userId: Int, dirty: Boolean = true, deleted: Boolean = false): RoomIncome{
            var roomIncome = RoomIncome(
                id = income.id,
                date = income.date,
                value = income.value,
                category = income.category,
                description = income.description,
                userId = userId,
                dirty = dirty,
                deleted = deleted
            )

            return roomIncome
        }
    }
}