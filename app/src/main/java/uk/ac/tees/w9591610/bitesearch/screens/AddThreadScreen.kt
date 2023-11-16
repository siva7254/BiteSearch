package uk.ac.tees.w9591610.bitesearch.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import uk.ac.tees.w9591610.bitesearch.R
import uk.ac.tees.w9591610.bitesearch.navigation.Routes
import uk.ac.tees.w9591610.bitesearch.util.Util
import uk.ac.tees.w9591610.bitesearch.viewmodels.AddThreadViewModel
import uk.ac.tees.w9591610.bitesearch.viewmodels.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AddThreadScreen(controller: NavHostController) {
    BottomSheetDesign(controller)
}

@Composable
fun BottomSheetDesign(controller: NavHostController) {

    var description by remember { mutableStateOf("") }

    var images by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val context = LocalContext.current

    val addThreadViewModel = AddThreadViewModel()
    val authViewModel = AuthViewModel()
    val isLoading by addThreadViewModel.showLoader.observeAsState(false)

    val currentUser by authViewModel.firebaseUser.observeAsState()
    val result by addThreadViewModel.result.observeAsState()
    val profileData by authViewModel.profileData.observeAsState()

    authViewModel.getProfileData(FirebaseAuth.getInstance().currentUser!!.uid)

    LaunchedEffect(result) {
        if (result?.status == "success") {
            Util.showToast(context, result!!.message)
            description = ""
            controller.navigate(Routes.Home.route) {
                popUpTo(Routes.AddThread.route) {
                    inclusive = true
                }
            }
        }
    }

    LaunchedEffect(profileData) {
        Log.e("TAG", "BottomSheetDesign: ${profileData}")
    }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {

        val (postCard, firstRow, divider, username, verifiedIcon, descriptionBox, descriptionLabel, postImage, galleryIcon, cameraIcon, progressBar) = createRefs()

        val imageModifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .constrainAs(postImage) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(descriptionBox.bottom)
            }
            .then(Modifier.padding(start = 10.dp, end = 10.dp, top = 20.dp))
            .clip(RoundedCornerShape(20.dp))

        val imagesLauncher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.GetMultipleContents(),
                onResult = {
                    images = it
                })

        Row(horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .constrainAs(firstRow) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }) {
            Image(painter = painterResource(id = R.drawable.ic_close_icon),
                contentDescription = "close_icon",
                modifier = Modifier.clickable {
                    controller.navigate(Routes.Home.route)
                })
            Text(
                text = "New Thread",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Medium)
            )

            Text(
                modifier = Modifier.clickable {
                    if (currentUser != null) {
                        if(images.isNotEmpty()){
                            addThreadViewModel.createPost( currentUser!!.uid,
                                description,
                                images)
                        }
                    }
                }, text = "Post", style = TextStyle(
                    fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Blue
                )
            )
        }

        Divider(thickness = 1.dp, modifier = Modifier.constrainAs(divider) {
            start.linkTo(parent.start)
            top.linkTo(firstRow.bottom)
        })

        if (profileData != null) {
            Image(painter = rememberAsyncImagePainter(model = profileData!!.imageUrl),
                contentDescription = "user_image",
                modifier = Modifier
                    .width(65.dp)
                    .height(65.dp)
                    .padding(start = 10.dp, top = 20.dp)
                    .clip(CircleShape)
                    .constrainAs(postCard) {
                        start.linkTo(parent.start)
                        top.linkTo(divider.bottom)
                    }
            )
        } else {
            Image(painter = painterResource(id = R.drawable.ic_people_icon),
                contentDescription = "user_image",
                modifier = Modifier
                    .width(65.dp)
                    .height(65.dp)
                    .padding(start = 10.dp, top = 20.dp)
                    .clip(CircleShape)
                    .constrainAs(postCard) {
                        start.linkTo(parent.start)
                        top.linkTo(divider.bottom)
                    }
            )
        }



        Text(text = profileData?.username ?: "", style = TextStyle(
            fontSize = 13.sp, color = Color.Black, fontWeight = FontWeight.Medium
        ), modifier = Modifier
            .padding(start = 10.dp, top = 20.dp)
            .constrainAs(username) {
                start.linkTo(postCard.end)
                top.linkTo(postCard.top)
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

        Image(painter = painterResource(id = R.drawable.ic_gallery_icon),
            contentDescription = "gallery_icon",
            modifier = Modifier
                .height(40.dp)
                .width(40.dp)
                .constrainAs(galleryIcon) {
                    end.linkTo(parent.end)
                    top.linkTo(divider.bottom)
                }
                .then(Modifier.padding(top = 15.dp))
                .clip(CircleShape)
                .clickable {
                    imagesLauncher.launch("image/*")
                })

        Image(painter = painterResource(id = R.drawable.ic_camera_icon),
            contentDescription = "gallery_icon",
            modifier = Modifier
                .height(40.dp)
                .width(40.dp)
                .constrainAs(cameraIcon) {
                    end.linkTo(galleryIcon.start)
                    top.linkTo(galleryIcon.top)
                    bottom.linkTo(galleryIcon.bottom)
                }
                .then(Modifier.padding(top = 15.dp))
                .clip(CircleShape)
                .clickable {
                    imagesLauncher.launch("image/*")
                })

        BasicTextField(maxLines = 5,
            value = description,
            onValueChange = {
                description = it
            }, textStyle = TextStyle(fontSize = 15.sp),
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(descriptionBox) {
                    start.linkTo(postCard.end)
                    end.linkTo(parent.end)
                    top.linkTo(username.bottom)
                }
                .then(Modifier.padding(top = 10.dp, start = 40.dp)))

        if (images.isNotEmpty()) {
            if (images.size == 1) {
                Image(
                    painter = rememberAsyncImagePainter(model = images[0]),
                    contentDescription = "",
                    modifier = imageModifier,
                    contentScale = ContentScale.Fit
                )
            } else {
                LazyRow(modifier = Modifier
                    .constrainAs(postImage) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(descriptionBox.bottom)
                    }
                    .fillMaxWidth()) {
                    items(images) {
                        ConstraintLayout {
                            val (imageCard, crossIcon) = createRefs();
                            Card(
                                modifier = Modifier.then(
                                    Modifier
                                        .padding(vertical = 10.dp, horizontal = 5.dp)
                                        .constrainAs(imageCard) {
                                            start.linkTo(parent.start)
                                            end.linkTo(parent.end)
                                        })
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(model = it),
                                    contentDescription = "",
                                    contentScale = ContentScale.FillWidth, modifier = Modifier
                                        .height(200.dp)
                                        .width(200.dp)

                                )
                            }

                            IconButton(onClick = {
                                //(images as ArrayList<Uri>).removeIf { image==it }
                            }) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_close_icon),
                                    contentDescription = "close_icon",
                                    modifier = Modifier
                                        .height(20.dp)
                                        .width(20.dp)
                                        .constrainAs(crossIcon) {
                                            start.linkTo(imageCard.end)
                                            top.linkTo(imageCard.top)

                                        })
                            }

                        }


                    }
                }

            }
        }

        if (description.isEmpty()) {
            Text(
                text = "Write description..",
                style = TextStyle(fontSize = 15.sp),
                modifier = Modifier
                    .constrainAs(descriptionLabel) {
                        start.linkTo(postCard.end)
                        top.linkTo(username.bottom)
                    }
                    .then(Modifier.padding(start = 8.dp, top = 9.dp))
            )
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.constrainAs(progressBar) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            })
        }

    }

}
