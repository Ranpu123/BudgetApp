package com.example.budgetapp.services.local.dao.income

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.budgetapp.domain.models.income.Income
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeDao {
    @Query("Select * FROM Income")
    fun fetchAll(): Flow<List<Income>>

    @Insert
    suspend fun add(income: Income): Long

    @Upsert
    suspend fun update(income: Income): Long

    @Delete
    suspend fun remove(income: Income): Int

}