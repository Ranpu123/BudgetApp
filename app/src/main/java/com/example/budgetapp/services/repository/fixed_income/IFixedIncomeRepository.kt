package com.example.budgetapp.services.repository.fixed_income

import com.example.budgetapp.models.income.FixedIncome


interface IFixedIncomeRepository {
    fun fetchAll():MutableList<FixedIncome>
    fun addFixedIncome(fixedIncome: FixedIncome)
    fun removeFixedIncome(fixedIncome: FixedIncome)
    fun updateFixedIncome(fixedIncome: FixedIncome)
}