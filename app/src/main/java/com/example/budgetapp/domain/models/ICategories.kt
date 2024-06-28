package com.example.budgetapp.domain.models

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable

interface ICategories {
    @get:StringRes
    val displayName: Int

    fun asString(): String
}
