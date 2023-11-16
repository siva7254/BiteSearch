package uk.ac.tees.w9591610.bitesearch.model

data class UserModel(
    val uid: String? = "",
    val name: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val bio: String = "",
    val imageUrl: String = "",
    val createdAt: String = "",
    val isPrivate: Boolean = true
)
