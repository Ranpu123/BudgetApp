package com.example.budgetapp.domain.models.income

import androidx.room.Entity
import com.example.budgetapp.domain.models.transaction.FixedTransaction
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
open class FixedIncome(
    date: LocalDateTime,
    value: Double,
    category: IncomeCategory,
    description: String,
    active: Boolean = true,
    endDate: LocalDate? = null,
    lastDate: LocalDate = if (date.toLocalDate().isBefore(LocalDate.now())) date.toLocalDate() else date.toLocalDate().minusMonths(1)

) : FixedTransaction<IncomeCategory>(
    date = date,
    value = value,
    category = category,
    description = description,
    lastDate = lastDate,
    active = active,
    endDate = endDate
){
    init {
        require(value > 0.0){"Fixed Income requires positive value!"}
    }
}