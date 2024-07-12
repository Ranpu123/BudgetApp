package com.example.budgetapp.services.remote.models

import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.domain.models.expense.ExpenseCategory
import java.time.LocalDateTime

class ApiExpense(
    date: LocalDateTime,
    value: Double,
    category: ExpenseCategory,
    description: String,
    var userId: Int? = null
): Expense(
    date = date,
    value = value,
    category = category,
    description = description
) {

    companion object{
        fun fromExpense(expense: Expense, userId: Int): ApiExpense{
            var apiExpense = (expense as ApiExpense)
            apiExpense.userId = userId

            return  apiExpense
        }
    }
}