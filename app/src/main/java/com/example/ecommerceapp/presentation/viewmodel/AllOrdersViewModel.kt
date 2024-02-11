package com.example.ecommerceapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.Order
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.domain.use_case.AllOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllOrdersViewModel @Inject constructor(
    private val allOrdersUseCase: AllOrdersUseCase
) : ViewModel() {
    private val _allOrders = MutableStateFlow<Resource<List<Order>>>(Resource.Unspecified())
    val allOrders = _allOrders.asStateFlow()

    init {
        getAllOrders()
    }

    private fun getAllOrders() {
        viewModelScope.launch {
            _allOrders.emit( Resource.Loading())
            try {
                _allOrders.emit(Resource.Success(allOrdersUseCase()))
            } catch (e: Exception) {
                _allOrders.emit(Resource.Error(e.message.toString()))
            }
        }
    }
}