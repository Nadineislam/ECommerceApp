package com.example.ecommerceapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.Address
import com.example.ecommerceapp.data.repository.ShoppingRepository
import com.example.ecommerceapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(
    private val shoppingRepository: ShoppingRepository
) : ViewModel() {

    private val _address = MutableStateFlow<Resource<List<Address>>>(Resource.Unspecified())
    val address: StateFlow<Resource<List<Address>>> = _address

    init {
        getUserAddresses()
    }

    private fun getUserAddresses() {
        viewModelScope.launch {
            _address.value = Resource.Loading()
            try {
                val addresses = shoppingRepository.getUserAddresses()
                _address.value = Resource.Success(addresses)
            } catch (e: Exception) {
                _address.value = Resource.Error(e.message.toString())
            }
        }
    }


}
