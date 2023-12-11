package uk.ac.tees.w9591610.bitesearch.viewmodels.details

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import uk.ac.tees.w9591610.bitesearch.model.response.Category

@Composable
fun MealDetailsScreen(meal: Category?) {
//    var isExpanded by remember { mutableStateOf(false) }
//    val imageSizeDp: Dp by animateDpAsState(targetValue = if (isExpanded) 200.dp else 100.dp,
//        label = ""
//    )

    // we have created enum for managing the states based on the transition
    var profilePictureState by remember { mutableStateOf(MealProfilePictureState.Normal) }
    val transition = updateTransition(targetState = profilePictureState, label = "")
    val imageSizeDp by transition.animateDp(targetValueByState = { it.size }, label = "")
    val color by transition.animateColor(targetValueByState = { it.color }, label = "")
    val widthSize by transition.animateDp(targetValueByState = { it.borderWidth }, label = "")

    Column {
        Row {
            Card (
                modifier = Modifier.padding(16.dp),
                shape = CircleShape,
                border = BorderStroke(
                    width = widthSize,
                    color = color
                )
            ){
                Image(
                    painter = rememberAsyncImagePainter(model = meal?.strCategoryThumb),
                    contentDescription = "thumb",
                    modifier = Modifier
                        .size(imageSizeDp)
                        .padding(10.dp)

                )
            }

            Text(
                text = meal?.strCategory ?: "default name",
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterVertically)
            )

        }

        Button(
            onClick = {
                profilePictureState = if(profilePictureState == MealProfilePictureState.Normal)
                    MealProfilePictureState.Expanded
                else
                    MealProfilePictureState.Normal
            }, modifier = Modifier

                .padding(10.dp)
        ) {
            Text("Change state of meal profile picture")
        }


    }

}
enum class MealProfilePictureState(val color: Color, val size: Dp, val borderWidth: Dp) {
    Normal(Color.Magenta, 120.dp, 8.dp),
    Expanded(Color.Green, 200.dp, 24.dp)
}