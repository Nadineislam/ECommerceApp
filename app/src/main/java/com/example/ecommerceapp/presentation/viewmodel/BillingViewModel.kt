package com.example.ecommerceapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.Address
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.domain.use_case.UserAddressesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(
    private val userAddressesUseCase: UserAddressesUseCase
) : ViewModel() {

    private val _address = MutableStateFlow<Resource<List<Address>>>(Resource.Unspecified())
    val address = _address.asStateFlow()

    init {
        getUserAddresses()
    }

    private fun getUserAddresses() {
        viewModelScope.launch {
            _address.emit(Resource.Loading())
            try {
                _address.emit(Resource.Success(userAddressesUseCase()))
            } catch (e: Exception) {
                _address.emit(Resource.Error(e.message.toString()))
            }
        }
    }


}
