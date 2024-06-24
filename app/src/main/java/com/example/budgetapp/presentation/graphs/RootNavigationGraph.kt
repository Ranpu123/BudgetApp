package com.example.budgetapp.presentation.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.budgetapp.presentation.viewModels.home.HomeViewModel
import com.example.budgetapp.presentation.views.HomeView
import com.example.budgetapp.presentation.views.records.BottomBarScreen
import com.example.budgetapp.presentation.views.records.RecordsView
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinContext


@Composable
fun RootNavigationGraph(navController: NavHostController){
    KoinContext {
        NavHost(
            navController = navController,
            route = Graph.ROOT,
            startDestination = Graph.MAIN
        ) {
            composable(route = Graph.MAIN) {
                HomeView(
                    navController = navController,
                    viewModel = koinViewModel<HomeViewModel>()
                )
            }
            composable(
                route = Graph.RECORDS + "?page={page}&fixed={fixed}",
                arguments = listOf(
                    navArgument("page"){
                        type = NavType.StringType
                        defaultValue = BottomBarScreen.Overview.route
                        nullable = true
                    },
                    navArgument("fixed"){
                        type = NavType.BoolType
                        defaultValue = false
                    },
                )
            ) {entry->
                var page = entry.arguments?.getString("page") ?: BottomBarScreen.Overview.route
                var isFixed = entry.arguments?.getBoolean("fixed") ?: false
                RecordsView(page = page, isFixed = isFixed)
            }
        }
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val MAIN = "home_graph"
    const val RECORDS = "records_graph"
}