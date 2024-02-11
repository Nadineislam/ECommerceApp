package com.example.ecommerceapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.login_register_feature.data.model.User
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.domain.use_case.UserGetUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userGetUseCase: UserGetUseCase,
    private val auth: FirebaseAuth
) : ViewModel() {
    private val _user = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val user = _user.asStateFlow()

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            _user.emit(Resource.Loading())
            try {
                val user = userGetUseCase(auth.uid!!)
                _user.emit( Resource.Success(user))
            } catch (e: Exception) {
                _user.emit(Resource.Error(e.message.toString()))
            }
        }
    }

    fun logOut() {
        auth.signOut()
    }

}