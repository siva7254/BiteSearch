package uk.ac.tees.w9591610.bitesearch.widgets

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.w9591610.bitesearch.model.BottomNavItem
import uk.ac.tees.w9591610.bitesearch.navigation.Routes
import uk.ac.tees.w9591610.bitesearch.screens.AddThreadScreen
import uk.ac.tees.w9591610.bitesearch.screens.HomeScreen
import uk.ac.tees.w9591610.bitesearch.screens.NotificationScreen
import uk.ac.tees.w9591610.bitesearch.screens.ProfileScreen
import uk.ac.tees.w9591610.bitesearch.screens.SearchScreen
import uk.ac.tees.w9591610.bitesearch.screens.SettingScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomNavbar(navController: NavHostController) {
    val controller = rememberNavController() //local scope controller for bottom icon routing

    Scaffold(bottomBar = { CustomBottomNavBar(controller) }) {
        NavHost(
            navController = controller,
            startDestination = Routes.Home.route,
            modifier = Modifier.padding(it)
        ) {

            composable(Routes.Home.route) {
                HomeScreen(bottomController = controller, mainController = navController)
            }
            composable(Routes.SearchThread.route) {
                SearchScreen(navController)
            }
            composable(Routes.AddThread.route) {
                AddThreadScreen(controller)
            }
            composable(Routes.Notification.route) {
                NotificationScreen()
            }
            composable(Routes.Profile.route) {
                ProfileScreen(navController)
            }
            composable(Routes.Setting.route) {
                SettingScreen(navController)
            }

        }
    }
}

@Composable
fun CustomBottomNavBar(navController: NavHostController) {

    val backStackEntry = navController.currentBackStackEntryAsState()

    val bottomNavItems = listOf(
        BottomNavItem("Home", Routes.Home.route, Icons.Rounded.Home),
       // BottomNavItem("Search", Routes.SearchThread.route, Icons.Rounded.Search),
        //BottomNavItem("Add Thread", Routes.AddThread.route, Icons.Rounded.Add),
        BottomNavItem("Notification", Routes.Notification.route, Icons.Rounded.Notifications),
        BottomNavItem("Profile", Routes.Profile.route, Icons.Rounded.Person),
        BottomNavItem("Settings", Routes.Setting.route, Icons.Rounded.Settings)
    )

    BottomAppBar(containerColor = Color.Black, contentColor = MaterialTheme.colorScheme.primary) {
        bottomNavItems.forEach {
            val selected = it.route == backStackEntry.value?.destination?.route

            NavigationBarItem(selected = selected, onClick = {
                navController.navigate(it.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            }, icon = { Icon(imageVector = it.icon, contentDescription = it.title) })

        }
    }

}