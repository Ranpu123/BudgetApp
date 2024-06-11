package com.example.budgetapp.domain.models.expense

import com.example.budgetapp.domain.models.ICategories

enum class FIXED_EXPENSE_CATEGORIE(override val displayName: String): ICategories {
    STREAMING("Streaming"),
    INTERNET("Internet"),
    PHONE_PLAN("Plano de celular"),
    INSURANCE("Seguro"),
    INSTALLMENT("Prestação"),
    RENT("Aluguel"),
    OTHERS("Outros")
}