package com.example.budgetapp.utils

import com.example.budgetapp.domain.models.transaction.Transaction

fun sortByMonth(transactions: List<Transaction<*>>): Map<Any?, List<Transaction<*>>> =
    transactions.sortedByDescending { it.date }.groupBy { it.date.toLocalDate().withDayOfMonth(1) }

fun sortByCategory(transactions: List<Transaction<*>>): Map<Any?, List<Transaction<*>>> =
    transactions.groupBy { it.category }

fun sortByDay(transactions: List<Transaction<*>>): Map<Any?, List<Transaction<*>>> =
    transactions.sortedByDescending { it.date }.groupBy { it.date.toLocalDate() }