package com.example.ecommerceapp.data.repository

import com.example.ecommerceapp.data.Address
import com.example.ecommerceapp.data.Order
import com.example.ecommerceapp.data.Product
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.domain.repository.ShoppingRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ShoppingRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val auth: FirebaseAuth
) :ShoppingRepository{
   override suspend fun fetchOfferProducts(category: String): Resource<List<Product>> {
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

    override suspend fun fetchBestProducts(category: String): Resource<List<Product>> {
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

   override suspend fun getBestProducts(): List<Product> {
        val pagingInfo = PagingInfo()
        val productsCollection = fireStore.collection("Products")
        val querySnapshot = productsCollection.limit(pagingInfo.bestProductsPage * 9).get().await()
        val bestProductsList = querySnapshot.toObjects(Product::class.java)
        pagingInfo.isPagingEnd = bestProductsList == pagingInfo.oldBesProducts
        pagingInfo.oldBesProducts = bestProductsList
        pagingInfo.bestProductsPage++
        return bestProductsList
    }

   override suspend fun getBestDealsProducts(): List<Product> {
        val productsCollection = fireStore.collection("Products")
        val querySnapshot = productsCollection.whereEqualTo("category", "Collections").get().await()
        return querySnapshot.toObjects(Product::class.java)
    }

    override suspend fun getSpecialProducts(): List<Product> {
        val productsCollection = fireStore.collection("Products")
        val querySnapshot = productsCollection.whereEqualTo("category", "Chair").get().await()
        return querySnapshot.toObjects(Product::class.java)
    }
    override suspend fun getAllOrders(): List<Order> {
        val querySnapshot = fireStore.collection("user")
            .document(auth.uid!!)
            .collection("orders")
            .get()
            .await()
        return querySnapshot.toObjects(Order::class.java)
    }
    override suspend fun getUserAddresses(): List<Address> {
        val querySnapshot = fireStore.collection("user")
            .document(auth.uid!!)
            .collection("address")
            .get()
            .await()
        return querySnapshot.toObjects(Address::class.java)
    }
   override suspend fun placeOrder(order: Order) {
        val batch = fireStore.batch()

        val userOrderRef = fireStore.collection("user")
            .document(auth.uid!!)
            .collection("orders")
            .document()
        batch.set(userOrderRef, order)

        val ordersRef = fireStore.collection("orders")
            .document()
        batch.set(ordersRef, order)

        val cartQuerySnapshot = fireStore.collection("user")
            .document(auth.uid!!)
            .collection("cart")
            .get()
            .await()
        val cartDocuments = cartQuerySnapshot.documents
        cartDocuments.forEach { document ->
            batch.delete(document.reference)
        }

        batch.commit().await()
    }
    internal data class PagingInfo(
        var bestProductsPage: Long = 1,
        var oldBesProducts: List<Product> = emptyList(),
        var isPagingEnd: Boolean = false
    )
}