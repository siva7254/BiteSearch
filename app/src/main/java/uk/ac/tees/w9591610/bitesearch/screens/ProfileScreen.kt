package uk.ac.tees.w9591610.bitesearch.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import uk.ac.tees.w9591610.bitesearch.R
import uk.ac.tees.w9591610.bitesearch.model.SuggestionModel
import uk.ac.tees.w9591610.bitesearch.navigation.Routes
import uk.ac.tees.w9591610.bitesearch.viewmodels.AddThreadViewModel
import uk.ac.tees.w9591610.bitesearch.viewmodels.AuthViewModel
import uk.ac.tees.w9591610.bitesearch.widgets.CustomBottomThreadInfoSheet
import uk.ac.tees.w9591610.bitesearch.widgets.ReusableProfileScreen
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileScreen(controller: NavHostController) {
    val authViewModel = remember { AuthViewModel() }
    val addThreadViewModel = AddThreadViewModel(false, FirebaseAuth.getInstance().currentUser?.uid)
    val threads by addThreadViewModel.postDataV2.observeAsState(null)

    val profileData by authViewModel.profileData.observeAsState()

    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Expanded)

    var modalSheetVisible by remember { mutableStateOf(false) }

    LaunchedEffect(modalSheetVisible) {
        if (bottomSheetState.isVisible) {
            bottomSheetState.hide()
        } else {
            bottomSheetState.show()
        }
    }

    val suggestionPeopleList = arrayListOf(
        SuggestionModel(
            title = "Virat Kohli",
            image = "https://i.pinimg.com/736x/fb/e5/17/fbe517a62e9bcafdddc6fbbde48b854f.jpg"
        ),
        SuggestionModel(
            title = "Start Sports",
            image = "https://www.exchange4media.com/news-photo/122156-starmain.jpg"
        ),
        SuggestionModel(
            title = "ICC",
            image = "https://yt3.googleusercontent.com/3K6h6gpMPf4mK9qh6SXTl0W3PLxnOMzUnFHc2lbS9t-ucS-b4JGcR8nW7ja9XDYkHM-kAnijk2c=s900-c-k-c0x00ffffff-no-rj"
        ),
        SuggestionModel(
            title = "Team India",
            image = "https://upload.wikimedia.org/wikipedia/en/thumb/8/8d/Cricket_India_Crest.svg/1200px-Cricket_India_Crest.svg.png"
        ),
    )

    authViewModel.getProfileData(FirebaseAuth.getInstance().currentUser!!.uid)

    val scrollState = rememberLazyListState(suggestionPeopleList.size)

    val itemsState = rememberSaveable { mutableStateOf(suggestionPeopleList) }

    if (profileData != null) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = {
                    controller.navigate(Routes.Privacy.route)
                }) {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = "privacy_icon")
                }
                IconButton(onClick = {
                    controller.navigate(Routes.Setting.route)
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_menu_icon),
                        contentDescription = "setting_icon"
                    )
                }
            }
            ReusableProfileScreen(userProfileData = profileData!!, threadCallBack = {
                modalSheetVisible = !modalSheetVisible;
            }, callback1 = {
                controller.navigate(Routes.EditProfile.route)
            }, callback2 = {

            })

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Suggested for you", style = TextStyle(
                    fontSize = 15.sp, color = Color.Black, fontWeight = FontWeight(500)
                ), modifier = Modifier.then(Modifier.padding(horizontal = 15.dp))
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyRow(
                state = scrollState,
                modifier = Modifier
                    .fillMaxWidth()
                    .then(Modifier.padding(horizontal = 10.dp))
            ) {
                items(itemsState.value) {
                    SuggestionCard(title = it.title, imageUrl = it.image)
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

        }

        ModalBottomSheetLayout(
            sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp), sheetContent = {
                CustomBottomThreadInfoSheet(username = profileData!!.username)
            }, sheetState = bottomSheetState
        ) {

        }
    }

}

@Composable
fun SuggestionCard(title: String, imageUrl: String) {
    OutlinedCard(
        colors = CardColors(
            contentColor = Color.Black,
            containerColor = Color.LightGray,
            disabledContainerColor = Color.Yellow,
            disabledContentColor = Color.LightGray
        ), modifier = Modifier
            .width(160.dp)
            .then(Modifier.padding(10.dp))
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (image, crossIcon, suggestionCardTitle, blueTick, username, followButton) = createRefs()
            Image(painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = "",
                Modifier
                    .size(70.dp)
                    .constrainAs(image) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top, margin = 20.dp)
                    }
                    .clip(RoundedCornerShape(percent = 50)))
            Image(painter = painterResource(id = R.drawable.ic_close_icon),
                contentDescription = "close icon",
                modifier = Modifier.constrainAs(crossIcon) {
                    end.linkTo(parent.end, margin = 5.dp)
                    top.linkTo(parent.top, margin = 5.dp)
                })

            Image(painter = painterResource(id = R.drawable.ic_blue_tick_icon),
                contentDescription = "blue tick icon",
                modifier = Modifier.constrainAs(blueTick) {
                    start.linkTo(image.start)
                    bottom.linkTo(image.bottom)
                })

            Text(text = title,
                style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.constrainAs(suggestionCardTitle) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(image.bottom, margin = 5.dp)
                })

            Text(
                text = title.trim().lowercase(),
                style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.constrainAs(username) {
                    start.linkTo(suggestionCardTitle.start)
                    end.linkTo(suggestionCardTitle.end)
                    top.linkTo(suggestionCardTitle.bottom, margin = 5.dp)
                },
                textAlign = TextAlign.Center

            )

            OutlinedButton(colors = ButtonColors(
                containerColor = Color.Black,
                contentColor = Color.White,
                disabledContentColor = Color.LightGray,
                disabledContainerColor = Color.White
            ), onClick = {

            }, modifier = Modifier.constrainAs(followButton) {
                start.linkTo(username.start)
                end.linkTo(username.end)
                top.linkTo(username.bottom, margin = 10.dp)
                bottom.linkTo(parent.bottom, margin = 10.dp)
            }) {
                Text(
                    text = "Follow",
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }

        }
    }
}
