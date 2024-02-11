package com.example.ecommerceapp.login_register_feature.domain.use_case

import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.login_register_feature.domain.repository.LoginRegisterRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResetPasswordUseCase@Inject constructor(private val repository: LoginRegisterRepository) {
    suspend operator fun invoke(email: String): Resource<String> =
        repository.resetPassword(email)
}