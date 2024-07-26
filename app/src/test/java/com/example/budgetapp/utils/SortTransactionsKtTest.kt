package com.example.budgetapp.utils

import com.example.budgetapp.domain.models.expense.ExpenseCategory
import com.example.budgetapp.domain.models.transaction.Transaction
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class SortTransactionsKtTest{

    private val date = LocalDate.of(2024, 7, 23).atTime(LocalTime.now())
    private val transactions = listOf(
        Transaction(
            date.minusDays(1),
            value = -10.00,
            description = "Teste1",
            category = ExpenseCategory.OTHER
        ),
        Transaction(
            date,
            value = -10.00,
            description = "Teste",
            category = ExpenseCategory.OTHER
        ),
        Transaction(
            date.minusDays(1).minusMonths(1),
            value = -10.00,
            description = "Teste3",
            category = ExpenseCategory.RENT
        ),
        Transaction(
            date.minusMonths(1),
            value = -10.00,
            description = "Teste2",
            category = ExpenseCategory.RESTAURANT
        ),

    )

    @Test
    fun `agrupar por mês e ordenar por data`(){
        var res = sortByMonth(transactions.reversed())

        var expected = mapOf(
            date.withDayOfMonth(1).toLocalDate() to listOf(
                transactions[1], transactions[0]
            ),
            date.minusMonths(1)
                .withDayOfMonth(1).toLocalDate() to listOf(
                transactions[3], transactions[2]
            )
        )

        assertEquals(expected, res)

    }

    @Test
    fun `agrupar por categoria`(){
        var res = sortByCategory(transactions)

        var expected = mapOf(
            ExpenseCategory.OTHER to listOf(
                transactions[0], transactions[1]
            ),
            ExpenseCategory.RENT to listOf(
                transactions[2]
            ),
            ExpenseCategory.RESTAURANT to listOf(
                transactions[3]
            )
        )

        assertEquals(expected, res)
    }

    @Test
    fun `agrupar por data`(){
        var res = sortByDay(transactions.reversed())

        var expected = mapOf(
            date.toLocalDate() to listOf(
                transactions[1]
            ),
            date.minusDays(1).toLocalDate() to listOf(
                transactions[0]
            ),
            date.minusMonths(1).toLocalDate() to listOf(
                transactions[3]
            ),
            date.minusMonths(1).minusDays(1).toLocalDate() to listOf(
                transactions[2]
            )
        )

        assertEquals(expected, res)
    }
}