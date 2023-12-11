package uk.ac.tees.w9591610.bitesearch.navigation

sealed class Routes(val route: String) {

    object Home : Routes("home")
    object AddThread : Routes("add_thread")
    object SearchThread : Routes("search")
    object Profile : Routes("profile")
    object EditProfile : Routes("edit_profile")

    object EditBioLinkProfile : Routes("edit_profile_bio_link/{data}/{value}")

    object Notification : Routes("notification")
    object Splash : Routes("splash")
    object BottomNav : Routes("bottom_nav")

    object Login : Routes("login")
    object Register : Routes("register")
    object UserProfile : Routes("other_user/{data}")

    object Privacy : Routes("privacy")
    object Setting : Routes("setting")
    object DestList : Routes("dest_list")


}
