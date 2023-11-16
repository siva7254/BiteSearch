package uk.ac.tees.w9591610.bitesearch.validation

import androidx.compose.ui.text.input.TextFieldValue

class Validation {

    companion object {

        fun isFormDataValid(filedList: ArrayList<TextFieldValue>): Boolean {
            return filedList.all { it.text.trim().isNotEmpty() }
        }
    }
}