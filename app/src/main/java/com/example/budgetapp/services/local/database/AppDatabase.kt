package com.example.budgetapp.services.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.domain.models.expense.FixedExpense
import com.example.budgetapp.domain.models.income.FixedIncome
import com.example.budgetapp.domain.models.income.Income
import com.example.budgetapp.services.local.dao.fixed_income.FixedIncomeDao
import com.example.budgetapp.services.local.dao.expense.ExpenseDao
import com.example.budgetapp.services.local.dao.fixed_expense.FixedExpenseDao
import com.example.budgetapp.services.local.dao.income.IncomeDao
import com.example.budgetapp.services.local.models.RoomExpense
import com.example.budgetapp.services.local.models.RoomFixedExpense
import com.example.budgetapp.services.local.models.RoomFixedIncome
import com.example.budgetapp.services.local.models.RoomIncome

@Database(
    entities = [RoomIncome::class, RoomExpense::class, RoomFixedExpense::class, RoomFixedIncome::class],
    version = 9,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun incomeDao(): IncomeDao

    abstract fun expenseDao(): ExpenseDao

    abstract fun fixedExpenseDao(): FixedExpenseDao

    abstract fun fixedIncomeDao(): FixedIncomeDao
}