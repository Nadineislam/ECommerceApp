package com.example.ecommerceapp.core.utils

import android.util.Patterns

fun validateEmail(email:String): RegisterValidation {
    if(email.isEmpty())
        return RegisterValidation.Failed("email cannot be empty")
    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return RegisterValidation.Failed("Wrong email format")
    return RegisterValidation.Success

}
fun validatePassword(password:String): RegisterValidation {
    if(password.isEmpty())
        return RegisterValidation.Failed("password cannot be empty")
    if(password.length<6)
        return RegisterValidation.Failed("password should contains 6 chars")
    return RegisterValidation.Success

}