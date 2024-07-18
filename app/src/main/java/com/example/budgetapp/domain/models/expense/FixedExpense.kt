package com.example.budgetapp.domain.models.expense

import androidx.room.Entity
import com.example.budgetapp.domain.models.transaction.FixedTransaction
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
class FixedExpense(
    date: LocalDateTime,
    value: Double,
    category: ExpenseCategory,
    description: String,
    active : Boolean = true,
    endDate: LocalDate? = null,
    lastDate: LocalDate = if (date.toLocalDate().isBefore(LocalDate.now())) date.toLocalDate() else date.toLocalDate().minusMonths(1)

) : FixedTransaction<ExpenseCategory>(
    date = date,
    value = value,
    category = category,
    description = description,
    lastDate = lastDate,
    active = active,
    endDate = endDate
){
    init {
        require(value < 0.0){"Fixed Income requires negative value!"}
    }

}