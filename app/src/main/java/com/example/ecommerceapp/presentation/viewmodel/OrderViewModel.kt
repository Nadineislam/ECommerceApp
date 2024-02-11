package com.example.ecommerceapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.Order
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.domain.use_case.PlacingOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val placeOrderUseCase: PlacingOrderUseCase
) : ViewModel() {
    private val _order = MutableStateFlow<Resource<Order>>(Resource.Unspecified())
    val order = _order.asStateFlow()

    fun placeOrder(order: Order) {
        viewModelScope.launch {
            _order.emit(Resource.Loading())
            try {
                placeOrderUseCase
                _order.emit(Resource.Success(order))
            } catch (e: Exception) {
                _order.emit(Resource.Error(e.message.toString()))
            }
        }
    }
}