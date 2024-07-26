package com.example.budgetapp.domain.use_cases

import org.junit.Assert.*
import org.junit.Test

class ValidateTransactionValueTest{

    @Test
    fun `valor zero inválido`(){
        var res = ValidateTransactionValue().execute(0.0)
        var expected = ValidateResult(false, "Value can't be zero.")
        assertEquals(expected.success, res.success)
        assertEquals(expected.errorMessage, res.errorMessage)
    }

    @Test
    fun `valor nulo inválido`(){
        var res = ValidateTransactionValue().execute(null)
        var expected = ValidateResult(false, "Value can't be zero.")
        assertEquals(expected.success, res.success)
        assertEquals(expected.errorMessage, res.errorMessage)
    }

    @Test
    fun `valor válido`(){
        var res = ValidateTransactionValue().execute(50.0)
        var expected = ValidateResult(true, null)
        assertEquals(expected.success, res.success)
        assertEquals(expected.errorMessage, res.errorMessage)
    }
}