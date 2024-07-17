package com.example.budgetapp.services.local.dao.expense

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.services.local.models.RoomExpense
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ExpenseDao {
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM RoomExpense WHERE deleted = false AND userId = :userId")
    abstract fun fetchAll(userId: Int): Flow<List<Expense>>
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM RoomExpense WHERE dirty = true AND deleted = false AND userId = :userId")
    abstract suspend fun getDirtyEntries(userId: Int): List<Expense>

    @Query("SELECT * FROM RoomExpense WHERE id = :id")
    abstract suspend fun getExpense(id: String): RoomExpense

    @Query("SELECT * FROM RoomExpense WHERE deleted = true AND userId = :userId")
    abstract suspend fun getToDeleteExpenses(userId: Int): List<Expense>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun add(expense: RoomExpense): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun add(expenses: List<RoomExpense>): List<Long>

    @Upsert
    abstract suspend fun update(expense: RoomExpense): Long

    @Update
    abstract suspend fun update(expenses: List<RoomExpense>): Int

    @Transaction
    @Query("")
    suspend fun clearDirty(expenses: List<RoomExpense>): Int{
        expenses.forEach { it.dirty = false }
        return update(expenses)
    }

    @Transaction
    @Query("UPDATE RoomExpense SET deleted = true, dirty = true WHERE id = :id")
    abstract suspend fun remove(id: String): Int

    @Delete
    abstract suspend fun hardRemove(expense: RoomExpense): Int

    @Delete
    abstract suspend fun hardRemove(expenses: List<RoomExpense>): Int
}