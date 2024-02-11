package com.example.ecommerceapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.Address
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.domain.use_case.AddressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val addressUseCase: AddressUseCase
) : ViewModel() {
    private val _addNewAddress = MutableStateFlow<Resource<Address>>(Resource.Unspecified())
    val addNewAddress = _addNewAddress.asStateFlow()
    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()
    fun addAddress(address: Address) {
        val validateInputs = validateInputs(address)
        if (validateInputs) {
            viewModelScope.launch {
                _addNewAddress.emit(Resource.Loading())
                try {
                    addressUseCase(address)
                    _addNewAddress.emit(Resource.Success(address))
                } catch (e: Exception) {
                    _addNewAddress.emit(Resource.Error(e.message.toString()))
                }
            }
        } else {
            viewModelScope.launch {
                _error.emit("All fields are required!")
            }
        }
    }

    private fun validateInputs(address: Address): Boolean {
        return address.addressTitle.trim().isNotEmpty() && address.city.trim().isNotEmpty() &&
                address.phone.trim().isNotEmpty() && address.state.trim().isNotEmpty() &&
                address.fullName.trim().isNotEmpty() && address.street.trim().isNotEmpty()
    }
}