package uk.ac.tees.w9591610.bitesearch.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import uk.ac.tees.w9591610.bitesearch.R
import uk.ac.tees.w9591610.bitesearch.viewmodels.AuthViewModel
import uk.ac.tees.w9591610.bitesearch.widgets.CustomBottomThreadInfoSheet
import uk.ac.tees.w9591610.bitesearch.widgets.ReusableProfileScreen

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserProfileScreen(controller: NavHostController, uid: String) {
    val authViewModel = remember { AuthViewModel() }

    val profileData by authViewModel.profileData.observeAsState()

    var modalSheetVisible by remember {
        mutableStateOf(false)
    }

    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Expanded)

    authViewModel.getProfileData(uid)

    LaunchedEffect(modalSheetVisible) {
        if (bottomSheetState.isVisible) {
            bottomSheetState.hide()
        } else {
            bottomSheetState.show()
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_back_icon),
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
                text = "Profile",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                modifier = Modifier.then(Modifier.padding(top = 5.dp))
            )
        }
        if (profileData != null) {
            ReusableProfileScreen(userProfileData = profileData!!, threadCallBack = {
                modalSheetVisible = !modalSheetVisible;
            }, callback1 = {}, callback2 = {})
        }
    }
    if (profileData != null) {
        ModalBottomSheetLayout(
            sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            sheetContent = {
                CustomBottomThreadInfoSheet(username = profileData!!.username)
            }, sheetState = bottomSheetState
        ) {

        }

    }

}

