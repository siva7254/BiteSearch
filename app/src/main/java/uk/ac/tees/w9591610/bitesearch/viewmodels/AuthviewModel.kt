package uk.ac.tees.w9591610.bitesearch.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uk.ac.tees.w9591610.bitesearch.model.UserModel
import uk.ac.tees.w9591610.bitesearch.util.Constant
import uk.ac.tees.w9591610.bitesearch.util.Util
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.Date
import java.util.UUID

class AuthViewModel : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    private val firebaseStorage = Firebase.storage.reference
    private val storageRef =
        firebaseStorage.child("${Constant.FIREBASE_STORAGE_ROOT}/${UUID.randomUUID()}.jpg")

    private val firebaseFireStore = FirebaseFirestore.getInstance()

    private val _firebaseUser = MutableLiveData<FirebaseUser>()
    val firebaseUser: LiveData<FirebaseUser> = _firebaseUser

    private val _profileData = MutableLiveData<UserModel>()
    val profileData: LiveData<UserModel> = _profileData

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _showLoader = MutableLiveData<Boolean>()
    val showLoader: LiveData<Boolean> = _showLoader

    init {
        _firebaseUser.postValue(firebaseAuth.currentUser)
    }

    fun register(
        name: String,
        username: String,
        email: String,
        password: String,
        bio: String,
        imageUri: Uri
    ) {

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {

            saveDataToFirebase(
                name,
                username,
                email,
                password,
                bio,
                imageUri,
                it.user?.uid
            )
        }.addOnFailureListener {
            _errorMessage.postValue(it.message)
        }
    }

    fun login(email: String, password: String) {
        _showLoader.postValue(true)
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            _errorMessage.postValue("Login Successfully")
            _showLoader.postValue(false)
        }.addOnFailureListener {
            _errorMessage.postValue(it.message)
        }
    }

    private fun saveDataToFirebase(
        name: String,
        username: String,
        email: String,
        password: String,
        bio: String,
        imageUri: Uri,
        uid: String?
    ) {
        storageRef.putFile(imageUri).addOnCompleteListener {
            storageRef.downloadUrl.addOnCompleteListener {
                saveData(
                    name = name,
                    username = username,
                    email = email,
                    password = password,
                    bio = bio,
                    imageUrl = it.result.toString(),
                    uid = uid
                )
            }
        }
    }

    private fun saveData(
        name: String,
        username: String,
        email: String,
        password: String,
        bio: String,
        imageUrl: String,
        uid: String?
    ) {
        val user = UserModel(
            uid = uid,
            name = name,
            username = username,
            email = email,
            password = password,
            bio = bio,
            imageUrl = imageUrl,
            createdAt = Util.convertDateToString(Date()),
            isPrivate = true
        )

        val ref = firebaseFireStore.collection("users")
        ref.document(uid!!).set(user).addOnSuccessListener {
            _errorMessage.postValue("Register successfully")
        }.addOnFailureListener {
            _errorMessage.postValue(it.message)
        }
    }

    fun logout() {
        firebaseAuth.signOut()
        _firebaseUser.postValue(null)
    }

    fun getProfileData(id: String) {
        val ref = firebaseFireStore.collection("users")
        ref.document(id).get().addOnSuccessListener {
            _profileData.postValue(it.toObject(UserModel::class.java))
        }.addOnFailureListener {
            _errorMessage.postValue(it.message)
        }
    }

    fun updateProfile() {
        
    }

}