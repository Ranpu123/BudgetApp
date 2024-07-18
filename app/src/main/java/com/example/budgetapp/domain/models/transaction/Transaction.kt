package com.example.budgetapp.domain.models.transaction

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.budgetapp.domain.models.ICategories
import java.time.LocalDateTime
import java.util.UUID
import kotlin.enums.EnumEntries


@Entity
open class Transaction<T> (
    var date: LocalDateTime,
    var value: Double,
    open var category: T,
    var description: String,
    @PrimaryKey
    var id: UUID = UUID.randomUUID(),
)where T: Enum<T>, T : ICategories{
    init {
        require( value != 0.0)
    }
    override fun toString() : String {
        return "${category.displayName} $description"
    }

    override fun equals(other: Any?): Boolean {
        when(other){
            is Transaction<*> -> {
                return super.equals(other) &&
                        other.id == this.id &&
                        other.category == this.category &&
                        other.date.isEqual(this.date) &&
                        other.value == this.value &&
                        other.description.equals(this.description)
            }
            else -> return false
        }
    }
}
