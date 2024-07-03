package com.example.budgetapp.services.database

import androidx.room.TypeConverter
import com.example.budgetapp.domain.models.expense.ExpenseCategory
import com.example.budgetapp.domain.models.income.IncomeCategory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class Converters {

    @TypeConverter
    fun dateTimeFromTimestamp(value: String): LocalDateTime{
        return value.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME) }
    }

    @TypeConverter
    fun dateTimeToTimestamp(value: LocalDateTime): String{
        return value.let { it.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) }
    }

    @TypeConverter
    fun dateFromTimestamp(value: String?): LocalDate?{
        return value?.let { LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE) }
    }

    @TypeConverter
    fun dateToTimestamp(value: LocalDate?): String?{
        return value?.let { it.format(DateTimeFormatter.ISO_LOCAL_DATE) }
    }

    @TypeConverter
    fun toExpenseCategory(value: Int): ExpenseCategory{
        return value.let { enumValues<ExpenseCategory>()[it] }
    }

    @TypeConverter
    fun fromExpenseCategory(value: ExpenseCategory): Int{
        return value.let { it.ordinal }
    }

    @TypeConverter
    fun toIncomeCategory(value: Int): IncomeCategory {
        return value.let { enumValues<IncomeCategory>()[it] }
    }

    @TypeConverter
    fun fromIncomeCategory(value: IncomeCategory): Int{
        return value.let { it.ordinal }
    }

    @TypeConverter
    fun fromUUID(value: UUID): String{
        return value.let { it.toString() }
    }

    @TypeConverter
    fun toUUID(value: String): UUID{
        return value.let { UUID.fromString(it) }
    }

}