package uk.ac.tees.w9591610.bitesearch.util

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier

class Constant {
    companion object {

        val imagePermission = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        //firebase root nodes
        const val FIREBASE_STORAGE_ROOT = "users"

        val widthModifier = Modifier.fillMaxWidth()

    }
}