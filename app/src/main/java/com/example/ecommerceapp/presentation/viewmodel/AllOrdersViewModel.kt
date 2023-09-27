package com.example.ecommerceapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.Order
import com.example.ecommerceapp.data.repository.ShoppingRepository
import com.example.ecommerceapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllOrdersViewModel @Inject constructor(
    private val shoppingRepository: ShoppingRepository
) : ViewModel() {
    private val _allOrders = MutableStateFlow<Resource<List<Order>>>(Resource.Unspecified())
    val allOrders: StateFlow<Resource<List<Order>>> = _allOrders

    init {
        getAllOrders()
    }

    private fun getAllOrders() {
        viewModelScope.launch {
            _allOrders.value = Resource.Loading()
            try {
                val orders = shoppingRepository.getAllOrders()
                _allOrders.value = Resource.Success(orders)
            } catch (e: Exception) {
                _allOrders.value = Resource.Error(e.message.toString())
            }
        }
    }
}