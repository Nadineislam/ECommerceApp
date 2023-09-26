package com.example.ecommerceapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.Product
import com.example.ecommerceapp.data.repository.ShoppingRepository
import com.example.ecommerceapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(private val shoppingRepository: ShoppingRepository) :
    ViewModel() {
    private val _specialProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val specialProducts: StateFlow<Resource<List<Product>>> = _specialProducts

    private val _bestDealsProducts =
        MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestDealsProducts: StateFlow<Resource<List<Product>>> = _bestDealsProducts

    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestProducts: StateFlow<Resource<List<Product>>> = _bestProducts

    init {
        getSpecialProducts()
        getBestDealsProducts()
        getBestProducts()
    }

    fun getBestProducts() {
        viewModelScope.launch {
            _bestProducts.value = Resource.Loading()
            try {
                val bestProductsList = shoppingRepository.getBestProducts()
                _bestProducts.value = Resource.Success(bestProductsList)
            } catch (e: Exception) {
                _bestProducts.value = Resource.Error(e.message.toString())
            }
        }
    }

    private fun getBestDealsProducts() {
        viewModelScope.launch {
            _bestDealsProducts.value = Resource.Loading()
            try {
                val bestDealsProductsList = shoppingRepository.getBestDealsProducts()
                _bestDealsProducts.value = Resource.Success(bestDealsProductsList)
            } catch (e: Exception) {
                _bestDealsProducts.value = Resource.Error(e.message.toString())
            }
        }
    }

    private fun getSpecialProducts() {
        viewModelScope.launch {
            _specialProducts.value = Resource.Loading()
            try {
                val specialProductsList = shoppingRepository.getSpecialProducts()
                _specialProducts.value = Resource.Success(specialProductsList)
            } catch (e: Exception) {
                _specialProducts.value = Resource.Error(e.message.toString())
            }
        }
    }
}
