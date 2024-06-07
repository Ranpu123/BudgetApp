package com.example.budgetapp.domain.models.transaction

import java.time.LocalDateTime
import java.util.UUID

open class Transaction (
    var date: LocalDateTime,
    var value: Double,
    var category: String,
    var description: String,
    val id: UUID = UUID.randomUUID(),
){
    init {
        require( value != 0.0)
    }
    override fun toString() : String {
        return "$category $description"
    }
}
