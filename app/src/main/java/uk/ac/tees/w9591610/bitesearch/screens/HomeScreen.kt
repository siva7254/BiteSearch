package uk.ac.tees.w9591610.bitesearch.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import uk.ac.tees.w9591610.bitesearch.R
import uk.ac.tees.w9591610.bitesearch.navigation.Routes
import uk.ac.tees.w9591610.bitesearch.util.Util
import uk.ac.tees.w9591610.bitesearch.viewmodels.AddThreadViewModel
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(bottomController: NavHostController?, mainController: NavHostController?) {

    val addThreadViewModel = AddThreadViewModel()
    val postData by addThreadViewModel.postDataV2.observeAsState()
    val isLoading by addThreadViewModel.showLoader.observeAsState(false)

    val placeholder = painterResource(
        id = R.drawable.ic_twitter_icon
    )

    Text(
        text = "Welcome to BiteSearch App",
        style = TextStyle(fontSize = 30.sp),
        modifier = Modifier.then(Modifier.padding(start = 10.dp))
    )

    if (isLoading) {
        CircularProgressIndicator()
    } else {
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
                                        val allLikes = mutableListOf<String>()

                                        if (allLikes.size == 0) {
                                            allLikes.add(FirebaseAuth.getInstance().currentUser?.uid!!)
                                        } else {
                                            allLikes.addAll(postData!![index].second.likes)
                                            val likeIndex =
                                                allLikes.indexOfFirst { it == FirebaseAuth.getInstance().currentUser?.uid!! }
                                            if (likeIndex != -1) {
                                                allLikes.removeAt(likeIndex)
                                            } else {
                                                allLikes.add(FirebaseAuth.getInstance().currentUser?.uid!!)
                                            }
                                        }
                                        addThreadViewModel.likeThread(
                                            FirebaseAuth.getInstance().currentUser?.uid!!,
                                            postData!![index].second.postId,
                                            allLikes
                                        )
                                        postData!![index].second.likes = allLikes.toList()
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

}