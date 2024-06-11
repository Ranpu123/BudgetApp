package com.example.budgetapp.domain.models.income

import com.example.budgetapp.domain.models.ICategories

enum class INCOME_CATEGORIES(override val displayName: String): ICategories {
    COMMISSION("Comissão"),
    GIFT("Presente"),
    INTEREST("Juros"),
    INVESTMENT("Investimentos"),
    SALARY("Salário"),
    SELL("Venda"),
    TIPS("Gorjeta"),
    OTHERS("Outros")
}