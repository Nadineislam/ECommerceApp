package com.example.ecommerceapp.domain.use_case

import com.example.ecommerceapp.data.Order
import com.example.ecommerceapp.domain.repository.ShoppingRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacingOrderUseCase @Inject constructor(private val repository: ShoppingRepository) {
    suspend operator fun invoke(order: Order) {
        repository.placeOrder(order)
    }
}