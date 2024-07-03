package com.example.budgetapp.services.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.domain.models.income.Income
import com.example.budgetapp.services.dao.fixedIncome.FixedIncomeDao
import com.example.budgetapp.services.dao.expense.ExpenseDao
import com.example.budgetapp.services.dao.fixedExpense.FixedExpenseDao
import com.example.budgetapp.services.dao.income.IncomeDao

@Database(
    entities = [Income::class, Expense::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun incomeDao(): IncomeDao
    abstract fun expenseDao(): ExpenseDao

    abstract fun fixedExpenseDao(): FixedExpenseDao

    abstract fun fixedIncomeDao(): FixedIncomeDao
}