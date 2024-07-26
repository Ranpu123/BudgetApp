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
    fun `valor string moeda com 99 centavos`(){
        var res = formatCurrency("R$ 9999")

        assertEquals("R$ 99,99", res)
    }

    @Test
    fun `valor moeda com 99 centavos`(){
        var res = formatCurrency(99.99)

        assertEquals("R$ 99,99", res)
    }

    @Test
    fun `string invalida`(){
        var res = formatCurrency("Texto")

        assertEquals("0", res)
    }

    @Test
    fun `valor invalido`(){
        var res = formatCurrency(Double.POSITIVE_INFINITY)

        assertEquals("0", res)
    }
}