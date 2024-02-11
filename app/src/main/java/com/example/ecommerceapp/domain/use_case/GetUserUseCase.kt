package com.example.ecommerceapp.domain.use_case

import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.domain.repository.UserSettingsRepository
import com.example.ecommerceapp.login_register_feature.data.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetUserUseCase @Inject constructor(private val repository: UserSettingsRepository) {
    suspend operator fun invoke(): Resource<User> =
        repository.getUser()
}