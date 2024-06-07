package com.example.budgetapp.domain.repository_interfaces

import com.example.budgetapp.domain.models.income.FixedIncome


interface IFixedIncomeRepository {
    fun fetchAll():MutableList<FixedIncome>
    fun addFixedIncome(fixedIncome: FixedIncome)
    fun removeFixedIncome(fixedIncome: FixedIncome)
    fun updateFixedIncome(fixedIncome: FixedIncome)
}