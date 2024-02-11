package com.example.ecommerceapp.login_register_feature.domain.repository

import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.login_register_feature.data.model.User
import com.google.firebase.auth.FirebaseUser

interface LoginRegisterRepository {
    suspend fun login(email: String, password: String): Resource<FirebaseUser>
    suspend fun resetPassword(email: String): Resource<String>
    suspend fun createUserWithEmailAndPassword(user: User, password: String): Resource<User>
    suspend fun saveUserInfo(userUid: String, user: User)
}