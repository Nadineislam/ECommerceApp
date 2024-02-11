package com.example.ecommerceapp.domain.use_case

import com.example.ecommerceapp.data.Product
import com.example.ecommerceapp.domain.repository.ShoppingRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BestDealsUseCase @Inject constructor(private val repository: ShoppingRepository) {
    suspend operator fun invoke(): List<Product> =
        repository.getBestDealsProducts()
}