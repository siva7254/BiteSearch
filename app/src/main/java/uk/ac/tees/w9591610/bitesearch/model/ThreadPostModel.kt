package uk.ac.tees.w9591610.bitesearch.model

import com.google.firebase.firestore.QueryDocumentSnapshot

class ThreadPostModel(
    val postId: String,
    val uid: String,
    val description: String,
    val image: List<String>,
    val createdAt: String,
    var likes: List<String> = emptyList()
) {
    companion object {
        fun fromSnapShot(snapshot: QueryDocumentSnapshot): ThreadPostModel {
            val postId = snapshot["postId"].toString()
            val uid = snapshot["uid"].toString()
            val description = snapshot["description"].toString()
            val image = snapshot["image"] as List<String>
            val createdAt = snapshot["createdAt"].toString()
            val likes = snapshot["likes"] as List<String> ?: emptyList()
            return ThreadPostModel(postId, uid, description, image, createdAt, likes)
        }

    }
}
