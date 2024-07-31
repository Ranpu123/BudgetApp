package com.example.budgetapp.utils

import android.util.Log
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CurrencyToDoubleKtTest{
    @Before
    fun setup(){
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
    }

    @Test
    fun `when string has 99 cents should return ,99`(){
        var res = currencyToDouble("R$ 99.99")

        assertEquals(99.99, res, 0.0001)
    }

    @Test
    fun `when string is invalid should return 0`(){
        var res = currencyToDouble("texto")

        assertEquals(0.0, res, 0.0001)
    }

}