package uk.ac.tees.w9591610.bitesearch.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import uk.ac.tees.w9591610.bitesearch.screens.AddThreadScreen
import uk.ac.tees.w9591610.bitesearch.screens.EditProfileBioLinkScreen
import uk.ac.tees.w9591610.bitesearch.screens.EditProfileScreen
import uk.ac.tees.w9591610.bitesearch.screens.HomeScreen
import uk.ac.tees.w9591610.bitesearch.screens.LoginScreen
import uk.ac.tees.w9591610.bitesearch.screens.NotificationScreen
import uk.ac.tees.w9591610.bitesearch.screens.PrivacyScreen
import uk.ac.tees.w9591610.bitesearch.screens.ProfileScreen
import uk.ac.tees.w9591610.bitesearch.screens.RegisterScreen
import uk.ac.tees.w9591610.bitesearch.screens.SearchScreen
import uk.ac.tees.w9591610.bitesearch.screens.SettingScreen
import uk.ac.tees.w9591610.bitesearch.screens.SplashScreen
import uk.ac.tees.w9591610.bitesearch.screens.UserProfileScreen
import uk.ac.tees.w9591610.bitesearch.ui.details.MealDetailsViewModel
import uk.ac.tees.w9591610.bitesearch.ui.meals.MealsCategoriesScreen
import uk.ac.tees.w9591610.bitesearch.viewmodels.details.MealDetailsScreen
import uk.ac.tees.w9591610.bitesearch.widgets.BottomNavbar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(navHostController: NavHostController) {

    NavHost(navController = navHostController, startDestination = Routes.Splash.route) {
        composable(Routes.Splash.route) {
            SplashScreen(navHostController)
        }

        composable(Routes.Home.route) {
            MealsCategoriesScreen { navigationMealID ->
                navHostController.navigate(route = "dest_detail/$navigationMealID")
            }
        }

        composable(Routes.DestList.route) {
            MealsCategoriesScreen { navigationMealID ->
                navHostController.navigate(route = "dest_detail/$navigationMealID")
            }
        }

        composable(
            route = "dest_detail/{meal_category_id}",
            arguments = listOf(navArgument(name = "meal_category_id") {
                type = NavType.StringType
            })
        ) {
            val viewModel: MealDetailsViewModel = viewModel()
            MealDetailsScreen(viewModel.mealState.value)
        }
        composable(Routes.Notification.route) {
            NotificationScreen()
        }
        composable(Routes.AddThread.route) {
            AddThreadScreen(navHostController)
        }
        composable(Routes.SearchThread.route) {
            SearchScreen(navHostController)
        }
        composable(Routes.Profile.route) {
            ProfileScreen(navHostController)
        }
        composable(Routes.EditProfile.route) {
            EditProfileScreen(navHostController)
        }

        composable(Routes.EditBioLinkProfile.route) {
            val isEdit = it.arguments?.getString("data")
            val value=it.arguments?.getString("value")
            EditProfileBioLinkScreen(navHostController, isEdit == "true", value = value!!)
        }

        composable(Routes.BottomNav.route) {
            BottomNavbar(navHostController)
        }

        composable(Routes.Login.route) {
            LoginScreen(navHostController = navHostController)
        }

        composable(Routes.Register.route) {
            RegisterScreen(navHostController = navHostController)
        }

        composable(Routes.Privacy.route) {
            PrivacyScreen(controller = navHostController)
        }

        composable(Routes.Setting.route) {
            SettingScreen(controller = navHostController)
        }

        composable(Routes.UserProfile.route) {
            val uid = it.arguments?.getString("data");
            UserProfileScreen(controller = navHostController, uid = uid!!)
        }

    }

}