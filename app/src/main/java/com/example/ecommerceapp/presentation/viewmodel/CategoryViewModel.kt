package com.example.ecommerceapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.Category
import com.example.ecommerceapp.data.Product
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.domain.use_case.BestProductsUseCase
import com.example.ecommerceapp.domain.use_case.OfferProductsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val category: Category,
    private val offerProductsUseCase: OfferProductsUseCase,
    private val bestProductsUseCase: BestProductsUseCase
) : ViewModel() {
    private val _offerProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val offerProducts = _offerProducts.asStateFlow()

    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestProducts = _bestProducts.asStateFlow()

    init {
        fetchBestProducts()
        fetchOfferProducts()
    }

    private fun fetchOfferProducts() {
        viewModelScope.launch {
            _offerProducts.emit(Resource.Loading())
            _offerProducts.emit(offerProductsUseCase(category.category))
        }
    }

    private fun fetchBestProducts() {
        viewModelScope.launch {
            _bestProducts.emit(Resource.Loading())
            _bestProducts.emit(bestProductsUseCase(category.category))
        }
    }

}