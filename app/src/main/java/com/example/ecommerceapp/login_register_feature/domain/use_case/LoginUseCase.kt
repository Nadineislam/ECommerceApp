package com.example.ecommerceapp.login_register_feature.domain.use_case

import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.login_register_feature.domain.repository.LoginRegisterRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginUseCase @Inject constructor(private val repository: LoginRegisterRepository){
    suspend operator fun invoke(email: String, password: String): Resource<FirebaseUser> =
        repository.login(email,password)
}