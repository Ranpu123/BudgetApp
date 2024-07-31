package com.example.budgetapp.domain.use_cases

import org.junit.Assert.*
import org.junit.Test

class ValidateTransactionDescriptionTest{

    @Test
    fun `when description is empty or blank should be invalid`(){

        var res = ValidateTransactionDescription().execute(" ")
        var expected = ValidateResult(false, "Description can't be empty")
        assertEquals(expected.success, res.success)
        assertEquals(expected.errorMessage, res.errorMessage)
    }

    @Test
    fun `when description is null should be invalid`(){
        var description: String? = null
        var res = ValidateTransactionDescription().execute(description)
        var expected = ValidateResult(false, "Description can't be empty")
        assertEquals(expected.success, res.success)
        assertEquals(expected.errorMessage, res.errorMessage)
    }
    @Test
    fun `should be valid description`(){
        var res = ValidateTransactionDescription().execute("Outros")
        var expected = ValidateResult(true, null)
        assertEquals(expected.success, res.success)
        assertEquals(expected.errorMessage, res.errorMessage)
    }
}