package com.example.budgetapp.services.dao.fixedExpense

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.budgetapp.domain.models.expense.FixedExpense
import kotlinx.coroutines.flow.Flow

@Dao
interface FixedExpenseDao {
    @Query("SELECT * FROM FixedExpense")
    suspend fun fetchAll(): Flow<List<FixedExpense>>

    @Insert
    suspend fun add(fixedExpense: FixedExpense): Long

    @Upsert
    suspend fun update(fixedExpense: FixedExpense): Int

    @Delete
    suspend fun delete(fixedExpense: FixedExpense): Int
}