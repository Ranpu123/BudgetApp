package com.example.budgetapp.utils

import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

class ValidDayofMonthKtTest{

    @Test
    fun `when day is 31 in a month with 29 days should return day 29`(){
        var date = LocalDate.of(2024,2,5)
        var res = validDayofMonth(31,date)

        assertEquals(29, res)
    }

    @Test
    fun `when day 30 in a month with 31 days should return day 30`(){
        var date = LocalDate.of(2024,7,5)
        var res = validDayofMonth(30,date)

        assertEquals(30, res)
    }
}