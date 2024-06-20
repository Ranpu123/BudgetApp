package com.example.budgetapp.domain.models.expense

import com.example.budgetapp.domain.models.ICategories

enum class ExpenseCategory(override val displayName: String): ICategories {
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
    STREAMING("Streaming"),
    INTERNET("Internet"),
    PHONE_PLAN("Plano de celular"),
    INSURANCE("Seguro"),
    INSTALLMENT("Prestação"),
    OTHER("Outro")
}