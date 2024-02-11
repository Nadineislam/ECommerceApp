package com.example.ecommerceapp.domain.repository

import com.example.ecommerceapp.data.CartProduct

interface CartRepository {
    suspend fun addOrUpdateCartProduct(cartProduct: CartProduct): CartProduct
    suspend fun addNewProduct(cartProduct: CartProduct): CartProduct
    suspend fun increaseQuantity(documentId: String, cartProduct: CartProduct): CartProduct
}