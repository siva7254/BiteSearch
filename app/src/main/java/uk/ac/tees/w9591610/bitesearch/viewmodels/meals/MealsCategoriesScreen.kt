package uk.ac.tees.w9591610.bitesearch.ui.meals

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberAsyncImagePainter
import uk.ac.tees.w9591610.bitesearch.model.response.Category
import uk.ac.tees.w9591610.bitesearch.viewmodels.details.MealDetailsScreen
import uk.ac.tees.w9591610.bitesearch.ui.details.MealDetailsViewModel

@Composable
fun MealsCategoriesScreen(navigationCallback: (String) -> Unit) {

    val viewModel: MealCategoriesViewModel = viewModel()
    val meals = viewModel.mealsState.value

    LazyColumn {
        items(meals) { meal ->
            MealCategory(meal, navigationCallback)
        }
    }
}

@Composable
fun FoodzApp() {

    val navigationController = rememberNavController()

    NavHost(navController = navigationController, startDestination = "dest_list") {

        composable(route = "dest_list") {
            MealsCategoriesScreen { navigationMealID ->
                navigationController.navigate(route = "dest_detail/$navigationMealID")
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
    }


}

@Composable
fun MealCategory(meal: Category, navigationCallback: (String) -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                navigationCallback(meal.idCategory)
            }
    ) {

        Row(
            modifier = Modifier
                .padding(12.dp)
                .animateContentSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Image(
                painter = rememberAsyncImagePainter(model = meal.strCategoryThumb),
                contentDescription = "thumb",
                modifier = Modifier
                    .size(80.dp)
                    .padding(10.dp)
                    .align(Alignment.CenterVertically),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(0.8f)
            ) {
                Text(
                    text = meal.strCategory,
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.headlineMedium
                )


                Text(
                    text = meal.strCategoryDescription, fontSize = 14.sp,
                    modifier = Modifier.alpha(0.5f),
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = if (isExpanded) 5 else 3,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp
                else Icons.Filled.KeyboardArrowDown,
                contentDescription = "icon",
                modifier = Modifier
                    .padding(10.dp)
                    .align(if (isExpanded) Alignment.Bottom else Alignment.CenterVertically)
                    .clickable {
                        isExpanded = !isExpanded
                    }

            )


        }
    }

}