package com.example.ecommerceapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.loginRegister.data.model.User
import com.example.ecommerceapp.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {
    private val _user = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val user: StateFlow<Resource<User>> = _user

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch { _user.emit(Resource.Loading()) }
        fireStore.collection("user").document(auth.uid!!).addSnapshotListener { value, error ->
            if (error != null) {
                viewModelScope.launch { _user.emit(Resource.Error(error.message.toString())) }
            } else {
                val user = value?.toObject(User::class.java)
                user?.let {
                    viewModelScope.launch { _user.emit(Resource.Success(user)) }
                }
            }

        }
    }

    fun logOut() {
        auth.signOut()
    }

}