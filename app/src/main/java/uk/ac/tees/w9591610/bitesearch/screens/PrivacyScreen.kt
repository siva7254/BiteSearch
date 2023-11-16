package uk.ac.tees.w9591610.bitesearch.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Switch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import uk.ac.tees.w9591610.bitesearch.R
import uk.ac.tees.w9591610.bitesearch.util.Constant

@Composable
fun PrivacyScreen(controller: NavHostController) {
    val titleList = listOf("Mentions", "Muted", "Hidden Words", "Profile you follow")

    val imageList = listOf(
        R.drawable.ic_thread_privacy_icon,
        R.drawable.ic_mute_icon,
        R.drawable.ic_hide_icon,
        R.drawable.ic_users_icon
    )

    Column(modifier = Constant.widthModifier) {
        Row(modifier = Constant.widthModifier, verticalAlignment = Alignment.CenterVertically) {
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
                text = "Privacy", style = TextStyle(
                    fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black
                ), modifier = Modifier.then(Modifier.padding(top = 5.dp))
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn(modifier = Constant.widthModifier) {

            items(titleList.size) {
                if (it == 0) {
                    CustomPrivacyItem(R.drawable.ic_users_icon, "Private profile", true)
                } else {
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
                }
            }
            item {
                OtherPrivacyWidget()
            }
            item {
                CustomPrivacyItem(R.drawable.ic_block_icon, "Blocked")
            }
            item {
                CustomPrivacyItem(R.drawable.ic_hide_like_icon, "Hide likes")
            }
        }
    }

}


@Composable
fun CustomPrivacyItem(icon: Int, title: String, forAccount: Boolean = false) {
    Row(
        modifier = if (forAccount) Constant.widthModifier.padding(horizontal = 10.dp) else Constant.widthModifier.padding(
            horizontal = 10.dp,
            vertical = 10.dp
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.weight(0.9f)) {
            Image(painter = painterResource(id = icon), contentDescription = "")
            Spacer(modifier = Modifier.width(30.dp))
            Text(
                text = title, style = TextStyle(fontSize = 20.sp, color = Color.Black)
            )
        }
        if (forAccount) {
            Switch(checked = true, onCheckedChange = {
            })
        } else {
            Image(
                painter = painterResource(id = R.drawable.ic_move_arraow_icon),
                contentDescription = ""
            )
        }
    }
}

@Composable
fun OtherPrivacyWidget() {
    Column(modifier = Constant.widthModifier) {
        Row(
            modifier = Constant.widthModifier.padding(
                horizontal = 10.dp, vertical = 10.dp
            ), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Other privacy settings", style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
            )
            Image(
                painter = painterResource(id = R.drawable.ic_move_arraow_icon),
                contentDescription = ""
            )
        }

        Text(
            text = "Some settings, like restrict, apply to both Threads and Instagram and can be managed on Instagram.",
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            ),
            modifier = Modifier
                .width(300.dp)
                .padding(
                    horizontal = 10.dp, vertical = 10.dp
                )
        )

    }
}