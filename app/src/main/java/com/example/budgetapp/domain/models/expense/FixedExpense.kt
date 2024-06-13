package com.example.budgetapp.domain.models.expense

import com.example.budgetapp.domain.models.ICategories
import com.example.budgetapp.domain.models.income.FIXED_INCOME_CATEGORIES
import com.example.budgetapp.domain.models.transaction.FixedTransaction
import java.time.LocalDate
import java.time.LocalDateTime

class FixedExpense(
    date: LocalDateTime,
    value: Double,
    category: EXPENSE_CATEGORIES,
    description: String,
    active : Boolean = true,
    endDate: LocalDate? = null,
    lastDate: LocalDate = if (date.toLocalDate().isBefore(LocalDate.now())) date.toLocalDate() else date.toLocalDate().minusMonths(1)

) : FixedTransaction<EXPENSE_CATEGORIES>(
    date = date,
    value = value,
    category = category,
    description = description,
    lastDate = lastDate,
    active = active,
    endDate = endDate
){
    init {
        require(value < 0.0){"Fixed Income requires negative value!"}
    }

}