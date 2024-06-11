package com.example.budgetapp.services.repository.expense

import com.example.budgetapp.domain.models.expense.EXPENSE_CATEGORIES
import com.example.budgetapp.domain.repository_interfaces.IExpenseRepository
import com.example.budgetapp.domain.models.expense.Expense
import java.time.LocalDateTime

object LocalExpenseRepository: IExpenseRepository {
    private val expenses: MutableList<Expense> = mutableListOf(
        Expense(
            date = LocalDateTime.parse("2024-06-06T10:30:00"),
            value = -50.00,
            category = EXPENSE_CATEGORIES.FUEL,
            description = "Gasolina Fox"
        ),
        Expense(
            date = LocalDateTime.parse("2024-06-01T11:30:00"),
            value = -25.00,
            category = EXPENSE_CATEGORIES.RESTAURANT,
            description = "Almoço"
        ),
        Expense(
            date = LocalDateTime.parse("2024-06-02T08:30:00"),
            value = -5.00,
            category = EXPENSE_CATEGORIES.RESTAURANT,
            description = "Café"
        ),
        Expense(
            date = LocalDateTime.parse("2024-06-02T08:30:00"),
            value = -105.00,
            category = EXPENSE_CATEGORIES.LIGHTING,
            description = "Conta Luz"
        ),
    )
    override fun fetchAll(): MutableList<Expense> {
        return expenses
    }

    override fun addExpense(expense: Expense) {
        expenses.add(expense)
    }

    override fun removeExpense(expense: Expense) {
        expenses.remove(expense)
    }

    override fun updateExpense(expense: Expense) {
        if(expenses.removeIf { it.id.compareTo(expense.id) == 0 }){
            expenses.add(expense)
        }
    }
}