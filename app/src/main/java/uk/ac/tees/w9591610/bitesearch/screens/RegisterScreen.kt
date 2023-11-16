package uk.ac.tees.w9591610.bitesearch.screens

import uk.ac.tees.w9591610.bitesearch.widgets.AuthBottomLabel
import uk.ac.tees.w9591610.bitesearch.widgets.CustomButton
import uk.ac.tees.w9591610.bitesearch.widgets.CustomTextFiled
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import uk.ac.tees.w9591610.bitesearch.R
import uk.ac.tees.w9591610.bitesearch.navigation.Routes
import uk.ac.tees.w9591610.bitesearch.util.Constant
import uk.ac.tees.w9591610.bitesearch.util.Util
import uk.ac.tees.w9591610.bitesearch.validation.Validation
import uk.ac.tees.w9591610.bitesearch.viewmodels.AuthViewModel

@Composable
fun RegisterScreen(navHostController: NavHostController) {

    var name by remember { mutableStateOf(TextFieldValue("")) }
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var bio by remember { mutableStateOf(TextFieldValue("")) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val focusRequester = remember { FocusRequester() }

    val authViewModel = AuthViewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState()
    val errorMessage by authViewModel.errorMessage.observeAsState()

    val context = LocalContext.current

    val modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp, 0.dp)

    //handle image picker
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            if (it) {
                Toast.makeText(context, "permission granted successfully", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(context, "Please allow required permission", Toast.LENGTH_SHORT)
                    .show()
            }
        })

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            if (it != null) {
                imageUri = it
            }
        })

    //observer for register
    LaunchedEffect(firebaseUser) {
        if (firebaseUser != null) {
            navHostController.navigate(Routes.BottomNav.route) {
                popUpTo(navHostController.graph.findStartDestination().id) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }

    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            Util.showToast(context, errorMessage!!)
        }
    }

    Box(modifier = Modifier.background(color = Color.Black)) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(painter = if (imageUri == null) painterResource(id = R.drawable.ic_people_icon) else rememberAsyncImagePainter(
                model = imageUri
            ),
                contentDescription = "user",
                modifier = Modifier
                    .size(100.dp)
                    .clip(
                        CircleShape
                    )
                    .background(Color.White)
                    .clickable {
                        //check image permission

                        if (ContextCompat.checkSelfPermission(
                                context,
                                Constant.imagePermission
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            imageLauncher.launch("image/*")
                        } else {
                            permissionLauncher.launch(Constant.imagePermission)
                        }
                    })
            Spacer(modifier = Modifier.height(20.dp))
            CustomTextFiled(textFiled = name, callBack = {
                name = it
            }, "Name", modifier, false)
            Spacer(modifier = Modifier.height(20.dp))
            CustomTextFiled(textFiled = username, callBack = {
                username = it
            }, "Username", modifier, false)
            Spacer(modifier = Modifier.height(20.dp))
            CustomTextFiled(textFiled = email, callBack = {
                email = it
            }, "Email", modifier, false)

            Spacer(modifier = Modifier.height(20.dp))

            CustomTextFiled(textFiled = password, callBack = {
                password = it
            }, "Password", modifier, true, focusRequester)
            Spacer(modifier = Modifier.height(20.dp))

            CustomTextFiled(textFiled = bio, callBack = {
                bio = it
            }, "Bio", modifier.height(100.dp), true, focusRequester)
            Spacer(modifier = Modifier.height(20.dp))

            CustomButton({
                val filedList = arrayListOf(name, username, email, password, bio)
                if (Validation.isFormDataValid(filedList) && imageUri != null) {
                    authViewModel.register(
                        name.text.trim(),
                        username.text.trim(),
                        email.text.trim(),
                        password.text.trim(),
                        bio.text.trim(),
                        imageUri!!
                    )
                } else {
                    Util.showToast(context, "Please fill up all values")
                }
            }, "Register", modifier)

            Spacer(modifier = Modifier.height(20.dp))

            AuthBottomLabel("Already have an account? ", "Login", modifier = Modifier.clickable {
                navHostController.navigate(Routes.Login.route) {
                    popUpTo(navHostController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            })

        }
    }
}