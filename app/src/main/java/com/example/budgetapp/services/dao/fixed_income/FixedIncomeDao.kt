package com.example.budgetapp.services.dao.fixed_income

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.budgetapp.domain.models.income.FixedIncome
import kotlinx.coroutines.flow.Flow

@Dao
interface FixedIncomeDao {
    @Query("SELECT * FROM FixedIncome")
    fun fetchAll(): Flow<List<FixedIncome>>

    @Insert
    suspend fun add(fixedIncome: FixedIncome): Long

    @Upsert
    suspend fun update(fixedIncome: FixedIncome): Long

    @Update
    suspend fun update(fixedIncomes: List<FixedIncome>): Int

    @Delete
    suspend fun remove(fixedIncome: FixedIncome): Int
}