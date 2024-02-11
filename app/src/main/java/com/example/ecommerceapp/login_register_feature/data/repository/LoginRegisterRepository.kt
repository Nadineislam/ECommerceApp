package com.example.ecommerceapp.login_register_feature.data.repository

import com.example.ecommerceapp.login_register_feature.data.model.User
import com.example.ecommerceapp.core.utils.Constants.USER_COLLECTION
import com.example.ecommerceapp.core.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException
import javax.inject.Inject

class LoginRegisterRepository @Inject constructor(private val firebaseAuth: FirebaseAuth, private val db: FirebaseFirestore) {

    suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                Resource.Success(user)
            } else {
                Resource.Error("Login failed. User not found.")
            }
        } catch (e: Exception) {
            if(e is CancellationException){
                throw e
            }
            Resource.Error(e.message.toString())
        }
    }

    suspend fun resetPassword(email: String): Resource<String> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Resource.Success(email)
        } catch (e: Exception) {
            if(e is CancellationException){
                throw e
            }
            Resource.Error(e.message.toString())
        }
    }
    suspend fun createUserWithEmailAndPassword(user: User, password: String): Resource<User> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(user.email, password).await()
            val userUid = authResult.user?.uid
            coroutineScope {
            if (userUid != null) {
                saveUserInfo(userUid, user)
                Resource.Success(user)
            } else {
                Resource.Error("Failed to create user.")
            }}
        } catch (e: Exception) {
            if(e is CancellationException){
                throw e
            }
            Resource.Error(e.message.toString())
        }
    }

    private suspend fun saveUserInfo(userUid: String, user: User) {
        try {
            db.collection(USER_COLLECTION).document(userUid).set(user).await()
        } catch (e: Exception) {
            if(e is CancellationException){
                throw e
            }
            throw Exception("Failed to save user info.")
        }
    }
}