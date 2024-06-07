package com.example.budgetapp.domain.repository_interfaces

import com.example.budgetapp.domain.models.income.Income

interface IIncomeRepository {
    fun fetchAll():MutableList<Income>
    fun addIncome(income: Income)
    fun removeIncome(income: Income)
    fun updateIncome(income: Income)
}