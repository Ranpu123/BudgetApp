package com.example.budgetapp.presentation.views.records

import androidx.annotation.DrawableRes
import com.example.budgetapp.R

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    @DrawableRes val icon: Int
) {
    object Overview : BottomBarScreen(
        route = "OVERVIEW",
        title = "visão geral",
        icon = R.drawable.ic_filled_updown
    )

    object Expenses : BottomBarScreen(
        route = "EXPENSES",
        title = "gastos",
        icon = R.drawable.ic_filled_uparrow
    )

    object  Incomes: BottomBarScreen(
        route = "INCOMES",
        title = "receitas",
        icon = R.drawable.ic_filled_downarrow
    )
}