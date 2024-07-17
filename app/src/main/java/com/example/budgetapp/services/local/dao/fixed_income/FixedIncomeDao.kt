package com.example.budgetapp.services.local.dao.fixed_income

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
import com.example.budgetapp.services.local.models.RoomFixedIncome
import kotlinx.coroutines.flow.Flow

@Dao
abstract class FixedIncomeDao {
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM RoomFixedIncome WHERE deleted = false AND userId = :userId")
    abstract fun fetchAll(userId: Int): Flow<List<FixedIncome>>

    @RewriteQueriesToDropUnusedColumns
    @Query("Select * FROM RoomFixedIncome WHERE dirty = true AND deleted = false AND userId = :userId")
    abstract suspend fun getDirtyEntries(userId: Int): List<FixedIncome>

    @Query("SELECT * FROM RoomFixedIncome WHERE id = :id")
    abstract suspend fun getFixedIncome(id: String): RoomFixedIncome

    @Query("SELECT * FROM RoomFixedIncome WHERE deleted = true AND userId = :userId")
    abstract suspend fun getToDeleteFixedIncome(userId: Int): List<FixedIncome>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun add(fixedIncome: RoomFixedIncome): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun add(fixedIncomes: List<RoomFixedIncome>): List<Long>

    @Upsert
    abstract suspend fun update(fixedIncome: RoomFixedIncome): Long

    @Update
    abstract suspend fun update(fixedIncomes: List<RoomFixedIncome>): Int

    @Transaction
    @Query("")
    suspend fun clearDirty(fixedIncomes: List<RoomFixedIncome>): Int{
        fixedIncomes.forEach { it.dirty = false }
        return update(fixedIncomes)
    }

    @Transaction
    @Query("UPDATE RoomFixedIncome SET deleted = true, dirty = true WHERE id = :id")
    abstract suspend fun remove(id: String): Int

    @Delete
    abstract suspend fun hardRemove(fixedIncome: RoomFixedIncome): Int

    @Delete
    abstract suspend fun hardRemove(fixedIncomes: List<RoomFixedIncome>): Int
}