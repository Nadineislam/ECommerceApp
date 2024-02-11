package com.example.ecommerceapp.login_register_feature.peresentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.login_register_feature.domain.use_case.LoginUseCase
import com.example.ecommerceapp.login_register_feature.domain.use_case.ResetPasswordUseCase
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase
) :
    ViewModel() {
    private val _login = MutableSharedFlow<Resource<FirebaseUser>>()
    val login = _login.asSharedFlow()
    private val _resetPassword = MutableSharedFlow<Resource<String>>()
    val resetPassword = _resetPassword.asSharedFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _login.emit(Resource.Loading())
            _login.emit(loginUseCase(email, password))
        }
    }


    fun resetPassword(email: String) {
        viewModelScope.launch {
            _resetPassword.emit(Resource.Loading())
            _resetPassword.emit(resetPasswordUseCase(email))
        }
    }
}