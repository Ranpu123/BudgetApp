package com.example.budgetapp.utils

import android.util.Log
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FormatCurrencyKtTest{

    @Before
    fun setup(){
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
    }

    @Test
    fun `when string has 99 cents should return formated currency ,99`(){
        var res = formatCurrency("R$ 9999")

        assertEquals("R$ 99,99", res)
    }

    @Test
    fun `when value has ,99 should return 99 cents`(){
        var res = formatCurrency(99.99)

        assertEquals("R$ 99,99", res)
    }

    @Test
    fun `when string is invalid should return 0`(){
        var res = formatCurrency("Texto")

        assertEquals("0", res)
    }

    @Test
    fun `when value is invalid should return 0`(){
        var res = formatCurrency(Double.POSITIVE_INFINITY)

        assertEquals("0", res)
    }
}