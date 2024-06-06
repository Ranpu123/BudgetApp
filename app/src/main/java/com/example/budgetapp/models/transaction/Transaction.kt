package com.example.budgetapp.models.transaction

import java.time.LocalDateTime

open class Transaction (
    var date: LocalDateTime,
    var value: Double,
    var category: String,
    var description: String,

){
    init {
        require( value != 0.0)
    }
    override fun toString() : String {
        return "$category $description"
    }
}
