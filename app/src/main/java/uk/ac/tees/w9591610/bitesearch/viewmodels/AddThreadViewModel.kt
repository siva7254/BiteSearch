package uk.ac.tees.w9591610.bitesearch.viewmodels

import android.annotation.SuppressLint
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uk.ac.tees.w9591610.bitesearch.model.ResultModel
import uk.ac.tees.w9591610.bitesearch.model.ThreadPostModel
import uk.ac.tees.w9591610.bitesearch.model.UserModel
import uk.ac.tees.w9591610.bitesearch.util.Util
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.Date
import java.util.UUID

class AddThreadViewModel(val isFromSearch: Boolean = false, var uid: String? = null) : ViewModel() {

    private val firebaseStorage = Firebase.storage.reference

    private val firebaseFireStore = FirebaseFirestore.getInstance()

    private val _result = MutableLiveData<ResultModel>()
    val result: LiveData<ResultModel> = _result

    private val _showLoader = MutableLiveData<Boolean>()
    val showLoader: LiveData<Boolean> = _showLoader

    private val _postDataV2 = MutableLiveData<List<Pair<UserModel, ThreadPostModel>>>()
    val postDataV2: LiveData<List<Pair<UserModel, ThreadPostModel>>> = _postDataV2

    private val _userList = MutableLiveData<List<UserModel>>()
    val userList: LiveData<List<UserModel>> = _userList

    private val _postChanges = MutableLiveData<Boolean>()
    val postChanges: LiveData<Boolean> = _postChanges

    init {
        if (isFromSearch) {
            getUserList(FirebaseAuth.getInstance().currentUser!!.uid) {
                _userList.postValue(it)
            }
        } else {
            fetchThread {
                _postDataV2.postValue(it)
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun createPost(uid: String, description: String, images: List<Uri>) {
        _showLoader.postValue(true)

        val imageUrlList = mutableListOf<String>()

        images.forEach {
            val postId = UUID.randomUUID()
            val storageRef = firebaseStorage.child("post/${postId}.jpg")
            storageRef.putFile(it).addOnCompleteListener {
                storageRef.downloadUrl.addOnSuccessListener { url ->
                    imageUrlList.add(0, url.toString())
                    if (images.size == imageUrlList.size) {
                        savePostIntoDatabase(uid, postId.toString(), description, imageUrlList)
                    }
                }.addOnFailureListener {
                    _result.postValue(ResultModel("failed", "something went wrong"))
                }
            }
        }

    }

    private fun savePostIntoDatabase(
        uid: String,
        potsId: String,
        description: String,
        urlList: List<String>
    ) {
        val ref = firebaseFireStore.collection("post")
        val postModel =
            ThreadPostModel(potsId, uid, description, urlList, Util.convertDateToString(Date()))
        ref.document(potsId).set(postModel).addOnSuccessListener {
            _showLoader.postValue(false)
            _result.postValue(ResultModel("success", "post successfully"))
        }.addOnFailureListener {
            _result.postValue(ResultModel("failed", "something went wrong"))
            _showLoader.postValue(false)
        }
    }

    private fun fetchThread(onResultList: (List<Pair<UserModel, ThreadPostModel>>) -> Unit) {
        val ref = firebaseFireStore.collection("post").orderBy("createdAt")

        val list = mutableListOf<Pair<UserModel, ThreadPostModel>>()

        ref.get().addOnSuccessListener { querySnapshot ->

            for (threads in querySnapshot) {
                val threadPostModel =
                    ThreadPostModel.fromSnapShot(threads);

                fetchUser(threadPostModel) {
                    list.add(0, it to threadPostModel)
                    //condition is used because firebase take some time to  return data when you are get multiple data at same time
                    if (list.size == querySnapshot.size()) {
                        if (uid == null) {
                            onResultList(list)
                        } else {
                            val filterList=list.filter { thread->thread.first.uid==uid }
                            onResultList(filterList)
                        }
                    }
                }
            }

        }
    }


    private fun fetchUser(thread: ThreadPostModel, onResult: (UserModel) -> Unit) {
        firebaseFireStore.collection("users").document(thread.uid).get().addOnSuccessListener {
            val user = it.toObject(UserModel::class.java)
            user?.let(onResult)
        }.addOnFailureListener {
            _result.postValue(ResultModel("failed", "something went wrong"))
        }
    }

    private fun getUserList(uid: String, onResult: (List<UserModel>) -> Unit) {
        _showLoader.postValue(true)
        firebaseFireStore.collection("users").get().addOnSuccessListener { querySnapshot ->
            val userList = mutableListOf<UserModel>()

            for (user in querySnapshot) {
                val data = user.toObject(UserModel::class.java)
                if (data.uid != uid) { //remove it self from user list
                    userList.add(data);
                }
            }

            userList.let(onResult)
            _result.postValue(ResultModel("success", "user list get successfully"))
        }.addOnFailureListener {
            _showLoader.postValue(false)
            _result.postValue(ResultModel("failed", "something went wrong"))
        }
    }

    fun likeThread(uid: String, postId: String, likes: List<String>) {

        val ref = firebaseFireStore.collection("post").document(postId).update("likes", likes)
            .addOnSuccessListener {
                _postChanges.postValue(true)
                _result.postValue(ResultModel("success", "Thread liked successfully"))
            }.addOnFailureListener {
                _postChanges.postValue(false)
                _result.postValue(ResultModel("failed", "something went wrong"))
            }
    }

}