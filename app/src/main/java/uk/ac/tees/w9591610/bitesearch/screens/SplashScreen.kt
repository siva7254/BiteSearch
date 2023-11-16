package uk.ac.tees.w9591610.bitesearch.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.navigation.NavHostController
import uk.ac.tees.w9591610.bitesearch.R
import uk.ac.tees.w9591610.bitesearch.navigation.Routes
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SplashScreen(controller: NavHostController) {

    val constraints = ConstraintSet {
        val imageRef = createRefFor("image")
        val text = createRefFor("text")
        val topGuidLine = createGuidelineFromTop(0.1f)
        val bottomGuidLine = createGuidelineFromBottom(0.1f)

        constrain(imageRef) {
            top.linkTo(topGuidLine)
            bottom.linkTo(bottomGuidLine)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        constrain(text) {
            top.linkTo(imageRef.bottom)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

    }

    ConstraintLayout(constraints, modifier = Modifier.fillMaxSize()) {
        val (image) = createRefs()
        Image(
            painter = painterResource(id = R.drawable.baseline_fastfood_24),
            contentDescription = "app_logo",
            modifier = Modifier
                .height(100.dp)
                .width(100.dp)
                .layoutId("image"),
        )

        Text(
            text = "Developed by Siva",
            Modifier
                .layoutId("text")
                .fillMaxWidth(),
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Normal,
                fontSize = 20.sp
            )
        )

    }

    LaunchedEffect(true) {
        delay(3000)
        FirebaseAuth.getInstance().signOut()
        val route = if (FirebaseAuth.getInstance().currentUser != null) {
            Routes.BottomNav.route
        } else {
            Routes.Login.route
        }

        controller.navigate(route) {
            popUpTo(controller.graph.startDestinationId) {
                inclusive = true
            }
            launchSingleTop=true
        }
    }

}