package com.example.budgetapp.utils

import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

class ValidDayofMonthKtTest{

    @Test
    fun `dia 31 em mes com 29 dias deve retornar dia 29`(){
        var date = LocalDate.of(2024,2,5)
        var res = validDayofMonth(31,date)

        assertEquals(29, res)
    }

    @Test
    fun `dia 30 em mes com 31 dias deve retornar dia 30`(){
        var date = LocalDate.of(2024,7,5)
        var res = validDayofMonth(30,date)

        assertEquals(30, res)
    }
}