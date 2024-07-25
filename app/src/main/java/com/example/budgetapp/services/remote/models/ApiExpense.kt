package com.example.budgetapp.services.remote.models

import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.domain.models.expense.ExpenseCategory
import java.time.LocalDateTime
import java.util.UUID

class ApiExpense(
    date: LocalDateTime,
    value: Double,
    category: ExpenseCategory,
    description: String,
    id: UUID = UUID.randomUUID(),
    var userId: Int? = null

): Expense(
    id = id,
    date = date,
    value = value,
    category = category,
    description = description
) {

    companion object{
        fun fromExpense(expense: Expense, userId: Int): ApiExpense{
            var apiExpense = ApiExpense(
                id = expense.id,
                date = expense.date,
                value = expense.value,
                category = expense.category,
                description = expense.description,
                userId = userId,
            )


            return  apiExpense
        }
    }
}