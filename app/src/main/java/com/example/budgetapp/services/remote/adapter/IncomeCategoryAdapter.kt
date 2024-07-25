package com.example.budgetapp.services.remote.adapter

import com.example.budgetapp.domain.models.income.IncomeCategory
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class IncomeCategoryAdapter: TypeAdapter<IncomeCategory>() {
    override fun write(jsonWriter: JsonWriter?, value: IncomeCategory) {
        jsonWriter?.value(value.ordinal)
    }

    override fun read(jsonReader: JsonReader?): IncomeCategory {
        return enumValues<IncomeCategory>()[jsonReader?.nextInt()!!]
    }
}