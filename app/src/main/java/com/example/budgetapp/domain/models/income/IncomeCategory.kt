package com.example.budgetapp.domain.models.income

import com.example.budgetapp.domain.models.ICategories

enum class IncomeCategory(override val displayName: String): ICategories {
    COMMISSION("Comissão"),
    GIFT("Presente"),
    INTEREST("Juros"),
    INVESTMENT("Investimentos"),
    SALARY("Salário"),
    SELL("Venda"),
    TIPS("Gorjeta"),
    OTHER("Outros")
}