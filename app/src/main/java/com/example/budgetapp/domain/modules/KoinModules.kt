package com.example.budgetapp.domain.modules

import android.content.Context
import androidx.room.Room
import com.example.budgetapp.domain.models.expense.ExpenseCategory
import com.example.budgetapp.domain.models.income.IncomeCategory
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
import com.example.budgetapp.services.ExpenseCategoryAdapter
import com.example.budgetapp.services.IncomeCategoryAdapter
import com.example.budgetapp.services.LocalDateAdapter
import com.example.budgetapp.services.LocalDateTimeAdapter
import com.example.budgetapp.services.dao.expense.ExpenseDao
import com.example.budgetapp.services.dao.fixed_expense.FixedExpenseDao
import com.example.budgetapp.services.dao.fixed_income.FixedIncomeDao
import com.example.budgetapp.services.dao.income.IncomeDao
import com.example.budgetapp.services.database.AppDatabase
import com.example.budgetapp.services.remote.IBudgetAppAPI
import com.example.budgetapp.services.repository.expense.LocalExpenseRepository
import com.example.budgetapp.services.repository.expense.RemoteExpenseRepository
import com.example.budgetapp.services.repository.fixed_expense.LocalFixedExpenseRepository
import com.example.budgetapp.services.repository.fixed_income.LocalFixedIncomeRepository
import com.example.budgetapp.services.repository.income.LocalIncomeRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun provideBudgetAppAPI(): IBudgetAppAPI =
    Retrofit.Builder()
        .baseUrl("http://10.0.2.2:3000")
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
                    .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
                    .registerTypeAdapter(ExpenseCategory::class.java, ExpenseCategoryAdapter().nullSafe())
                    .registerTypeAdapter(IncomeCategory::class.java, IncomeCategoryAdapter().nullSafe())
                    .create()
            )
        )
        .build()
        .create(IBudgetAppAPI::class.java)

fun provideDatabase(context: Context): AppDatabase =
    Room.databaseBuilder(context, AppDatabase::class.java,"appDatabase")
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

fun provideIncomeDao(db: AppDatabase): IncomeDao = db.incomeDao()
fun provideExpenseDao(db: AppDatabase): ExpenseDao = db.expenseDao()
fun provideFixedExpenseDao(db: AppDatabase): FixedExpenseDao = db.fixedExpenseDao()
fun provideFixedIncomeDao(db: AppDatabase): FixedIncomeDao = db.fixedIncomeDao()

val DatabaseModule = module {
    single { provideDatabase(androidContext()) }
    single { provideIncomeDao(get()) }
    single { provideExpenseDao(get()) }
    single { provideFixedExpenseDao(get()) }
    single { provideFixedIncomeDao(get()) }
}

val RemoteNetworkModule = module {
    single { provideBudgetAppAPI() }
}

val HomePageModule = module{
    viewModel<HomeViewModel> {
        HomeViewModel(get(), get(), get(), get())
    }
}

val uiStateModule = module{
    single{ ValidateTransactionValue() }
    single{ ValidateTransactionDescription() }
}

val ExpenseBottomSheetModule = module {
    includes(uiStateModule)
    viewModel<ExpenseBottomSheetViewModel>{
        ExpenseBottomSheetViewModel(get(), get(), get())
    }
}

val IncomeBottomSheetModule = module {
    includes(uiStateModule)
    viewModel<IncomeBottomSheetViewModel>{
        IncomeBottomSheetViewModel(get(), get(), get())
    }
}

val FixedExpenseBottomSheetModule = module {
    includes(uiStateModule)
    viewModel<FExpenseBottomSheetViewModel>{
        FExpenseBottomSheetViewModel(get(), get(), get())
    }
}

val FixedIncomeBottomSheetModule = module {
    includes(uiStateModule)
    viewModel<FIncomeBottomSheetViewModel>{
        FIncomeBottomSheetViewModel(get(), get(), get())
    }
}

val RecordsModule = module {
    viewModel<RecordsViewModel> {
        RecordsViewModel(get(), get(), get(), get())
    }
}

val RepositoryModule = module {
    factory<IExpenseRepository> { LocalExpenseRepository(get()) }
    factory<IFixedExpenseRepository>{ LocalFixedExpenseRepository(get()) }
    factory<IIncomeRepository> { LocalIncomeRepository(get()) }
    factory<IFixedIncomeRepository>{LocalFixedIncomeRepository(get())}
}