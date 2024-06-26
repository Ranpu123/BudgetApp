package com.example.budgetapp.domain.models.expense

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.budgetapp.App
import com.example.budgetapp.R
import com.example.budgetapp.domain.models.ICategories

enum class ExpenseCategory(@StringRes override val displayName: Int): ICategories {
    FOOD(R.string.food),
    GROCERY(R.string.grocery),
    HEALTH(R.string.health),
    LIGHTING(R.string.lighting),
    RENT(R.string.rent),
    RESTAURANT(R.string.restaurant),
    TAX(R.string.tax),
    WATER(R.string.water),
    GAS(R.string.gas),
    FUEL(R.string.fuel),
    STREAMING(R.string.streaming),
    INTERNET(R.string.internet),
    PHONE_PLAN(R.string.phone_plan),
    INSURANCE(R.string.insurance),
    INSTALLMENT(R.string.installment),
    OTHER(R.string.other);

    override fun asString(): String{
        return App.mContext.getString(this.displayName)
    }
}