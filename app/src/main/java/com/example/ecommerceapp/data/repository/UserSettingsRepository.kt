package com.example.ecommerceapp.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.example.ecommerceapp.data.Address
import com.example.ecommerceapp.login_register_feature.data.model.User
import com.example.ecommerceapp.core.utils.RegisterValidation
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.core.utils.validateEmail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

class UserSettingsRepository @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: StorageReference, @ApplicationContext private val context: Context
) {
    suspend fun getUser(): Resource<User> {
        return try {
            val documentSnapshot = fireStore.collection("user").document(auth.uid!!).get().await()
            val user = documentSnapshot.toObject(User::class.java)
            user?.let { Resource.Success(it) } ?: Resource.Error("User not found")
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

    suspend fun updateUser(user: User, imageUri: Uri?): Resource<User> {
        val areInputsValidate = validateEmail(user.email) is RegisterValidation.Success
                && user.firstName.trim().isNotEmpty()
                && user.lastName.trim().isNotEmpty()
        if (!areInputsValidate) {
            return Resource.Error("Check your inputs")
        }

        return try {
            val savedUser = if (imageUri == null) {
                saveUserInformation(user, true)
            } else {
                saveUserInformationWithNewImage(user, imageUri)
            }
            Resource.Success(savedUser)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

    suspend fun getUser(userId: String): User {
        val documentSnapshot = fireStore.collection("user").document(userId).get().await()
        return documentSnapshot.toObject(User::class.java)!!
    }

    fun logOut() {
        auth.signOut()
    }

    private suspend fun saveUserInformationWithNewImage(user: User, imageUri: Uri): User {
        val imageBitmap = MediaStore.Images.Media.getBitmap(
            context.contentResolver,
            imageUri
        )
        val byteArrayOutputStream = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 96, byteArrayOutputStream)
        val imageByteArray = byteArrayOutputStream.toByteArray()
        val imageDirectory = storage.child("profileImages/${auth.uid}/${UUID.randomUUID()}")
        val result = imageDirectory.putBytes(imageByteArray).await()
        val imageUrl = result.storage.downloadUrl.await().toString()
        return saveUserInformation(user.copy(imagePath = imageUrl), false)
    }

    private suspend fun saveUserInformation(user: User, shouldRetrieveOldImage: Boolean): User {
        return fireStore.runTransaction { transition ->
            val documentRef = fireStore.collection("user").document(auth.uid!!)
            if (shouldRetrieveOldImage) {
                val currentUser = transition.get(documentRef).toObject(User::class.java)
                val newUser = user.copy(imagePath = currentUser!!.imagePath)
                transition.set(documentRef, newUser)
                newUser
            } else {
                transition.set(documentRef, user)
                user
            }
        }.await()
    }

    suspend fun addAddress(address: Address) {
        val userAddressCollection = fireStore.collection("user").document(auth.uid!!)
            .collection("address")
        userAddressCollection.add(address).await()
    }

}