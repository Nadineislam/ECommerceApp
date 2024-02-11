package com.example.ecommerceapp.domain.use_case

import com.example.ecommerceapp.data.CartProduct
import com.example.ecommerceapp.domain.repository.CartRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartProductUseCase @Inject constructor(private val repository: CartRepository) {
    suspend operator fun invoke(cartProduct: CartProduct): CartProduct =
        repository.addOrUpdateCartProduct(cartProduct)
}