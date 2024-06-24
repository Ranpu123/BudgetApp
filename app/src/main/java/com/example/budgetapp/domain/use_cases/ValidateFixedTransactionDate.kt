package com.example.budgetapp.domain.use_cases

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ValidateFixedTransactionDate {

    fun execute(date: LocalDate): ValidateResult{
        if (date.isBefore(LocalDate.now())){
            return ValidateResult(errorMessage = "Date must be after ${LocalDate.now().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy"))}")
        }
        return ValidateResult(success = true)
    }
}