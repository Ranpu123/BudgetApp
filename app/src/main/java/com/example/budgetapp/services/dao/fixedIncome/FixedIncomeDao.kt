package com.example.budgetapp.services.dao.fixedIncome

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.budgetapp.domain.models.expense.FixedExpense
import com.example.budgetapp.domain.models.income.FixedIncome
import kotlinx.coroutines.flow.Flow

@Dao
interface FixedIncomeDao {
    @Query("SELECT * FROM FixedIncome")
    suspend fun fetchAll(): Flow<List<FixedIncome>>

    @Insert
    suspend fun add(fixedIncome: FixedIncome): Long

    @Upsert
    suspend fun update(fixedIncome: FixedIncome): Int

    @Delete
    suspend fun delete(fixedIncome: FixedIncome): Int
}