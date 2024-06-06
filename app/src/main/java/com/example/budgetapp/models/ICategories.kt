package com.example.budgetapp.models

interface ICategories {
    enum class CATEGORIES(displayName: String)

    fun getCategories(): List<String>
}
