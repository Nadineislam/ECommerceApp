package com.example.ecommerceapp.presentation.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.login_register_feature.data.model.User
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.domain.use_case.GetUserUseCase
import com.example.ecommerceapp.domain.use_case.UserUpdatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAccountViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val userUpdatesUseCase: UserUpdatesUseCase,
    app: Application
) : AndroidViewModel(app) {

    private val _user = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val user = _user.asStateFlow()

    private val _updateInfo = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val updateInfo = _updateInfo.asStateFlow()

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            _user.emit(Resource.Loading())
            _user.emit(getUserUseCase())
        }
    }

    fun updateUser(user: User, imageUri: Uri?) {
        viewModelScope.launch {
            _updateInfo.emit(userUpdatesUseCase(user, imageUri))
        }
    }
}