package com.example.budgetapp.presentation.views.records

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.budgetapp.presentation.graphs.RecordNavigationGraph

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RecordsView(
    navController: NavHostController = rememberNavController(),
    page: String = BottomBarScreen.Overview.route,
    isFixed: Boolean = false
){
    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) {
        RecordNavigationGraph(
            modifier = Modifier.padding(bottom = it.calculateBottomPadding()),
            navController = navController,
            isFixed = isFixed
        )
        when(page){
            BottomBarScreen.Incomes.route -> navController.navigate(BottomBarScreen.Incomes.route)
            BottomBarScreen.Expenses.route -> navController.navigate(BottomBarScreen.Expenses.route)
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {

    var screens = listOf(
        BottomBarScreen.Incomes,
                                            BottomBarScreen.Overview,
                                            BottomBarScreen.Expenses)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination



    BottomAppBar(containerColor = Color(0xFF00BD40)) {
        screens.forEach{ screen ->

            val isSelected = currentDestination
                ?.hierarchy
                ?.any {it.route == screen.route } == true

            NavigationBarItem(
                label = { Text(text = screen.title, color = if(isSelected) Color.White else Color(0XFF014D19)) },
                selected = isSelected,
                colors = NavigationBarItemDefaults.colors(
                    unselectedIconColor = Color(0XFF014D19),
                    selectedIconColor = Color.White,
                    indicatorColor = Color(0X1C009A33)
                ),
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(screen.icon),
                        contentDescription = "Navigation Icon"
                    )
                }
            )
        }
    }
}
