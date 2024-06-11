package com.example.budgetapp.domain.models.income

import com.example.budgetapp.domain.models.ICategories

enum class FIXED_INCOME_CATEGORIES(override val displayName: String): ICategories {
    SALARY("Salário"),
    OTHERS("Outros")
}