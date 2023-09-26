package com.example.ecommerceapp.data.repository

import com.example.ecommerceapp.data.Product
import com.example.ecommerceapp.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ShoppingRepository @Inject constructor(private val fireStore: FirebaseFirestore) {
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

    suspend fun getBestProducts(): List<Product> {
        val pagingInfo = PagingInfo()
        val productsCollection = fireStore.collection("Products")
        val querySnapshot = productsCollection.limit(pagingInfo.bestProductsPage * 9).get().await()
        val bestProductsList = querySnapshot.toObjects(Product::class.java)
        pagingInfo.isPagingEnd = bestProductsList == pagingInfo.oldBesProducts
        pagingInfo.oldBesProducts = bestProductsList
        pagingInfo.bestProductsPage++
        return bestProductsList
    }

    suspend fun getBestDealsProducts(): List<Product> {
        val productsCollection = fireStore.collection("Products")
        val querySnapshot = productsCollection.whereEqualTo("category", "Collections").get().await()
        return querySnapshot.toObjects(Product::class.java)
    }

    suspend fun getSpecialProducts(): List<Product> {
        val productsCollection = fireStore.collection("Products")
        val querySnapshot = productsCollection.whereEqualTo("category", "Chair").get().await()
        return querySnapshot.toObjects(Product::class.java)
    }

    internal data class PagingInfo(
        var bestProductsPage: Long = 1,
        var oldBesProducts: List<Product> = emptyList(),
        var isPagingEnd: Boolean = false
    )
}