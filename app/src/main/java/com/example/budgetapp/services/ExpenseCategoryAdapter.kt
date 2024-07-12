package com.example.budgetapp.services

import com.example.budgetapp.domain.models.expense.ExpenseCategory
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class ExpenseCategoryAdapter: TypeAdapter<ExpenseCategory>() {
    override fun write(jsonWriter: JsonWriter?, value: ExpenseCategory) {
        jsonWriter?.value(value.ordinal)
    }

    override fun read(jsonReader: JsonReader?): ExpenseCategory {
        return enumValues<ExpenseCategory>()[jsonReader?.nextInt()!!]
    }
}