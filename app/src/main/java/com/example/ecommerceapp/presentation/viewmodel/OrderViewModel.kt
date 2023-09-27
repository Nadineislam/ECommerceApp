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
class OrderViewModel @Inject constructor(
    private val shoppingRepository: ShoppingRepository
) : ViewModel() {
    private val _order = MutableStateFlow<Resource<Order>>(Resource.Unspecified())
    val order: StateFlow<Resource<Order>> = _order

    fun placeOrder(order: Order) {
        viewModelScope.launch {
            _order.value = Resource.Loading()
            try {
                shoppingRepository.placeOrder(order)
                _order.value = Resource.Success(order)
            } catch (e: Exception) {
                _order.value = Resource.Error(e.message.toString())
            }
        }
    }
}