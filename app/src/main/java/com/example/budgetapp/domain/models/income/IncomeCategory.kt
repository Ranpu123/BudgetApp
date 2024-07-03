package com.example.budgetapp.domain.models.income

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.budgetapp.App
import com.example.budgetapp.R
import com.example.budgetapp.domain.models.ICategories

enum class IncomeCategory(@StringRes override val displayName: Int): ICategories {
    COMMISSION(R.string.commission),
    GIFT(R.string.gift),
    INTEREST(R.string.interest),
    INVESTMENT(R.string.investment),
    SALARY(R.string.salary),
    SELL(R.string.sell),
    TIPS(R.string.tips),
    OTHER(R.string.other);

    override fun asString(): String{
        return App.mContext.getString(this.displayName)
    }
}