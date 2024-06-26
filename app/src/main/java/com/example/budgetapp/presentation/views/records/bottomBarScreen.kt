package com.example.budgetapp.presentation.views.records

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.budgetapp.R

sealed class BottomBarScreen(
    val route: String,
    @StringRes val title: Int,
    @DrawableRes val icon: Int
) {
    object Overview : BottomBarScreen(
        route = "OVERVIEW",
        title = R.string.overview,
        icon = R.drawable.ic_filled_updown
    )

    object Expenses : BottomBarScreen(
        route = "EXPENSES",
        title = R.string.expenses,
        icon = R.drawable.ic_filled_uparrow
    )

    object  Incomes: BottomBarScreen(
        route = "INCOMES",
        title = R.string.incomes,
        icon = R.drawable.ic_filled_downarrow
    )
}