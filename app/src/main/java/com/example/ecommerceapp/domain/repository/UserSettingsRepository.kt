package com.example.ecommerceapp.domain.repository

import android.net.Uri
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.data.Address
import com.example.ecommerceapp.login_register_feature.data.model.User

interface UserSettingsRepository {
    suspend fun getUser(): Resource<User>
    suspend fun updateUser(user: User, imageUri: Uri?): Resource<User>
    suspend fun getUser(userId: String): User
    suspend fun addAddress(address: Address)
}