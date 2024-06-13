package com.example.budgetapp.domain.use_cases

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ValidateTransactionValue {
    fun execute(value: Double): ValidateResult{
        if (value == 0.0){
            return ValidateResult(errorMessage = "Value can't be zero.")
        }
        return ValidateResult(success = true)
    }
}