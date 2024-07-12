package com.example.budgetapp.services.remote.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeAdapter: TypeAdapter<LocalDateTime>() {
    override fun write(jsonWriter: JsonWriter, value: LocalDateTime) {
        jsonWriter.value(value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
    }

    override fun read(jsonReader: JsonReader): LocalDateTime {
        return LocalDateTime.parse(jsonReader.nextString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }
}