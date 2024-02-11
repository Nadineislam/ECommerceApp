package com.example.ecommerceapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.Product
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.domain.use_case.BestDealsUseCase
import com.example.ecommerceapp.domain.use_case.GettingBestProductsUseCase
import com.example.ecommerceapp.domain.use_case.SpecialProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val getBestProductsUseCase: GettingBestProductsUseCase,
    private val bestDealsUseCase: BestDealsUseCase,
    private val specialProductsUseCase: SpecialProductsUseCase
) :
    ViewModel() {
    private val _specialProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val specialProducts = _specialProducts.asStateFlow()

    private val _bestDealsProducts =
        MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestDealsProducts = _bestDealsProducts.asStateFlow()

    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestProducts = _bestProducts.asStateFlow()

    init {
        getSpecialProducts()
        getBestDealsProducts()
        getBestProducts()
    }

    fun getBestProducts() {
        viewModelScope.launch {
            _bestProducts.emit(Resource.Loading())
            try {
                _bestProducts.emit(Resource.Success(getBestProductsUseCase()))
            } catch (e: Exception) {
                _bestProducts.emit(Resource.Error(e.message.toString()))
            }
        }
    }

    private fun getBestDealsProducts() {
        viewModelScope.launch {
            _bestDealsProducts.emit(Resource.Loading())
            try {
                _bestDealsProducts.emit(Resource.Success(bestDealsUseCase()))
            } catch (e: Exception) {
                _bestDealsProducts.emit(Resource.Error(e.message.toString()))
            }
        }
    }

    private fun getSpecialProducts() {
        viewModelScope.launch {
            _specialProducts.value = Resource.Loading()
            try {
                _specialProducts.emit(Resource.Success(specialProductsUseCase()))
            } catch (e: Exception) {
                _specialProducts.emit(Resource.Error(e.message.toString()))
            }
        }
    }
}
