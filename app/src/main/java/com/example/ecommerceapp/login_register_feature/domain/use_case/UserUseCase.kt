package com.example.ecommerceapp.login_register_feature.domain.use_case

import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.login_register_feature.data.model.User
import com.example.ecommerceapp.login_register_feature.domain.repository.LoginRegisterRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserUseCase @Inject constructor(private val repository: LoginRegisterRepository) {
    suspend operator fun invoke(user: User, password: String): Resource<User> =
        repository.createUserWithEmailAndPassword(user, password)

}