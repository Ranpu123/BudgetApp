package com.example.budgetapp.services.repository.income

import com.example.budgetapp.models.income.Income

interface IIncomeRepository {
    fun fetchAll():MutableList<Income>
    fun addIncome(income: Income)
    fun removeIncome(income: Income)
    fun updateIncome(income: Income)
}