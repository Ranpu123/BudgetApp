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
    fun `data mesmo dia válida`(){
        var res = ValidateFixedTransactionDate().execute(LocalDate.now())
        var expected = ValidateResult(true, null)
        assertEquals(expected.success, res.success)
        assertEquals(expected.errorMessage, res.errorMessage)
    }

    @Test
    fun `data anterior atual inválida`(){
        var res = ValidateFixedTransactionDate().execute(LocalDate.now().minusDays(5))
        var expected = ValidateResult(false, "Date must be after ${LocalDate.now().format(
            DateTimeFormatter.ofPattern("dd/MM/yyyy"))}")
        assertEquals(expected.success, res.success)
        assertEquals(expected.errorMessage, res.errorMessage)
    }

    @Test
    fun `data após atual válida`(){
        var res = ValidateFixedTransactionDate().execute(LocalDate.now().plusDays(5))
        var expected = ValidateResult(true,null)
        assertEquals(expected.success, res.success)
        assertEquals(expected.errorMessage, res.errorMessage)
    }
}