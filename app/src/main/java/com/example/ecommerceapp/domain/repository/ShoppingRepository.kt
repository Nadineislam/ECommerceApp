package com.example.ecommerceapp.domain.repository

import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.data.Address
import com.example.ecommerceapp.data.Order
import com.example.ecommerceapp.data.Product

interface ShoppingRepository {
    suspend fun fetchOfferProducts(category: String): Resource<List<Product>>
    suspend fun fetchBestProducts(category: String): Resource<List<Product>>
    suspend fun getBestProducts(): List<Product>
    suspend fun getBestDealsProducts(): List<Product>
    suspend fun getSpecialProducts(): List<Product>
    suspend fun getAllOrders(): List<Order>
    suspend fun getUserAddresses(): List<Address>
    suspend fun placeOrder(order: Order)
}