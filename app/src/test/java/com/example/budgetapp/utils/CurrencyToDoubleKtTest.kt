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
    fun `valor moeda com 99 centavos`(){
        var res = currencyToDouble("R$ 99.99")

        assertEquals(99.99, res, 0.0001)
    }

    @Test
    fun `valor inválido`(){
        var res = currencyToDouble("texto")

        assertEquals(0.0, res, 0.0001)
    }

}