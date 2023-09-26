package com.example.ecommerceapp.presentation.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.repository.UserSettingsRepository
import com.example.ecommerceapp.loginRegister.data.model.User
import com.example.ecommerceapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAccountViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
    app: Application
) : AndroidViewModel(app) {

    private val _user = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val user: StateFlow<Resource<User>> = _user

    private val _updateInfo = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val updateInfo: StateFlow<Resource<User>> = _updateInfo

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            _user.value = Resource.Loading()
            val result = userSettingsRepository.getUser()
            _user.value = result
        }
    }

    fun updateUser(user: User, imageUri: Uri?) {
        viewModelScope.launch {
            val result = userSettingsRepository.updateUser(user, imageUri)
            _updateInfo.value = result
        }
    }
}