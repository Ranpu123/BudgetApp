package com.example.budgetapp.services.repository.income

import com.example.budgetapp.domain.models.income.INCOME_CATEGORIES
import com.example.budgetapp.domain.repository_interfaces.IIncomeRepository
import com.example.budgetapp.domain.models.income.Income
import java.time.LocalDateTime

object LocalIncomeRepository: IIncomeRepository {

    private val incomes: MutableList<Income> = mutableListOf(
        Income(
            date = LocalDateTime.parse("2024-04-30T10:30:00"),
            value = 1415.00,
            category = INCOME_CATEGORIES.SALARY,
            description = "Salário"
        ),
        Income(
            date = LocalDateTime.parse("2024-05-31T10:30:00"),
            value = 1415.00,
            category = INCOME_CATEGORIES.SALARY,
            description = "Salário"
        ),
        Income(
            date = LocalDateTime.parse("2024-05-31T08:30:00"),
            value = 1300.00,
            category = INCOME_CATEGORIES.SELL,
            description = "Venda Computador"
        ),
    )

    override fun fetchAll(): MutableList<Income> {
        return incomes
    }

    override fun addIncome(income: Income) {
        incomes.add(income)
    }

    override fun removeIncome(income: Income) {
        incomes.remove(income)
    }

    override fun updateIncome(income: Income) {
        if(incomes.removeIf { it.id.compareTo(income.id) == 0}){
            incomes.add(income)
        }
    }
}