package com.example.budgetapp.domain.models.transaction

import com.example.budgetapp.domain.models.ICategories
import java.time.LocalDateTime
import java.util.UUID
import kotlin.enums.EnumEntries

open class Transaction<T> (
    var date: LocalDateTime,
    var value: Double,
    open var category: T,
    var description: String,
    val id: UUID = UUID.randomUUID(),
)where T: Enum<T>, T : ICategories{
    init {
        require( value != 0.0)
    }
    override fun toString() : String {
        return "${category.displayName} $description"
    }
}
