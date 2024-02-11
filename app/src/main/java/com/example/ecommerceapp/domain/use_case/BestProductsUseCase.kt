package com.example.ecommerceapp.domain.use_case

import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.data.Product
import com.example.ecommerceapp.domain.repository.ShoppingRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BestProductsUseCase @Inject constructor(private val repository: ShoppingRepository) {
    suspend operator fun invoke(category: String): Resource<List<Product>> =
        repository.fetchBestProducts(category)
}