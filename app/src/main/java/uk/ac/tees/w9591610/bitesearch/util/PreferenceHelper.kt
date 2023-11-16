package uk.ac.tees.w9591610.bitesearch.util

import android.content.Context

class PreferenceHelper() {

    companion object {
        fun setBioOrLinkData(context: Context,key:String,value:String) {
            val pref = context.getSharedPreferences("thread_app", Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.putString(key,value)
            editor.apply()
        }

        fun getBioOrLink(context: Context, key: String): String? {
            val pref = context.getSharedPreferences("thread_app", Context.MODE_PRIVATE)
            return pref.getString(key, null)
        }

        fun clearBioOrLink(context: Context) {
            val pref = context.getSharedPreferences("thread_app", Context.MODE_PRIVATE)
            val editor = pref.edit().also {
                it.remove("bio")
                it.remove("link")
                it.apply()
            }
        }

    }
}