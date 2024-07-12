package com.example.budgetapp.services.remote.models

import com.example.budgetapp.domain.models.income.FixedIncome
import com.example.budgetapp.domain.models.income.IncomeCategory
import java.time.LocalDate
import java.time.LocalDateTime

class ApiFixedIncome(
    date: LocalDateTime,
    value: Double,
    category: IncomeCategory,
    description: String,
    active : Boolean = true,
    endDate: LocalDate? = null,
    lastDate: LocalDate = if (date.toLocalDate().isBefore(LocalDate.now())) date.toLocalDate() else date.toLocalDate().minusMonths(1),
    var userId: Int? = null
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
        fun fromFixedIncome(fixedIncome: FixedIncome, userId: Int): ApiFixedIncome {
            var apiFixedIncome = (fixedIncome as ApiFixedIncome)
            apiFixedIncome.userId = userId

            return apiFixedIncome
        }
    }
}