package com.example.ecommerceapp.firebase

import com.example.ecommerceapp.data.CartProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseCommon(private val fireStore: FirebaseFirestore,private val firebaseAuth: FirebaseAuth) {
    private val cartCollection=fireStore.collection("user").document(firebaseAuth.uid.toString())
        .collection("cart")
    fun addProductToCart(cartProduct: CartProduct, onResult: (CartProduct?, Exception?)->Unit){
        cartCollection.document().set(cartProduct).addOnSuccessListener {
            onResult(cartProduct,null)
        }.addOnFailureListener {
            onResult(null,it)
        }
    }
fun increaseQuantity(documentId:String,onResult: (String?, Exception?) -> Unit){
    fireStore.runTransaction { transition->
        val documentReference=cartCollection.document(documentId)
        val document=transition.get(documentReference)
        val productObject=document.toObject(CartProduct::class.java)
        productObject?.let { cartProduct ->
            val newQuantity = cartProduct.quantity + 1
            val newProductObject = cartProduct.copy(quantity = newQuantity)
            transition.set(documentReference, newProductObject)
        }

    }.addOnSuccessListener {
        onResult(documentId,null)
    }.addOnFailureListener {
        onResult(null,it)
    }
}
    fun decreaseQuantity(documentId:String,onResult: (String?, Exception?) -> Unit){
        fireStore.runTransaction { transition->
            val documentReference=cartCollection.document(documentId)
            val document=transition.get(documentReference)
            val productObject=document.toObject(CartProduct::class.java)
            productObject?.let { cartProduct ->
                val newQuantity = cartProduct.quantity - 1
                val newProductObject = cartProduct.copy(quantity = newQuantity)
                transition.set(documentReference, newProductObject)
            }

        }.addOnSuccessListener {
            onResult(documentId,null)
        }.addOnFailureListener {
            onResult(null,it)
        }
    }
    enum class QuantityChanges{INCREASE,DECREASE}

}