package com.example.budgetapp.domain.models.transaction

import android.util.Log
import androidx.room.Entity
import com.example.budgetapp.domain.models.ICategories
import com.example.budgetapp.utils.validDayofMonth
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Entity
open class FixedTransaction<T>(
    date: LocalDateTime,
    value: Double,
    category: T,
    description: String,
    var active : Boolean = true,
    var endDate: LocalDate? = null,
    var lastDate: LocalDate = if (date.toLocalDate().isBefore(LocalDate.now())) date.toLocalDate() else date.toLocalDate().minusMonths(1)
): Transaction<T>(
    date = date,
    value = value,
    category = category,
    description = description,
) where T: Enum<T>,T: ICategories {

    override fun equals(other: Any?): Boolean {
        when(other){
            is FixedTransaction<*> -> {
                return super.equals(other) &&
                        other.active == this.active &&
                        other.endDate?.isEqual(this.endDate) ?: (other.endDate == this.endDate) &&
                        other.lastDate.isEqual(this.lastDate)
            }
            else -> return false
        }
    }

    private fun findCorrectDate(date: LocalDate): LocalDate{
        return if (date.isBefore(LocalDate.now())) date else date.minusMonths(1)
    }
    fun isDue() : Int{

        val curDate = LocalDate.now()
        val lastMonth = curDate.minusMonths(1)

        //If due day is smaller than the current day, check if it was already payed.
        //If due day is bigger, check if the payment was made last month.
        if( (curDate.dayOfMonth >= date.dayOfMonth)
            && lastDate.isBefore(curDate.withDayOfMonth(validDayofMonth(date.dayOfMonth, curDate))) ) {

            val res = ChronoUnit.MONTHS.between(lastDate.withDayOfMonth(validDayofMonth(date.dayOfMonth, curDate)),
                curDate.withDayOfMonth(validDayofMonth(date.dayOfMonth, curDate)))

            return res.toInt()

        }else if( (curDate.dayOfMonth < date.dayOfMonth)
            && lastDate.isBefore(lastMonth.withDayOfMonth(validDayofMonth(date.dayOfMonth, lastMonth))) ){

            val res = ChronoUnit.MONTHS.between(lastDate.withDayOfMonth(validDayofMonth(date.dayOfMonth, lastMonth)),
                lastMonth.withDayOfMonth(validDayofMonth(date.dayOfMonth, lastMonth)))
            return res.toInt()

        }else{
            return 0
        }
    }
}