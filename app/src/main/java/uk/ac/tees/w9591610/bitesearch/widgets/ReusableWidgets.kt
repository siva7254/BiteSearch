package uk.ac.tees.w9591610.bitesearch.widgets

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import uk.ac.tees.w9591610.bitesearch.R
import uk.ac.tees.w9591610.bitesearch.model.ThreadPostModel
import uk.ac.tees.w9591610.bitesearch.model.UserModel
import uk.ac.tees.w9591610.bitesearch.navigation.Routes
import uk.ac.tees.w9591610.bitesearch.util.Util
import com.google.firebase.auth.FirebaseAuth

@Composable
fun CustomTextFiled(
    textFiled: TextFieldValue,
    callBack: (TextFieldValue) -> Unit,
    label: String,
    modifier: Modifier,
    isDoneOption: Boolean = false,
    focusRequester: FocusRequester? = null
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(value = textFiled,
        onValueChange = callBack,
        label = { Text(text = label) },
        modifier = modifier,
        textStyle = TextStyle(color = Color.White),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = if (isDoneOption) ImeAction.Done else ImeAction.Next),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
            },

            onNext = {
                if (isDoneOption) {
                    focusRequester?.requestFocus()
                }
            }
        ))

}

@Composable
fun CustomButton(onTap: () -> Unit, title: String, modifier: Modifier) {

    Button(
        onClick = onTap,
        modifier = modifier,
        contentPadding = PaddingValues(15.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(text = title, style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
    }
}

@Composable
fun AuthBottomLabel(title: String, subTitle: String, modifier: Modifier) {
    Row {
        Text(
            text = title,
            style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Normal, color = Color.White)
        )
        BasicText(
            text = subTitle,
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White),
            modifier = modifier
        )
    }
}

