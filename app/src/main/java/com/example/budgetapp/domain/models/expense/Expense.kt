package com.example.budgetapp.domain.models.expense

import com.example.budgetapp.domain.models.ICategories
import com.example.budgetapp.domain.models.transaction.Transaction
import java.time.LocalDateTime

open class Expense(
    date: LocalDateTime,
    value: Double,
    category: EXPENSE_CATEGORIES,
    description: String,

) : Transaction<EXPENSE_CATEGORIES>(
    date = date,
    value = value,
    category = category,
    description = description
){
    init {
        require(value < 0.0){"Expense requires negative value!"}
    }
}