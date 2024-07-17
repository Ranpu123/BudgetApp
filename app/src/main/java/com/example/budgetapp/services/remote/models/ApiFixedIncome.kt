package com.example.budgetapp.services.remote.models

import com.example.budgetapp.domain.models.income.FixedIncome
import com.example.budgetapp.domain.models.income.IncomeCategory
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class ApiFixedIncome(
    date: LocalDateTime,
    value: Double,
    category: IncomeCategory,
    description: String,
    id: UUID = UUID.randomUUID(),
    active : Boolean = true,
    endDate: LocalDate? = null,
    lastDate: LocalDate = if (date.toLocalDate().isBefore(LocalDate.now())) date.toLocalDate() else date.toLocalDate().minusMonths(1),
    var userId: Int? = null
):FixedIncome(
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
        fun fromFixedIncome(fixedIncome: FixedIncome, userId: Int): ApiFixedIncome {
            var apiFixedIncome = ApiFixedIncome(
                id = fixedIncome.id,
                date = fixedIncome.date,
                value = fixedIncome.value,
                category = fixedIncome.category,
                description = fixedIncome.description,
                active = fixedIncome.active,
                endDate = fixedIncome.endDate,
                lastDate = fixedIncome.lastDate,
                userId = userId,
            )
            return apiFixedIncome
        }
    }
}