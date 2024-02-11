package com.example.ecommerceapp.domain.use_case

import android.net.Uri
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.domain.repository.UserSettingsRepository
import com.example.ecommerceapp.login_register_feature.data.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserUpdatesUseCase @Inject constructor(private val repository: UserSettingsRepository) {
    suspend operator fun invoke(user: User, imageUri: Uri?): Resource<User> =
        repository.updateUser(user,imageUri)
}