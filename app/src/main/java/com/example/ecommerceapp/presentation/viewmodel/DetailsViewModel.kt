package com.example.ecommerceapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.CartProduct
import com.example.ecommerceapp.data.repository.CartRepository
import com.example.ecommerceapp.core.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {
    private val _addToCart = MutableStateFlow<Resource<CartProduct>>(Resource.Unspecified())
    val addToCart: StateFlow<Resource<CartProduct>> = _addToCart

    fun addUpdateCartProducts(cartProduct: CartProduct) {
        viewModelScope.launch {
            _addToCart.emit(Resource.Loading())
            try {
                val result = cartRepository.addOrUpdateCartProduct(cartProduct)
                _addToCart.emit(Resource.Success(result))
            } catch (exception: Exception) {
                _addToCart.emit(Resource.Error(exception.message.toString()))
            }
        }
    }
}