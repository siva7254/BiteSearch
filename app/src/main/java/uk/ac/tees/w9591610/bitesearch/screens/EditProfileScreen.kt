package uk.ac.tees.w9591610.bitesearch.screens

import android.nfc.cardemulation.CardEmulation
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import uk.ac.tees.w9591610.bitesearch.R
import uk.ac.tees.w9591610.bitesearch.navigation.Routes
import uk.ac.tees.w9591610.bitesearch.util.PreferenceHelper

@Composable
fun EditProfileScreen(navHostController: NavHostController) {

    val labelStyle = TextStyle(
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal,
        color = Color.Black
    )

    var isPrivate by remember { mutableStateOf(true) }
    val isEditPerform by remember { mutableStateOf(false) }

    val context = LocalContext.current;

    val defaultStartMargin = 10.dp
    val defaultTopMargin = 10.dp


    val bioData by remember {
        mutableStateOf(PreferenceHelper.getBioOrLink(context, "bio"))
    }

    val linkData by remember {
        mutableStateOf(PreferenceHelper.getBioOrLink(context, "link"))
    }

    PreferenceHelper.clearBioOrLink(context)

    ConstraintLayout(modifier = Modifier.then(Modifier.padding(horizontal = 10.dp))) {
        val (topBar, card) = createRefs()

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .constrainAs(topBar) {
                    top.linkTo(parent.top, defaultTopMargin)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
        ) {
            Row(modifier = Modifier.weight(0.9f), verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = {
                    navHostController.popBackStack()
                }, modifier = Modifier) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_close_icon),
                        contentDescription = "close_icon",
                    )
                }
                Text(
                    text = "Edit profile",
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Medium)
                )
            }

            TextButton(onClick = {}, modifier = Modifier.weight(0.2f)) {
                Text(
                    text = "Done",
                    style = if (isEditPerform) labelStyle else labelStyle.copy(color = Color.Gray)
                )
            }
        }

        Box(
            modifier = Modifier
                .constrainAs(card) {
                    top.linkTo(topBar.bottom)
                    start.linkTo(parent.start, defaultStartMargin)
                    end.linkTo(parent.end, defaultStartMargin)
                    bottom.linkTo(parent.bottom)
                }
                .border(0.8.dp, color = Color.Black, shape = RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .padding(10.dp)
                    .then(Modifier.padding(10.dp))
                    .fillMaxWidth(),
            ) {
                val (nameLabel, lockIcon, name, username, userImage, divider1, bioLabel, bio, divider2, linkLabel, link, divider3, privateLabel, switch) = createRefs()

                Text(text = "Name", style = labelStyle, modifier = Modifier.constrainAs(nameLabel) {
                    start.linkTo(parent.start, margin = defaultStartMargin)
                    top.linkTo(parent.top, margin = defaultTopMargin)
                })

                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "lock",
                    modifier = Modifier.constrainAs(lockIcon) {
                        start.linkTo(parent.start, margin = defaultStartMargin)
                        top.linkTo(nameLabel.bottom, margin = defaultTopMargin)
                    })

                Text(text = "Rjcoding", style = labelStyle, modifier = Modifier
                    .constrainAs(name) {
                        start.linkTo(lockIcon.end)
                        top.linkTo(lockIcon.top)
                        bottom.linkTo(lockIcon.bottom)
                    }
                    .clickable {
                        Toast
                            .makeText(
                                context,
                                "Your name and username are synced with instagram and can only be changed there",
                                Toast.LENGTH_LONG
                            )
                            .show()
                    })

                Text(
                    text = "(javiyaraj12)",
                    style = labelStyle,
                    modifier = Modifier
                        .constrainAs(username) {
                            start.linkTo(name.end, margin = defaultStartMargin)
                            top.linkTo(name.top)
                            bottom.linkTo(name.bottom)
                        }
                        .clickable {
                            Toast
                                .makeText(
                                    context,
                                    "Your name and username are synced with instagram and can only be changed there",
                                    Toast.LENGTH_LONG
                                )
                                .show()
                        })

                Image(
                    painter = painterResource(id = R.drawable.ic_people_icon),
                    contentDescription = "user_image",
                    modifier = Modifier
                        .constrainAs(userImage) {
                            top.linkTo(parent.top, defaultTopMargin)
                            end.linkTo(parent.end, defaultStartMargin)
                        }
                        .height(45.dp)
                        .width(45.dp)
                        .clip(CircleShape)
                )

                Divider(thickness = 0.8.dp, color = Color.Black, modifier = Modifier
                    .constrainAs(divider1) {
                        start.linkTo(parent.start, defaultStartMargin)
                        end.linkTo(parent.end, defaultStartMargin)
                        top.linkTo(username.bottom, defaultTopMargin)
                    }
                    .padding(start = defaultStartMargin, end = 100.dp))

                Text(text = "Bio", style = labelStyle, modifier = Modifier.constrainAs(bioLabel) {
                    start.linkTo(parent.start, margin = defaultStartMargin)
                    top.linkTo(divider1.bottom, margin = defaultTopMargin)
                })

                Text(
                    text = if (bioData != null) bioData!! else "",
                    style = labelStyle,
                    modifier = Modifier
                        .constrainAs(bio) {
                            start.linkTo(parent.start, margin = defaultStartMargin)
                            top.linkTo(bioLabel.bottom, margin = defaultTopMargin)
                        }
                        .clickable {
                            val route = Routes.EditBioLinkProfile.route
                                .replace("{data}", "true")
                                .replace(
                                    "{value}",
                                    if (bioData != null) bioData!! else "test bio"
                                )

                            navHostController.navigate(route)
                        })

                Divider(thickness = 0.8.dp, color = Color.Black, modifier = Modifier
                    .constrainAs(divider2) {
                        start.linkTo(parent.start, defaultStartMargin)
                        end.linkTo(parent.end, defaultStartMargin)
                        top.linkTo(bio.bottom, defaultTopMargin)
                    }
                    .padding(start = defaultStartMargin, end = defaultStartMargin))

                Text(text = "Link", style = labelStyle, modifier = Modifier.constrainAs(linkLabel) {
                    start.linkTo(parent.start, margin = defaultStartMargin)
                    top.linkTo(divider2.bottom, margin = defaultTopMargin)
                })

                Text(
                    text = if (linkData != null) linkData!! else "",
                    style = labelStyle.copy(color = Color.Blue),
                    modifier = Modifier
                        .constrainAs(link) {
                            start.linkTo(parent.start, margin = defaultStartMargin)
                            top.linkTo(linkLabel.bottom, margin = defaultTopMargin)
                        }
                        .clickable {
                            val route = Routes.EditBioLinkProfile.route
                                .replace("{data}", "false")
                                .replace("{value}", if (linkData != null) linkData!! else "test link")
                            navHostController.navigate(route)
                        })

                Divider(thickness = 0.8.dp, color = Color.Black, modifier = Modifier
                    .constrainAs(divider3) {
                        start.linkTo(parent.start, defaultStartMargin)
                        end.linkTo(parent.end, defaultStartMargin)
                        top.linkTo(link.bottom, defaultTopMargin)
                    }
                    .padding(start = defaultStartMargin, end = defaultStartMargin))



                Text(
                    text = "Private Profile",
                    style = labelStyle,
                    modifier = Modifier.constrainAs(privateLabel) {
                        start.linkTo(parent.start, margin = defaultStartMargin)
                        top.linkTo(divider3.bottom, margin = defaultTopMargin)
                    })

                Switch(checked = isPrivate, onCheckedChange = {
                    isPrivate = it
                }, modifier = Modifier.constrainAs(switch) {
                    end.linkTo(parent.end)
                    top.linkTo(divider3.bottom)
                })

            }
        }
    }

}

val DarkPink = Color(0XFFFF597B)
val Pink = Color(0XFFFF8E9E)
val LightPink = Color(0XFFF9B5D0)
val Background = Color(0XFFEEEEEE)

@Composable
fun CheckCircle(
    modifier: Modifier = Modifier
) {

    Card(
        shape = CircleShape, modifier = modifier.size(20.dp)
    ) {
        Box(modifier = Modifier.background(Color.White))
    }

}

@Composable
fun CustomToggleButton(
    selected: Boolean,
    modifier: Modifier = Modifier,
    onUpdate: (Boolean) -> Unit
) {

    Card(
        modifier = modifier
            .width(50.dp)
            .clickable {
                onUpdate(!selected)
            }, shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier.background(
                if (selected) DarkPink else LightPink.copy(0.4f)
            ), contentAlignment = if (selected) Alignment.TopEnd else Alignment.TopStart
        ) {
            CheckCircle(modifier = Modifier.padding(5.dp))
        }
    }

}