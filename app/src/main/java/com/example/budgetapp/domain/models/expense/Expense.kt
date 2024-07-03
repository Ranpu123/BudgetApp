package com.example.budgetapp.domain.models.expense

import androidx.room.Entity
import com.example.budgetapp.domain.models.transaction.Transaction
import java.time.LocalDateTime

@Entity
open class Expense(
    date: LocalDateTime,
    value: Double,
    category: ExpenseCategory,
    description: String,

    ) : Transaction<ExpenseCategory>(
    date = date,
    value = value,
    category = category,
    description = description
){
    init {
        require(value < 0.0){"Expense requires negative value!"}
    }
}