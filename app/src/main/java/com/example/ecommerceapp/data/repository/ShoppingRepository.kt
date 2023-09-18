package com.example.ecommerceapp.data.repository

import com.example.ecommerceapp.data.Product
import com.example.ecommerceapp.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ShoppingRepository(private val fireStore: FirebaseFirestore) {
    suspend fun fetchOfferProducts(category: String): Resource<List<Product>> {
        return try {
            val querySnapshot = fireStore.collection("Products")
                .whereEqualTo("category", category)
                .get().await()

            val products = querySnapshot.toObjects(Product::class.java)
            Resource.Success(products)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
    suspend fun fetchBestProducts(category: String): Resource<List<Product>> {
        return try {
            val querySnapshot = fireStore.collection("Products")
                .whereEqualTo("category", category)
                .whereEqualTo("offerPercentage", null)
                .get().await()

            val products = querySnapshot.toObjects(Product::class.java)
            Resource.Success(products)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
}