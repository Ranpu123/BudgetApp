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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.toLowerCase
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.budgetapp.R
import com.example.budgetapp.presentation.graphs.RecordNavigationGraph
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RecordsView(
    parentNav: NavHostController,
    page: String = BottomBarScreen.Overview.route,
    isFixed: Boolean = false
){
    val navController: NavHostController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) {
        RecordNavigationGraph(
            modifier = Modifier.padding(bottom = it.calculateBottomPadding()),
            parentNav = parentNav,
            navController = navController,
            fixed = isFixed
        )
        when(page){
            BottomBarScreen.Incomes.route -> navController.navigate(BottomBarScreen.Incomes.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
            BottomBarScreen.Expenses.route -> navController.navigate(BottomBarScreen.Expenses.route){
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
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

    val context = LocalContext.current

    BottomAppBar(containerColor = Color(0xFF00BD40)) {
        screens.forEach{ screen ->

            val isSelected = currentDestination
                ?.hierarchy
                ?.any {it.route == screen.route } == true

            NavigationBarItem(
                label = {Text(
                        text = context.getString(screen.title).lowercase(Locale.getDefault()),
                        color = if(isSelected) Color.White else Color(0XFF014D19)
                )},
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
                        contentDescription = null
                    )
                }
            )
        }
    }
}
