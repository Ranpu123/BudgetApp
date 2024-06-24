@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.budgetapp.presentation.views

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.compose.rememberNavController

import com.example.budgetapp.domain.modules.ExpenseBottomSheetModule
import com.example.budgetapp.domain.modules.FixedExpenseBottomSheetModule
import com.example.budgetapp.domain.modules.FixedIncomeBottomSheetModule
import com.example.budgetapp.domain.modules.IncomeBottomSheetModule
import com.example.budgetapp.domain.modules.RecordsModule
import com.example.budgetapp.domain.modules.homePageModule
import com.example.budgetapp.presentation.graphs.RootNavigationGraph
import com.example.budgetapp.presentation.ui.theme.BudgetAppTheme
import com.example.budgetapp.presentation.viewModels.home.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.ext.android.viewModel

import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin


class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startModules()
        enableEdgeToEdge()

        setContent {
            BudgetAppTheme {

                RootNavigationGraph(navController = rememberNavController())

            }
        }
    }

    fun startModules(){
        stopKoin()
        startKoin(){
            androidLogger()
            androidContext(this@MainActivity)
            modules(
                homePageModule,
                ExpenseBottomSheetModule,
                IncomeBottomSheetModule,
                FixedExpenseBottomSheetModule,
                FixedIncomeBottomSheetModule,
                RecordsModule
            )
        }
    }
}





