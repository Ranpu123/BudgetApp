package com.example.budgetapp.domain.modules

import com.example.budgetapp.domain.repository_interfaces.IExpenseRepository
import com.example.budgetapp.domain.repository_interfaces.IFixedExpenseRepository
import com.example.budgetapp.domain.repository_interfaces.IFixedIncomeRepository
import com.example.budgetapp.domain.repository_interfaces.IIncomeRepository
import com.example.budgetapp.domain.use_cases.ValidateTransactionDescription
import com.example.budgetapp.domain.use_cases.ValidateTransactionValue
import com.example.budgetapp.presentation.viewModels.transactionBottomSheet.ExpenseBottomSheetViewModel
import com.example.budgetapp.presentation.viewModels.transactionBottomSheet.FExpenseBottomSheetViewModel
import com.example.budgetapp.presentation.viewModels.transactionBottomSheet.FIncomeBottomSheetViewModel
import com.example.budgetapp.presentation.viewModels.home.HomeViewModel
import com.example.budgetapp.presentation.viewModels.records.RecordsViewModel
import com.example.budgetapp.presentation.viewModels.transactionBottomSheet.IncomeBottomSheetViewModel
import com.example.budgetapp.services.repository.expense.LocalExpenseRepository
import com.example.budgetapp.services.repository.fixed_expense.LocalFixedExpenseRepository
import com.example.budgetapp.services.repository.fixed_income.LocalFixedIncomeRepository
import com.example.budgetapp.services.repository.income.LocalIncomeRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homePageModule = module{

    single<IExpenseRepository> {LocalExpenseRepository}
    single<IFixedExpenseRepository>{LocalFixedExpenseRepository}
    single<IIncomeRepository> {LocalIncomeRepository}
    single<IFixedIncomeRepository>{LocalFixedIncomeRepository}

    viewModel<HomeViewModel> {
        HomeViewModel(get(), get(), get(), get())
    }
}

val uiStateModule = module{
    single{ ValidateTransactionValue() }
    single{ ValidateTransactionDescription() }
}

val ExpenseBottomSheetModule = module {

    single<IExpenseRepository> {LocalExpenseRepository}
    includes(uiStateModule)

    viewModel<ExpenseBottomSheetViewModel>{
        ExpenseBottomSheetViewModel(get(), get(), get())
    }
}

val IncomeBottomSheetModule = module {

    single<IIncomeRepository> {LocalIncomeRepository}
    includes(uiStateModule)

    viewModel<IncomeBottomSheetViewModel>{
        IncomeBottomSheetViewModel(get(), get(), get())
    }
}

val FixedExpenseBottomSheetModule = module {

    single<IFixedExpenseRepository> {LocalFixedExpenseRepository}
    includes(uiStateModule)

    viewModel<FExpenseBottomSheetViewModel>{
        FExpenseBottomSheetViewModel(get(), get(), get())
    }
}

val FixedIncomeBottomSheetModule = module {

    single<IFixedIncomeRepository> {LocalFixedIncomeRepository}
    includes(uiStateModule)

    viewModel<FIncomeBottomSheetViewModel>{
        FIncomeBottomSheetViewModel(get(), get(), get())
    }
}

val RecordsModule = module {

    single<IExpenseRepository> {LocalExpenseRepository}
    single<IFixedExpenseRepository>{LocalFixedExpenseRepository}
    single<IIncomeRepository> {LocalIncomeRepository}
    single<IFixedIncomeRepository>{LocalFixedIncomeRepository}

    viewModel<RecordsViewModel> {
        RecordsViewModel(get(), get(), get(), get())
    }
}