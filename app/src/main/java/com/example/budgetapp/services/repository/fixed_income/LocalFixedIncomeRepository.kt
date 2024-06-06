package com.example.budgetapp.services.repository.fixed_income

import com.example.budgetapp.models.income.FixedIncome
import java.time.LocalDate
import java.time.LocalDateTime

class LocalFixedIncomeRepository(): IFixedIncomeRepository {

    var fixedIncomes: MutableList<FixedIncome> = mutableListOf(
        FixedIncome(
            date = LocalDateTime.parse("2024-04-31"),
            value = 1415.00,
            category = FixedIncome.Companion.CATEGORIES.SALARY.displayName,
            description = "Salário",
            lastDate = LocalDate.parse("2024-05-31"),
        )
    )

    override fun fetchAll(): MutableList<FixedIncome> {
        return fixedIncomes
    }

    override fun addFixedIncome(fixedIncome: FixedIncome) {
        fixedIncomes.add(fixedIncome)
    }

    override fun removeFixedIncome(fixedIncome: FixedIncome) {
        fixedIncomes.remove(fixedIncome)
    }

    override fun updateFixedIncome(fixedIncome: FixedIncome) {
        if(fixedIncomes.removeIf({it.id.compareTo(fixedIncome.id) == 0})){
            fixedIncomes.add(fixedIncome)
        }
    }
}