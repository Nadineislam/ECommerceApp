package com.example.ecommerceapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ecommerceapp.data.Category
import com.example.ecommerceapp.domain.use_case.BestProductsUseCase
import com.example.ecommerceapp.domain.use_case.OfferProductsUseCase

class CategoryViewModelFactory(
    private val category: Category,
    private val offerProductsUseCase: OfferProductsUseCase,
    private val bestProductsUseCase: BestProductsUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryViewModel(category, offerProductsUseCase, bestProductsUseCase) as T
    }

}