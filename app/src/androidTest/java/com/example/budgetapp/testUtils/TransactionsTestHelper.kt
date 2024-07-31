package com.example.budgetapp.testUtils

import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.domain.models.expense.ExpenseCategory
import com.example.budgetapp.domain.models.expense.FixedExpense
import com.example.budgetapp.domain.models.income.FixedIncome
import com.example.budgetapp.domain.models.income.Income
import com.example.budgetapp.domain.models.income.IncomeCategory
import com.example.budgetapp.domain.models.transaction.Transaction
import java.time.LocalDate
import java.time.LocalDateTime

object TransactionsTestHelper {
    val incomes = listOf(
        Income(
            value = 50.0,
            category = IncomeCategory.OTHER,
            description = "description",
            date = LocalDateTime.of(2024,7,23,12,0)
        ),
        Income(
            value = 43.21,
            category = IncomeCategory.GIFT,
            description = "Presente",
            date = LocalDateTime.of(2024,7,23,12,0)
        ),
    )

    val expenses = listOf(
        Expense(
            value = -50.0,
            category = ExpenseCategory.OTHER,
            description = "description",
            date = LocalDateTime.of(2024,6,23,12,0)
        ),
        Expense(
            value = -43.21,
            category = ExpenseCategory.RESTAURANT,
            description = "Presente",
            date = LocalDateTime.of(2024,6,23,12,0)
        ),
    )

    val fixedIncomes = listOf(
        FixedIncome(
            value = 50.0,
            category = IncomeCategory.OTHER,
            description = "description",
            date = LocalDateTime.of(2024,4,23,12,0),
            lastDate = LocalDate.of(2024,5,23),
        )
    )

    val fixedExpenses = listOf(
        FixedExpense(
            value = -50.0,
            category = ExpenseCategory.OTHER,
            description = "description",
            date = LocalDateTime.of(2024,4,23,12,0),
            lastDate = LocalDate.of(2024,5,23),
        )
    )

    val transactions = listOf(
        Transaction(
            value = -43.21,
            category = IncomeCategory.GIFT,
            description = "Presente",
            date = LocalDateTime.of(2024,6,23,12,0)
        ),
        Transaction(
            value = -43.21,
            category = ExpenseCategory.RESTAURANT,
            description = "Restaurante",
            date = LocalDateTime.of(2024,6,23,12,0)
        )
    )
}