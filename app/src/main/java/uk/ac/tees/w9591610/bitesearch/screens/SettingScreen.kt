package uk.ac.tees.w9591610.bitesearch.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import uk.ac.tees.w9591610.bitesearch.R
import uk.ac.tees.w9591610.bitesearch.navigation.Routes
import uk.ac.tees.w9591610.bitesearch.util.Constant
import uk.ac.tees.w9591610.bitesearch.viewmodels.AuthViewModel

@Composable
fun SettingScreen(controller: NavHostController) {
    val titleList = listOf(
        "Follow and invite friends",
        "Notification",
        "Your likes",
        "Privacy",
        "Accessibility",
        "Account",
        "Language",
        "Help",
        "About"
    )

    val imageList = listOf(
        R.drawable.ic_invite_user_setting_icon,
        R.drawable.ic_notification_setting_icon,
        R.drawable.ic_heart_icon,
        R.drawable.ic_lock_icon,
        R.drawable.ic_accessiblity_setting_icon,
        R.drawable.ic_users_icon,
        R.drawable.ic_language_setting_icon,
        R.drawable.ic_help_seeting_icon,
        R.drawable.ic_about_setting_icon
    )

    val authViewModel = AuthViewModel()

    Column(modifier = Constant.widthModifier) {
        /*Row(modifier = Constant.widthModifier, verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = R.drawable.ic_back_icon),
                contentDescription = "close_icon",
                modifier = Modifier
                    .height(40.dp)
                    .width(40.dp)
                    .padding(start = 10.dp, top = 10.dp)
                    .clickable {
                        controller.popBackStack()
                    })
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = "Settings", style = TextStyle(
                    fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black
                ), modifier = Modifier.then(Modifier.padding(top = 5.dp))
            )
        }*/
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn(modifier = Constant.widthModifier) {

       /*     items(titleList.size) {
                Row(
                    modifier = Constant.widthModifier.padding(
                        horizontal = 10.dp, vertical = 10.dp
                    ), verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = imageList[it]), contentDescription = ""
                    )
                    Spacer(modifier = Modifier.width(30.dp))
                    Text(
                        text = titleList[it],
                        style = TextStyle(fontSize = 20.sp, color = Color.Black)
                    )
                }

            }*/
        /*    item {
                TextButton(onClick = { }) {
                    Text(
                        text = "Switch profiles", style = TextStyle(
                            fontSize = 20.sp, fontWeight = FontWeight.Normal, color = Color.Blue
                        ), modifier = Constant.widthModifier.padding(
                            horizontal = 10.dp, vertical = 5.dp
                        )
                    )
                }
            }*/
            item {
                TextButton(onClick = {

                }) {
                    Text(
                        text = "Log out", style = TextStyle(
                            fontSize = 20.sp, fontWeight = FontWeight.Normal, color = Color.Red
                        ), modifier = Constant.widthModifier
                            .padding(
                                horizontal = 10.dp, vertical = 5.dp
                            )
                            .clickable {
                                authViewModel.logout()
                                controller.navigate(Routes.Login.route)
                            }
                    )
                }

            }
        }
    }

}