@Composable
fun ProfileButton(title: String, onTap: () -> Unit, modifier: Modifier) {
    OutlinedButton(
        shape = RoundedCornerShape(5.dp),
        onClick = onTap,
        modifier = modifier.height(35.dp)
    ) {
        Text(
            text = title,
            style = TextStyle(textAlign = TextAlign.Center, color = Color.Black),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun PickImage(modifier: Modifier, imageList: List<Uri>) {
    LazyVerticalGrid(
        horizontalArrangement = Arrangement.SpaceBetween,
        columns = GridCells.Fixed(3),
        modifier = modifier
    ) {
        items(imageList.size) {
            Image(
                painter = rememberAsyncImagePainter(model = imageList[it]),
                contentDescription = "",
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
fun ReusableProfileScreen(
    userProfileData: UserModel,
    threadCallBack: () -> Unit,
    callback1: () -> Unit,
    callback2: () -> Unit,
) {

    val button1Title: String
    val button2Title: String

    if (FirebaseAuth.getInstance().currentUser!!.uid == userProfileData.uid) {
        button1Title = "Edit Profile"
        button2Title = "Share Profile"
    } else {
        button1Title = "Follow"
        button2Title = "Mention"
    }

    ConstraintLayout(
        modifier = Modifier.fillMaxWidth(),
    ) {
        val (profileCard, bio, profileButtons) = createRefs()

        Box(
            modifier = Modifier
                .constrainAs(profileCard) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
                .padding(start = 15.dp, end = 15.dp, top = 20.dp)
        ) {
            ConstraintLayout(
                modifier = Modifier.fillMaxWidth(),
            ) {
                val (name, username, threadCard, userImage, verifiedIcon) = createRefs()

                Text(
                    text = userProfileData.name ?: "",
                    style = TextStyle(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = FontFamily.Serif
                    ),
                    modifier = Modifier.constrainAs(name) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    }
                )

                Text(
                    text = userProfileData.username ?: "",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                    ),
                    modifier = Modifier
                        .constrainAs(username) {
                            start.linkTo(parent.start)
                            top.linkTo(name.bottom)
                            top.linkTo(name.bottom)
                            top.linkTo(name.bottom)
                            top.linkTo(name.bottom)
                        }
                        .padding(top = 10.dp)
                )

                Card(
                    modifier = Modifier
                        .constrainAs(threadCard) {
                            start.linkTo(username.end)
                            top.linkTo(name.bottom)
                        }
                        .height(25.dp)
                        .width(90.dp)
                        .padding(top = 10.dp, start = 10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(
                        text = "threads.net",
                        style = TextStyle(textAlign = TextAlign.Center),
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                threadCallBack()
                            }
                    )
                }

                if (userProfileData.imageUrl != null) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = userProfileData.imageUrl
                        ),
                        contentDescription = "user_image",
                        modifier = Modifier
                            .constrainAs(userImage) {
                                top.linkTo(parent.top)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                            }
                            .height(80.dp)
                            .width(80.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_people_icon),
                        contentDescription = "user_image",
                        modifier = Modifier
                            .constrainAs(userImage) {
                                top.linkTo(parent.top)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                            }
                            .height(80.dp)
                            .width(80.dp)
                            .clip(CircleShape)
                    )
                }



                Image(
                    painter = painterResource(id = R.drawable.ic_verified_icon),
                    contentDescription = "verified",
                    modifier = Modifier
                        .constrainAs(verifiedIcon) {
                            start.linkTo(userImage.start)
                            bottom.linkTo(userImage.bottom)
                        }
                        .height(20.dp)
                        .width(20.dp)
                        .clip(CircleShape)
                )
            }
        }

        Text(
            text = userProfileData.bio ?: "",
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
            ),
            modifier = Modifier
                .constrainAs(bio) {
                    start.linkTo(parent.start)
                    top.linkTo(profileCard.bottom)
                }
                .padding(top = 10.dp, start = 15.dp)
                .width(200.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .constrainAs(profileButtons) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(bio.bottom)
                }
                .padding(start = 15.dp, top = 20.dp, end = 10.dp)
        ) {
            ProfileButton(
                title = button1Title, onTap = {
                    callback1()
                }, modifier = Modifier
                    .weight(0.5f, false)
                    .padding(end = 10.dp)
            )
            ProfileButton(
                title = button2Title, onTap = {
                    callback2()
                }, modifier = Modifier
                    .weight(0.5f, false)
                    .padding(start = 10.dp)
            )
        }


    }
}


@Composable
fun CustomBottomThreadInfoSheet(username: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(Modifier.padding(vertical = 20.dp, horizontal = 10.dp))
    ) {
        Box(
            modifier = Modifier
                .height(4.dp)
                .width(30.dp)
                .background(color = Color.Gray)
                .align(alignment = Alignment.CenterHorizontally)
                .clip(
                    RoundedCornerShape(100.dp)
                )
        ) {
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "threads.net",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
            Image(
                painter = painterResource(id = R.drawable.ic_thread_icon),
                contentDescription = "thread_icon",
                modifier = Modifier
                    .size(50.dp)
                    .clip(
                        CircleShape
                    ),
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Soon, you'll be able to follow and interact with people on other fediverse platforms, like Mastodon. They can also find you with full username @${username}@threads.net",
            style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReusableThreadPostCard(
    postData: List<Pair<UserModel, ThreadPostModel>>?,
    mainController: NavHostController? = null,
    bottomController: NavHostController? = null,
    likeCallback: () -> Unit
) {

    val placeholder = painterResource(
        id = R.drawable.ic_twitter_icon
    )

    LazyColumn() {
        postData?.let {
            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
            item() {
                Image(
                    painter = painterResource(id = R.drawable.ic_thread_icon),
                    contentDescription = "app_icon",
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth(),
                    alignment = Alignment.Center
                )
            }
            items(it.size) { index ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(Modifier.padding(10.dp)),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(Modifier.padding(10.dp)),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = postData!![index].first.imageUrl,
                                    placeholder = placeholder
                                ),
                                contentDescription = "",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .height(40.dp)
                                    .width(40.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        if (postData!![index].first.uid != FirebaseAuth.getInstance().currentUser?.uid) {
                                            val route = Routes.UserProfile.route.replace(
                                                "{data}",
                                                postData!![index].first.uid!!
                                            )
                                            mainController!!.navigate(route)
                                        } else {
                                            bottomController!!.navigate(Routes.Profile.route)
                                        }
                                    }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = postData!![index].first.username,
                                style = TextStyle(
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                ), modifier = Modifier.clickable {
                                    if (postData!![index].first.uid != FirebaseAuth.getInstance().currentUser?.uid) {
                                        val route = Routes.UserProfile.route.replace(
                                            "{data}",
                                            postData!![index].first.uid!!
                                        )
                                        mainController!!.navigate(route)
                                    } else {
                                        bottomController!!.navigate(Routes.Profile.route)
                                    }
                                }
                            )
                            Image(
                                painter = painterResource(id = R.drawable.ic_verified_icon),
                                contentDescription = "verified",
                                modifier = Modifier
                                    .height(15.dp)
                                    .width(15.dp)
                            )
                        }


                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.weight(0.5f)
                        ) {
                            Text(
                                text = Util.formatDateString(postData!![index].second.createdAt),
                                style = TextStyle(
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Image(
                                painter = painterResource(id = R.drawable.ic_more_icon),
                                contentDescription = "more_icon"
                            )
                        }

                    }
                    Text(
                        text = postData!![index].second.description,
                        modifier = Modifier.then(Modifier.padding(10.dp)),
                        style = TextStyle(fontSize = 15.sp, color = Color.Black),
                        letterSpacing = 1.sp,
                        lineHeight = 22.sp
                    )
                    if (postData!![index].second.image.size == 1) {
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .then(Modifier.padding(horizontal = 10.dp, vertical = 10.dp))
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = postData!![index].second.image[0],
                                    placeholder = placeholder
                                ),
                                contentDescription = "post_image",
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    } else {
                        LazyRow() {
                            items(postData!![index].second.image) {
                                Card(
                                    modifier = Modifier
                                        .width(300.dp)
                                        .height(300.dp)
                                        .then(
                                            Modifier.padding(
                                                horizontal = 10.dp,
                                                vertical = 10.dp
                                            )
                                        ),
                                    shape = RoundedCornerShape(10.dp),
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(
                                            model = it,
                                            placeholder = placeholder
                                        ),
                                        contentDescription = "post_image",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .height(180.dp),
                                        contentScale = ContentScale.FillWidth,
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    Row(modifier = Modifier.then(Modifier.padding(start = 10.dp))) {
                        Log.e("TAG", "HomeScreen: ${postData!![index].second.likes.size}")
                        Image(
                            painter = painterResource(id = R.drawable.ic_heart_icon),
                            contentDescription = "like_icon",
                            modifier = Modifier
                                .size(25.dp)
                                .clickable {
                                    likeCallback()
                                }
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Image(
                            painter = painterResource(id = R.drawable.ic_comment_icon),
                            contentDescription = "comment_icon",
                            modifier = Modifier.size(25.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))

                        Image(
                            painter = painterResource(id = R.drawable.ic_sync_icon),
                            contentDescription = "sync_icon",
                            modifier = Modifier.size(25.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Image(
                            painter = painterResource(id = R.drawable.ic_share_icon),
                            contentDescription = "share_icon",
                            modifier = Modifier.size(25.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "${postData!![index].second.likes.size ?: 0} Likes",
                        style = TextStyle(fontSize = 15.sp),
                        modifier = Modifier.then(Modifier.padding(start = 10.dp))
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

            }
        }
    }
}

