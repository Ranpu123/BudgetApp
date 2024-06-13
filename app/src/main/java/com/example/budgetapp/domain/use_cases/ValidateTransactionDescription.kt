package com.example.budgetapp.domain.use_cases

class ValidateTransactionDescription {

    fun execute(description: String): ValidateResult{
        if (description.isEmpty()){
            return ValidateResult(errorMessage = "Description can't be empty")
        }
        return ValidateResult(success = true)
    }
}