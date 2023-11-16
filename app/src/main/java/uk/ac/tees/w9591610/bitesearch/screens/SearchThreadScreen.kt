package uk.ac.tees.w9591610.bitesearch.screens


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import uk.ac.tees.w9591610.bitesearch.R
import uk.ac.tees.w9591610.bitesearch.model.UserModel
import uk.ac.tees.w9591610.bitesearch.navigation.Routes
import uk.ac.tees.w9591610.bitesearch.viewmodels.AddThreadViewModel

@Composable
fun SearchScreen(controller: NavHostController) {

    val searchViewModel = AddThreadViewModel(isFromSearch = true)

    val userList by searchViewModel.userList.observeAsState(null)

    val isLoading by searchViewModel.showLoader.observeAsState(false)

    var search by remember { mutableStateOf("") }

    Column {
        OutlinedTextField(
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "") },
            value = search,
            onValueChange = {
                search = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .then(Modifier.padding(5.dp)),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = {
                Text(
                    text = "Search"
                )
            })

        if (userList != null && userList!!.isNotEmpty()) {

            val filterList = userList?.filter { it.username.contains(search, ignoreCase = false) }.orEmpty()
            if(filterList.isNotEmpty()){
            LazyColumn() {
                items(filterList) { user ->
                    UserListItem(user = user, controller)
                }
            }
            }

        }
    }

}

@Composable
fun UserListItem(user: UserModel, controller: NavHostController) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable {
                val route = Routes.UserProfile.route.replace("{data}", user.uid!!)
                controller.navigate(route)
            }
    ) {

        val (postCard, divider, username, verifiedIcon, bio) = createRefs()

        Image(painter = rememberAsyncImagePainter(model = user.imageUrl),
            contentDescription = "user_image",
            modifier = Modifier
                .width(60.dp)
                .height(60.dp)
                .then(Modifier.padding(5.dp))
                .clip(CircleShape)
                .constrainAs(postCard) {
                    start.linkTo(parent.start)
                    top.linkTo(divider.bottom)
                }
        )

        Text(text = user.username, style = TextStyle(
            fontSize = 13.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium
        ), modifier = Modifier
            .padding(start = 10.dp, top = 20.dp)
            .constrainAs(username) {
                start.linkTo(postCard.end)
                top.linkTo(postCard.top)
            })

        Text(text = user.bio, style = TextStyle(
            fontSize = 13.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium
        ), modifier = Modifier
            .padding(start = 10.dp, top = 5.dp)
            .constrainAs(bio) {
                start.linkTo(username.start)
                top.linkTo(username.bottom)
            })

        Image(painter = painterResource(id = R.drawable.ic_verified_icon),
            contentDescription = "verified",
            modifier = Modifier
                .height(15.dp)
                .width(15.dp)
                .constrainAs(verifiedIcon) {
                    start.linkTo(username.end)
                    bottom.linkTo(username.bottom)
                })

    }
}