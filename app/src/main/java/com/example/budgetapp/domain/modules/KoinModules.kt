package com.example.budgetapp.domain.modules

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.example.budgetapp.BuildConfig
import com.example.budgetapp.domain.models.expense.ExpenseCategory
import com.example.budgetapp.domain.models.income.IncomeCategory
import com.example.budgetapp.domain.repository_interfaces.IExpenseRepository
import com.example.budgetapp.domain.repository_interfaces.IFixedExpenseRepository
import com.example.budgetapp.domain.repository_interfaces.IFixedIncomeRepository
import com.example.budgetapp.domain.repository_interfaces.IIncomeRepository
import com.example.budgetapp.domain.use_cases.ValidateTransactionDescription
import com.example.budgetapp.domain.use_cases.ValidateTransactionValue
import com.example.budgetapp.presentation.SyncNotificationService
import com.example.budgetapp.presentation.viewModels.transactionBottomSheet.ExpenseBottomSheetViewModel
import com.example.budgetapp.presentation.viewModels.transactionBottomSheet.FExpenseBottomSheetViewModel
import com.example.budgetapp.presentation.viewModels.transactionBottomSheet.FIncomeBottomSheetViewModel
import com.example.budgetapp.presentation.viewModels.home.HomeViewModel
import com.example.budgetapp.presentation.viewModels.records.RecordsViewModel
import com.example.budgetapp.presentation.viewModels.transactionBottomSheet.IncomeBottomSheetViewModel
import com.example.budgetapp.services.remote.adapter.ExpenseCategoryAdapter
import com.example.budgetapp.services.remote.adapter.IncomeCategoryAdapter
import com.example.budgetapp.services.remote.adapter.LocalDateAdapter
import com.example.budgetapp.services.remote.adapter.LocalDateTimeAdapter
import com.example.budgetapp.services.local.dao.expense.ExpenseDao
import com.example.budgetapp.services.local.dao.fixed_expense.FixedExpenseDao
import com.example.budgetapp.services.local.dao.fixed_income.FixedIncomeDao
import com.example.budgetapp.services.local.dao.income.IncomeDao
import com.example.budgetapp.services.local.database.AppDatabase
import com.example.budgetapp.services.remote.IBudgetAppAPI
import com.example.budgetapp.services.repository.expense.LocalExpenseRepository
import com.example.budgetapp.services.repository.expense.SyncExpenseRepository
import com.example.budgetapp.services.repository.fixed_expense.LocalFixedExpenseRepository
import com.example.budgetapp.services.repository.fixed_expense.SyncFixedExpenseRepository
import com.example.budgetapp.services.repository.fixed_income.LocalFixedIncomeRepository
import com.example.budgetapp.services.repository.fixed_income.SyncFixedIncomeRepository
import com.example.budgetapp.services.repository.income.LocalIncomeRepository
import com.example.budgetapp.services.repository.income.SyncIncomeRepository
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit


fun provideInterceptor() =
    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
fun provideOkhttpClient(interceptor: HttpLoggingInterceptor) =
    OkHttpClient.Builder().apply {
        this.addInterceptor(interceptor)
            .connectTimeout(3, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
    }.build()

fun provideBudgetAppAPI(client: OkHttpClient): IBudgetAppAPI =
    Retrofit.Builder()
        .client(client)
        .baseUrl(BuildConfig.SERVER_BASE_URL)
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

val NotificationModule = module {
    single { SyncNotificationService(androidContext()) }
}

val RemoteNetworkModule = module {
    single { provideInterceptor() }
    single { provideOkhttpClient(get()) }
    single { provideBudgetAppAPI(get()) }
    single { WorkManager.getInstance(androidContext()) }
}

val HomePageModule = module{
    viewModel<HomeViewModel> {
        HomeViewModel(get(), get(), get(), get(), get())
    }
}

val uiStateModule = module{
    single{ ValidateTransactionValue() }
    single{ ValidateTransactionDescription() }
}

val ExpenseBottomSheetModule = module {
    includes(uiStateModule)
    viewModel<ExpenseBottomSheetViewModel>{
        ExpenseBottomSheetViewModel(get(), get(),  get(), androidContext())
    }
}

val IncomeBottomSheetModule = module {
    includes(uiStateModule)
    viewModel<IncomeBottomSheetViewModel>{
        IncomeBottomSheetViewModel(get(), get(), get(), get())
    }
}

val FixedExpenseBottomSheetModule = module {
    includes(uiStateModule)
    viewModel<FExpenseBottomSheetViewModel>{
        FExpenseBottomSheetViewModel(get(), get(), get(), get())
    }
}

val FixedIncomeBottomSheetModule = module {
    includes(uiStateModule)
    viewModel<FIncomeBottomSheetViewModel>{
        FIncomeBottomSheetViewModel(get(), get(), get(), get())
    }
}

val RecordsModule = module {
    viewModel<RecordsViewModel> {
        RecordsViewModel(get(), get(), get(), get())
    }
}

val RepositoryModule = module {
    factory<IExpenseRepository> { SyncExpenseRepository(get(), get()) }
    factory<IFixedExpenseRepository>{ SyncFixedExpenseRepository(get(), get()) }
    factory<IIncomeRepository> { SyncIncomeRepository(get(), get()) }
    factory<IFixedIncomeRepository>{SyncFixedIncomeRepository(get(), get())}
}