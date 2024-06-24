package com.example.budgetapp.utils

import java.time.LocalDate

//Returns a valid day based on the month of a given date
fun validDayofMonth(newDay: Int, date: LocalDate): Int{
    return if(newDay > date.lengthOfMonth()) date.lengthOfMonth() else newDay
}