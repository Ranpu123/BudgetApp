package com.example.budgetapp.services.local.dao.fixed_expense

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
import com.example.budgetapp.domain.models.expense.FixedExpense
import com.example.budgetapp.services.local.models.RoomFixedExpense
import kotlinx.coroutines.flow.Flow

@Dao
abstract class FixedExpenseDao {
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM RoomFixedExpense WHERE deleted = false AND userId = :userId")
    abstract fun fetchAll(userId: Int): Flow<List<FixedExpense>>

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM RoomFixedExpense WHERE dirty = true AND deleted = false AND userId = :userId")
    abstract suspend fun getDirtyEntries(userId: Int): List<FixedExpense>

    @Query("SELECT * FROM RoomFixedExpense WHERE id = :id")
    abstract suspend fun getFixedExpense(id: String): RoomFixedExpense

    @Query("SELECT * FROM RoomFixedExpense WHERE deleted = true AND userId = :userId")
    abstract suspend fun getToDeleteFixedExpenses(userId: Int): List<FixedExpense>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun add(fixedExpense: RoomFixedExpense): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun add(fixedExpenses: List<RoomFixedExpense>): List<Long>

    @Upsert
    abstract suspend fun update(fixedExpense: RoomFixedExpense): Long

    @Update
    abstract suspend fun update(fixedExpenses: List<RoomFixedExpense>): Int

    @Transaction
    @Query("")
    suspend fun clearDirty(fixedExpenses: List<RoomFixedExpense>): Int{
        fixedExpenses.forEach{it.dirty = false}
        return update(fixedExpenses)
    }

    @Transaction
    @Query("UPDATE RoomFixedExpense SET deleted = true, dirty = true WHERE id = :id")
    abstract suspend fun remove(id: String): Int

    @Delete
    abstract suspend fun hardRemove(fixedExpense: RoomFixedExpense): Int

    @Delete
    abstract suspend fun hardRemove(fixedExpenses: List<RoomFixedExpense>): Int

}