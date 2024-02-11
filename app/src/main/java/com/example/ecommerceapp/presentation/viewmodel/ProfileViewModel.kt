package com.example.ecommerceapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.repository.UserSettingsRepository
import com.example.ecommerceapp.login_register_feature.data.model.User
import com.example.ecommerceapp.core.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
    private val auth: FirebaseAuth
) : ViewModel() {
    private val _user = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val user: StateFlow<Resource<User>> = _user

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            _user.value = Resource.Loading()
            try {
                val user = userSettingsRepository.getUser(auth.uid!!)
                _user.value = Resource.Success(user)
            } catch (e: Exception) {
                _user.value = Resource.Error(e.message.toString())
            }
        }
    }

    fun logOut() {
        userSettingsRepository.logOut()
    }

}