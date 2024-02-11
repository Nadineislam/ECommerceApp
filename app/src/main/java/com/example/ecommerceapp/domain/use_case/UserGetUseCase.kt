package com.example.ecommerceapp.domain.use_case

import com.example.ecommerceapp.domain.repository.UserSettingsRepository
import com.example.ecommerceapp.login_register_feature.data.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserGetUseCase @Inject constructor(private val repository:UserSettingsRepository) {
    suspend operator fun invoke(userId: String): User =
        repository.getUser(userId)
}