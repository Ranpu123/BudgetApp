package com.example.budgetapp.services.remote.models

import com.example.budgetapp.domain.models.expense.ExpenseCategory
import com.example.budgetapp.domain.models.expense.FixedExpense
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class ApiFixedExpense(
    date: LocalDateTime,
    value: Double,
    category: ExpenseCategory,
    description: String,
    active : Boolean = true,
    endDate: LocalDate? = null,
    lastDate: LocalDate = if (date.toLocalDate().isBefore(LocalDate.now())) date.toLocalDate() else date.toLocalDate().minusMonths(1),
    id: UUID = UUID.randomUUID(),
    var userId: Int?
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
        fun fromFixedExpense(fixedExpense: FixedExpense, userId: Int): ApiFixedExpense {
            var apiFixedExpense = ApiFixedExpense(
                id = fixedExpense.id,
                date = fixedExpense.date,
                value = fixedExpense.value,
                category = fixedExpense.category,
                description = fixedExpense.description,
                active = fixedExpense.active,
                endDate = fixedExpense.endDate,
                lastDate = fixedExpense.lastDate,
                userId = userId,
            )

            return apiFixedExpense
        }
    }
}