package com.example.ecommerceapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.Category
import com.example.ecommerceapp.data.Product
import com.example.ecommerceapp.data.repository.ShoppingRepository
import com.example.ecommerceapp.core.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val fireStore: FirebaseFirestore,
    private val category: Category,
    private val shoppingRepository: ShoppingRepository
) : ViewModel() {
    private val _offerProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val offerProducts: StateFlow<Resource<List<Product>>> = _offerProducts

    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestProducts: StateFlow<Resource<List<Product>>> = _bestProducts

    init {
        fetchBestProducts()
        fetchOfferProducts()
    }

    private fun fetchOfferProducts() {
        viewModelScope.launch {
            _offerProducts.emit(Resource.Loading())
            val result = shoppingRepository.fetchOfferProducts(category.category)
            _offerProducts.emit(result)
        }
    }

    private fun fetchBestProducts() {
        viewModelScope.launch {
            _bestProducts.emit(Resource.Loading())
            val result=shoppingRepository.fetchBestProducts(category.category)
            _bestProducts.emit(result)
        }
    }

}