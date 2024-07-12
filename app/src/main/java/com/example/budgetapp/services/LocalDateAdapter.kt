package com.example.budgetapp.services

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateAdapter: TypeAdapter<LocalDate>() {
    override fun write(jsonWriter: JsonWriter, value: LocalDate) {
        jsonWriter.value(value.format(DateTimeFormatter.ISO_LOCAL_DATE))
    }

    override fun read(jsonReader: JsonReader): LocalDate {
        return LocalDate.parse(jsonReader.nextString(), DateTimeFormatter.ISO_LOCAL_DATE)
    }
}