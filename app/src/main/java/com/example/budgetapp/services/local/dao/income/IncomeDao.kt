package com.example.budgetapp.services.local.dao.income

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.example.budgetapp.domain.models.expense.FixedExpense
import com.example.budgetapp.domain.models.income.FixedIncome
import com.example.budgetapp.domain.models.income.Income
import com.example.budgetapp.services.local.models.RoomIncome
import kotlinx.coroutines.flow.Flow

@Dao
abstract class IncomeDao {
    @RewriteQueriesToDropUnusedColumns
    @Query("Select * FROM RoomIncome WHERE deleted = false AND userId = :userId")
    abstract fun fetchAll(userId: Int): Flow<List<Income>>

    @RewriteQueriesToDropUnusedColumns
    @Query("Select * FROM RoomIncome WHERE dirty = true AND deleted = false AND userId = :userId")
    abstract suspend fun getDirtyEntries(userId: Int): List<Income>

    @Query("SELECT * FROM RoomIncome WHERE id = :id")
    abstract suspend fun getIncome(id: String): RoomIncome

    @Query("SELECT * FROM RoomIncome WHERE deleted = true AND userId = :userId")
    abstract suspend fun getToDeleteIncome(userId: Int): List<Income>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun add(income: RoomIncome): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun add(incomes: List<RoomIncome>): List<Long>

    @Upsert
    abstract suspend fun update(income: RoomIncome): Long

    @Update
    abstract suspend fun update(incomes: List<RoomIncome>): Int

    @Transaction
    @Query("")
    suspend fun clearDirty(incomes: List<RoomIncome>): Int{
        incomes.forEach { it.dirty = false }
        return update(incomes)
    }

    @Transaction
    @Query("UPDATE RoomIncome SET deleted = true, dirty = true WHERE id = :id")
    abstract suspend fun remove(id: String): Int

    @Delete
    abstract suspend fun hardRemove(income: RoomIncome): Int

    @Delete
    abstract suspend fun hardRemove(incomes: List<RoomIncome>): Int

}