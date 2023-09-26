package com.example.ecommerceapp.data.repository

import com.example.ecommerceapp.data.CartProduct
import com.example.ecommerceapp.firebase.FirebaseCommon
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CartRepository @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val fireBaseCommon: FirebaseCommon
) {
    suspend fun addOrUpdateCartProduct(cartProduct: CartProduct): CartProduct {
        val documentSnapshot = fireStore.collection("user")
            .document(firebaseAuth.uid.toString())
            .collection("cart")
            .whereEqualTo("product.id", cartProduct.product.id)
            .get().await()

        return if (documentSnapshot.isEmpty) {
            addNewProduct(cartProduct)
        } else {
            val product = documentSnapshot.documents.first().toObject(CartProduct::class.java)
            if (product == cartProduct) {
                val documentId = documentSnapshot.documents.first().id
                increaseQuantity(documentId, cartProduct)
            } else {
                addNewProduct(cartProduct)
            }
        }
    }

    private suspend fun addNewProduct(cartProduct: CartProduct): CartProduct {
        return suspendCoroutine { continuation ->
            fireBaseCommon.addProductToCart(cartProduct) { addedProduct, exception ->
                if (exception != null) {
                    continuation.resumeWithException(exception)
                } else {
                    if (addedProduct != null) {
                        continuation.resume(addedProduct)
                    } else {
                        continuation.resumeWithException(Exception("Failed to add product to cart."))
                    }
                }
            }
        }
    }

    private suspend fun increaseQuantity(documentId: String, cartProduct: CartProduct): CartProduct {
        return suspendCoroutine { continuation ->
            fireBaseCommon.increaseQuantity(documentId) { updatedDocumentId, exception ->
                if (exception != null) {
                    continuation.resumeWithException(exception)
                } else {
                    if (updatedDocumentId != null) {
                        continuation.resume(cartProduct)
                    } else {
                        continuation.resumeWithException(Exception("Failed to increase quantity for documentId: $documentId"))
                    }
                }
            }
        }
    }
}