package com.example.budgetapp.domain.models

interface ICategories {
    enum class CATEGORIES(displayName: String)

    fun getCategories(): List<String>
}
