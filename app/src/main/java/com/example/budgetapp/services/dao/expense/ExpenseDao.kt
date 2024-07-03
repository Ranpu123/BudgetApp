package com.example.budgetapp.services.dao.expense

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.budgetapp.domain.models.expense.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Query("Select * FROM Expense")
    fun fetchAll(): Flow<List<Expense>>

    @Insert
    fun add(expense: Expense): Long

    @Delete
    fun remove(expense: Expense): Int

    @Upsert
    fun update(expense: Expense): Int
}