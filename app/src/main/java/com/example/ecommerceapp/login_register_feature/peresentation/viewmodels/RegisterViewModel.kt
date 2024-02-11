package com.example.ecommerceapp.login_register_feature.peresentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.core.utils.RegisterFieldState
import com.example.ecommerceapp.core.utils.RegisterValidation
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.core.utils.validateEmail
import com.example.ecommerceapp.core.utils.validatePassword
import com.example.ecommerceapp.login_register_feature.data.model.User
import com.example.ecommerceapp.login_register_feature.data.repository.LoginRegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerRepository: LoginRegisterRepository):ViewModel() {
    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val register: Flow<Resource<User>> = _register
    private val _validation= Channel<RegisterFieldState>()
    val validation=_validation.receiveAsFlow()
    fun createAccountWithUserAndPassword(user: User, password: String) = viewModelScope.launch {
        if (checkValidation(user, password)) {
            _register.emit(Resource.Loading())
            val result = registerRepository.createUserWithEmailAndPassword(user, password)
            _register.emit(result)
        } else {
            val registerFieldState = RegisterFieldState(validateEmail(user.email), validatePassword(password))
            _validation.send(registerFieldState)
        }
    }
//    fun createAccountWithUserAndPassword(user: User, password: String) = viewModelScope.launch {
//       if( checkValidation(user, password)){
//
//        _register.emit(Resource.Loading())
//        firebaseAuth.createUserWithEmailAndPassword(user.email, password).addOnSuccessListener {
//            it.user?.let {
//                saveUserInfo(it.uid,user)
//            }
//
//        }.addOnFailureListener {
//            _register.value = Resource.Error(it.message.toString())
//        }}else{
//            val registerFieldState=RegisterFieldState(validateEmail(user.email), validatePassword(password))
//           _validation.send(registerFieldState)
//        }
//
//    }
//
//    private fun saveUserInfo(userUId: String,user: User) {
//  db.collection(USER_COLLECTION).document(userUId).set(user).addOnSuccessListener {
//      _register.value = Resource.Success(user)
//  }.addOnFailureListener {
//      _register.value = Resource.Error(it.message.toString())
//  }
//    }

    private fun checkValidation(user: User, password: String): Boolean {
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)
        return (emailValidation is RegisterValidation.Success
                && passwordValidation is RegisterValidation.Success)
    }
}
