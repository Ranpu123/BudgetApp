package com.example.budgetapp.domain.use_cases

import org.junit.Assert.*
import org.junit.Test

class ValidateTransactionValueTest{

    @Test
    fun `when value is zero should be invalid`(){
        var res = ValidateTransactionValue().execute(0.0)
        var expected = ValidateResult(false, "Value can't be zero.")
        assertEquals(expected.success, res.success)
        assertEquals(expected.errorMessage, res.errorMessage)
    }

    @Test
    fun `when value is null should be invalid`(){
        var res = ValidateTransactionValue().execute(null)
        var expected = ValidateResult(false, "Value can't be zero.")
        assertEquals(expected.success, res.success)
        assertEquals(expected.errorMessage, res.errorMessage)
    }

    @Test
    fun `when value is positive should be valid`(){
        var res = ValidateTransactionValue().execute(50.0)
        var expected = ValidateResult(true, null)
        assertEquals(expected.success, res.success)
        assertEquals(expected.errorMessage, res.errorMessage)
    }

    @Test
    fun `when value is negative should be valid`(){
        var res = ValidateTransactionValue().execute(-50.0)
        var expected = ValidateResult(true, null)
        assertEquals(expected.success, res.success)
        assertEquals(expected.errorMessage, res.errorMessage)
    }
}