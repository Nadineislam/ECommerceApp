package com.example.ecommerceapp.presentation.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.CommerceApplication
import com.example.ecommerceapp.data.User
import com.example.ecommerceapp.utils.RegisterValidation
import com.example.ecommerceapp.utils.Resource
import com.example.ecommerceapp.utils.validateEmail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.*
import javax.inject.Inject

@HiltViewModel
class UserAccountViewModel @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: StorageReference,
    app: Application
) : AndroidViewModel(app) {
    private val _user = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val user: StateFlow<Resource<User>> = _user

    private val _updateInfo = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val updateInfo: StateFlow<Resource<User>> = _updateInfo

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch { _user.emit(Resource.Loading()) }
        fireStore.collection("user").document(auth.uid!!).get().addOnSuccessListener {
            val user = it.toObject(User::class.java)
            user?.let { viewModelScope.launch { _user.emit(Resource.Success(it)) } }
        }
            .addOnFailureListener { viewModelScope.launch { _user.emit(Resource.Error(it.message.toString())) } }


    }

    fun updateUser(user: User, imageUri: Uri?) {
        val areInputsValidate = validateEmail(user.email) is RegisterValidation.Success
                && user.firstName.trim().isNotEmpty()
                && user.lastName.trim().isNotEmpty()
        if (!areInputsValidate) {
            viewModelScope.launch { _updateInfo.emit(Resource.Error("Check your inputs")) }
            return
        }
        viewModelScope.launch { _updateInfo.emit(Resource.Loading()) }
        if (imageUri == null) {
            saveUserInformation(user, true)
        } else {
            saveUserInformationWithNewImage(user, imageUri)
        }


    }

    private fun saveUserInformationWithNewImage(user: User, imageUri: Uri) {
        viewModelScope.launch {
            try {
                val imageBitmap = MediaStore.Images.Media.getBitmap(
                    getApplication<CommerceApplication>().contentResolver,
                    imageUri
                )
                val byteArrayOutputStream = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 96, byteArrayOutputStream)
                val imageByteArray = byteArrayOutputStream.toByteArray()
                val imageDirectory = storage.child("profileImages/${auth.uid}/${UUID.randomUUID()}")
                val result = imageDirectory.putBytes(imageByteArray).await()
                val imageUrl = result.storage.downloadUrl.await().toString()
                saveUserInformation(user.copy(imagePath = imageUrl), false)

            } catch (e: Exception) {
                viewModelScope.launch { _user.emit(Resource.Error(e.message.toString())) }
            }
        }
    }

    private fun saveUserInformation(user: User, shouldRetrieveOldImage: Boolean) {
        fireStore.runTransaction { transition ->
            val documentRef = fireStore.collection("user").document(auth.uid!!)
            if (shouldRetrieveOldImage) {
                val currentUser = transition.get(documentRef).toObject(User::class.java)
                val newUser = user.copy(imagePath = currentUser!!.imagePath)
                transition.set(documentRef, newUser)
            } else {
                transition.set(documentRef, user)

            }

        }.addOnSuccessListener {
            viewModelScope.launch { _updateInfo.emit(Resource.Success(user)) }
        }.addOnFailureListener {
            viewModelScope.launch { _updateInfo.emit(Resource.Error(it.message.toString())) }
        }
    }

}