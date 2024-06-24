package com.example.budgetapp.presentation.graphs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.budgetapp.presentation.viewModels.records.RecordsViewModel
import com.example.budgetapp.presentation.views.records.BottomBarScreen
import com.example.budgetapp.presentation.views.records.RecordsExpenses
import com.example.budgetapp.presentation.views.records.RecordsIncomes
import com.example.budgetapp.presentation.views.records.RecordsOverview
import org.koin.androidx.compose.koinViewModel

@Composable
fun RecordNavigationGraph(
    modifier: Modifier,
    navController: NavHostController,
    isFixed: Boolean = false
) {
    var isFixed = isFixed

    val recordsViewModel = koinViewModel<RecordsViewModel>()

    NavHost(
        navController = navController,
        route =  Graph.RECORDS,
        startDestination = BottomBarScreen.Overview.route
    ){
        composable(
            route = BottomBarScreen.Overview.route,
        ){
            RecordsOverview(modifier = modifier, viewModel = recordsViewModel)
        }
        composable(route = BottomBarScreen.Expenses.route){
            RecordsExpenses(modifier = modifier, isFixed = isFixed, viewModel = recordsViewModel)
            isFixed = false
        }
        composable(route = BottomBarScreen.Incomes.route){
            RecordsIncomes(modifier = modifier, isFixed = isFixed, viewModel = recordsViewModel)
            isFixed = false
        }
    }

}