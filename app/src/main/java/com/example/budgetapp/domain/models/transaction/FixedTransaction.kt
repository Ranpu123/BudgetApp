package com.example.budgetapp.domain.models.transaction

import com.example.budgetapp.domain.models.ICategories
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

open class FixedTransaction<T>(
    date: LocalDateTime,
    value: Double,
    category: T,
    description: String,
    var active : Boolean = true,
    var endDate: LocalDate? = null,
    var lastDate: LocalDate = if (date.toLocalDate().isBefore(LocalDate.now())) date.toLocalDate() else LocalDate.now()

): Transaction<T>(
    date = date,
    value = value,
    category = category,
    description = description,
) where T: Enum<T>,T: ICategories {
    fun isDue() : Int{

        val curDate = LocalDate.now()
        val lastMonth = curDate.minusMonths(1)
        //If due day is smaller than the current day, check if it was already payed.
        //If due day is bigger, check if the payment was made last month.
        if( (curDate.dayOfMonth >= date.dayOfMonth)
            && lastDate.isBefore(curDate.withDayOfMonth(validDay(date.dayOfMonth, curDate))) ) {

            val res = ChronoUnit.MONTHS.between(lastDate,
                curDate.withDayOfMonth(validDay(date.dayOfMonth, curDate)))

            return res.toInt()

        }else if( (curDate.dayOfMonth < date.dayOfMonth)
            && lastDate.isBefore(lastMonth.withDayOfMonth(validDay(date.dayOfMonth, lastMonth))) ){

            val res = ChronoUnit.MONTHS.between(lastDate,
                lastMonth.withDayOfMonth(validDay(date.dayOfMonth, lastMonth)))

            return res.toInt()

        }else{
            return 0
        }
    }

    //Returns a valid day based on the month of a given date
    private fun validDay(newDay: Int, date: LocalDate): Int{
        return if(newDay > date.lengthOfMonth()) date.lengthOfMonth() else newDay
    }
}