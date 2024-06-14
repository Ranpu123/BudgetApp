package com.example.budgetapp.services.repository.fixed_income

import com.example.budgetapp.domain.models.income.FIXED_INCOME_CATEGORIES
import com.example.budgetapp.domain.repository_interfaces.IFixedIncomeRepository
import com.example.budgetapp.domain.models.income.FixedIncome
import com.example.budgetapp.domain.models.income.INCOME_CATEGORIES
import java.time.LocalDate
import java.time.LocalDateTime

object LocalFixedIncomeRepository: IFixedIncomeRepository {

    private val fixedIncomes: MutableList<FixedIncome> = mutableListOf(
        FixedIncome(
            date = LocalDateTime.parse("2024-06-03T10:00:00"),
            value = 1415.00,
            category = INCOME_CATEGORIES.SALARY,
            description = "Salário",
            //lastDate = LocalDate.parse("2024-05-01"),
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