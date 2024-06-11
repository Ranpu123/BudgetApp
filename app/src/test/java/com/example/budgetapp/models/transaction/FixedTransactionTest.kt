package com.example.budgetapp.models.transaction


import com.example.budgetapp.domain.models.expense.FIXED_EXPENSE_CATEGORIE
import com.example.budgetapp.domain.models.transaction.FixedTransaction
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime


class FixedTransactionTest {

    @Test
    fun payment_due_payed_return_0() {
        val lastDay = LocalDateTime.now().minusDays(1)
        val transaction = FixedTransaction(
            date = lastDay,
            value = 500.00,
            category = FIXED_EXPENSE_CATEGORIE.RENT,
            description = "My Apartment",
        )

        val res = transaction.isDue()

        assertEquals(0,res)
    }

    @Test
    fun payment_due_for_last_month_return_1() {
        val lastMonth = LocalDateTime.now().minusMonths(1).minusDays(1)
        val transaction = FixedTransaction(
            date = lastMonth,
            value = 500.00,
            category = FIXED_EXPENSE_CATEGORIE.RENT,
            description = "My Apartment",
        )

        val res = transaction.isDue()

        assertEquals(1, res)
    }

    @Test
    fun payment_due_payed_smaller_day_return_0() {
        val lastMonth = LocalDateTime.now().minusMonths(1).plusDays(1)
        val transaction = FixedTransaction(
            date = lastMonth.minusMonths(2),
            value = 500.00,
            category = FIXED_EXPENSE_CATEGORIE.RENT,
            description = "My Apartment",
            lastDate = lastMonth.toLocalDate()
        )

        val res = transaction.isDue()

        assertEquals(0,res)
    }

    @Test
    fun payment_due_for_last_month_smaller_day_return_1() {
        val nextDay = LocalDateTime.now().plusDays(1)
        val transaction = FixedTransaction(
            date = nextDay.minusMonths(3),
            value = 500.00,
            category = FIXED_EXPENSE_CATEGORIE.RENT,
            description = "My Apartment",
            lastDate = nextDay.minusMonths(2).toLocalDate()
        )

        val res = transaction.isDue()

        assertEquals(1, res)
    }
}