package com.example.budgetapp.domain.models.expense

import com.example.budgetapp.domain.models.ICategories

enum class EXPENSE_CATEGORIES(override val displayName: String): ICategories {
    FOOD("Alimentação"),
    GROCERY("Mercado"),
    HEALTH("Saúde"),
    LIGHTING("Luz"),
    RENT("Aluguel"),
    RESTAURANT("Restaurante"),
    TAX("Imposto"),
    WATER("Água"),
    GAS("Gás"),
    FUEL("Combustível"),
    OTHER("Outro")
}