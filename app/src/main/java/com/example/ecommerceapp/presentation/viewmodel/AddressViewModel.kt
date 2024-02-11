package com.example.ecommerceapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.Address
import com.example.ecommerceapp.data.repository.UserSettingsRepository
import com.example.ecommerceapp.core.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {
    private val _addNewAddress = MutableStateFlow<Resource<Address>>(Resource.Unspecified())
    val addNewAddress: StateFlow<Resource<Address>> = _addNewAddress
    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error
    fun addAddress(address: Address) {
        val validateInputs = validateInputs(address)
        if (validateInputs) {
            viewModelScope.launch {
                _addNewAddress.value = Resource.Loading()
                try {
                    userSettingsRepository.addAddress(address)
                    _addNewAddress.value = Resource.Success(address)
                } catch (e: Exception) {
                    _addNewAddress.value = Resource.Error(e.message.toString())
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