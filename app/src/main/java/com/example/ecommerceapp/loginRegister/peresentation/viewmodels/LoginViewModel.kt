package com.example.ecommerceapp.loginRegister.peresentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.loginRegister.data.repository.LoginRegisterRepository
import com.example.ecommerceapp.utils.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRegisterRepository) :
    ViewModel() {
    private val _login = MutableSharedFlow<Resource<FirebaseUser>>()
    val login = _login.asSharedFlow()
    private val _resetPassword = MutableSharedFlow<Resource<String>>()
    val resetPassword = _resetPassword.asSharedFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _login.emit(Resource.Loading())
            val result = loginRepository.login(email, password)
            _login.emit(result)
        }
    }


    fun resetPassword(email: String) {
        viewModelScope.launch {
            _resetPassword.emit(Resource.Loading())
            val result = loginRepository.resetPassword(email)
            _resetPassword.emit(result)
        }
    }

//    fun login(email: String, password: String) {
//        viewModelScope.launch {
//            _login.emit(Resource.Loading())
//        }
//        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
//            viewModelScope.launch {
//                it.user?.let {
//                    _login.emit(Resource.Success(it))
//                }
//            }
//        }.addOnFailureListener {
//            viewModelScope.launch {
//                _login.emit(Resource.Error(it.message.toString()))
//            }
//        }
//    }

//    fun resetPassword(email: String) {
//        viewModelScope.launch {
//            _resetPassword.emit(Resource.Loading())
//        }
//        firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener {
//            viewModelScope.launch {
//                _resetPassword.emit(Resource.Success(email))
//            }
//        }.addOnFailureListener {
//            viewModelScope.launch {
//                _resetPassword.emit(Resource.Error(it.message.toString()))
//            }
//        }
//
//    }
}