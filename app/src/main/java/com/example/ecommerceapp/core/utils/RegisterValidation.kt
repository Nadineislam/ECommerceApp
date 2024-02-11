package com.example.ecommerceapp.core.utils

sealed class RegisterValidation{
    object Success: RegisterValidation()
    class Failed(val message:String): RegisterValidation()
}
data class RegisterFieldState(val email: RegisterValidation, val password: RegisterValidation)
