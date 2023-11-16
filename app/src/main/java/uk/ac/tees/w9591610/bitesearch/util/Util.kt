package uk.ac.tees.w9591610.bitesearch.util

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import at.favre.lib.crypto.bcrypt.BCrypt
import uk.ac.tees.w9591610.bitesearch.navigation.Routes
import java.security.SecureRandom
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import javax.inject.Singleton

class Util {

    companion object {
        fun goTo(
            navHostController: NavHostController,
            route: String,
        ) {
            navHostController.navigate(route) {
                popUpTo(navHostController.graph.startDestinationId) {
                    inclusive = true
                }
            }
        }

        fun showToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        fun generateHashPassword(password: String): String {
            return BCrypt.withDefaults().hashToString(12, password.toCharArray())
        }

        fun verifyPassword(password: String, hashedPassword: String): Boolean {
            return BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified
        }

        fun convertDateToString(date: Date): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            return inputFormat.format(date)
        }

        fun getDay(date: String){
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
//            val inputResult=inputFormat.format(Date.parse(date))
//            val outputFormat=SimpleDateFormat("dd", Locale.getDefault())
//            return outputFormat.format(inputResult)
            Log.e("TAG", "getDay: ${Date.parse(date)}", )
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun formatDateString(inputDate: String): String {
            val inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
            val outputFormatter = DateTimeFormatter.ofPattern("dd MMMM", Locale.ENGLISH)

            val dateTime = LocalDateTime.parse(inputDate, inputFormatter)
            return dateTime.format(outputFormatter)
        }

    }

}