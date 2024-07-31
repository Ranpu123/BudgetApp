package com.example.budgetapp.domain.use_cases

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ValidateFixedTransactionDateTest{


    @Test
    fun `When date is the current date should be valid`(){
        var res = ValidateFixedTransactionDate().execute(LocalDate.now())
        var expected = ValidateResult(true, null)
        assertEquals(expected.success, res.success)
        assertEquals(expected.errorMessage, res.errorMessage)
    }

    @Test
    fun `When date is before the current date should be invalid`(){
        var res = ValidateFixedTransactionDate().execute(LocalDate.now().minusDays(5))
        var expected = ValidateResult(false, "Date must be after ${LocalDate.now().format(
            DateTimeFormatter.ofPattern("dd/MM/yyyy"))}")
        assertEquals(expected.success, res.success)
        assertEquals(expected.errorMessage, res.errorMessage)
    }

    @Test
    fun `When date is after current date should be valid`(){
        var res = ValidateFixedTransactionDate().execute(LocalDate.now().plusDays(5))
        var expected = ValidateResult(true,null)
        assertEquals(expected.success, res.success)
        assertEquals(expected.errorMessage, res.errorMessage)
    }
}