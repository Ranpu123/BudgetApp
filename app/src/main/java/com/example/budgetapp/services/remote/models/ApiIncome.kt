package com.example.budgetapp.services.remote.models

import com.example.budgetapp.domain.models.income.Income
import com.example.budgetapp.domain.models.income.IncomeCategory
import java.time.LocalDateTime

class ApiIncome(
    date: LocalDateTime,
    value: Double,
    category: IncomeCategory,
    description: String,
    var userId: Int? = null
):Income(
    date = date,
    value = value,
    category = category,
    description = description
) {

    companion object{
        fun fromIncome(income: Income, userId: Int): ApiIncome{
            var apiIncome = (income as ApiIncome)
            apiIncome.userId = userId

            return apiIncome
        }
    }
}