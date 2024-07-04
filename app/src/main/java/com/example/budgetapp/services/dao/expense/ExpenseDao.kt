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
    suspend fun add(expense: Expense): Long

    @Upsert
    suspend fun update(expense: Expense): Long

    @Delete
    suspend fun remove(expense: Expense): Int

}