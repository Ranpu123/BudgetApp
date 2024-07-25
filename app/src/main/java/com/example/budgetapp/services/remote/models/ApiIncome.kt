package com.example.budgetapp.services.remote.models

import com.example.budgetapp.domain.models.income.Income
import com.example.budgetapp.domain.models.income.IncomeCategory
import java.time.LocalDateTime
import java.util.UUID

class ApiIncome(
    date: LocalDateTime,
    value: Double,
    category: IncomeCategory,
    description: String,
    id: UUID = UUID.randomUUID(),
    var userId: Int? = null
):Income(
    id = id,
    date = date,
    value = value,
    category = category,
    description = description
) {

    companion object{
        fun fromIncome(income: Income, userId: Int): ApiIncome{
            var apiIncome = ApiIncome(
                id = income.id,
                date = income.date,
                value = income.value,
                category = income.category,
                description = income.description,
                userId = userId
            )
            return apiIncome
        }
    }
}