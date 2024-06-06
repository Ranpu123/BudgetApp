package com.example.budgetapp.services.repository.fixed_expense

import com.example.budgetapp.models.expense.FixedExpense

class LocalFixedExpenseRepository: IFixedExpenseRepository {

    var fixedExpenses: MutableList<FixedExpense> = mutableListOf()

    override fun fetchAll(): MutableList<FixedExpense> {
        return fixedExpenses
    }

    override fun addFixedExpense(fixedExpense: FixedExpense) {
        fixedExpenses.add(fixedExpense)
    }

    override fun removeFixedExpense(fixedExpense: FixedExpense) {
        fixedExpenses.remove(fixedExpense)
    }

    override fun updateFixedExpense(fixedExpense: FixedExpense) {
        if (fixedExpenses.removeIf { it.id.compareTo(fixedExpense.id) == 0 }){
            fixedExpenses.add(fixedExpense)
        }
    }
}