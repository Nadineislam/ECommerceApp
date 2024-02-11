package com.example.ecommerceapp.domain.use_case

import com.example.ecommerceapp.data.Order
import com.example.ecommerceapp.domain.repository.ShoppingRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AllOrdersUseCase @Inject constructor(private val repository: ShoppingRepository) {
    suspend operator fun invoke(): List<Order> =
        repository.getAllOrders()
}