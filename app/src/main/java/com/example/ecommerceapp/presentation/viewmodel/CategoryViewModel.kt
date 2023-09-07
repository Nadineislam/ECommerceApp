package com.example.ecommerceapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.Category
import com.example.ecommerceapp.data.Product
import com.example.ecommerceapp.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryViewModel (private val fireStore: FirebaseFirestore,private val category: Category):ViewModel() {
    private val _offerProducts=MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val offerProducts:StateFlow<Resource<List<Product>>> = _offerProducts

    private val _bestProducts=MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestProducts:StateFlow<Resource<List<Product>>> = _bestProducts
    init {
        fetchBestProducts()
        fetchOfferProducts()
    }

     private fun fetchOfferProducts(){
        viewModelScope.launch {
            _offerProducts.emit(Resource.Loading())
        }
        fireStore.collection("Products").whereEqualTo("category",category.category)
            .get().addOnSuccessListener {
                val products=it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _offerProducts.emit(Resource.Success(products))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _offerProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }
     private fun fetchBestProducts(){
        viewModelScope.launch {
            _bestProducts.emit(Resource.Loading())
        }
        fireStore.collection("Products").whereEqualTo("category",category.category)
            .whereEqualTo("offerPercentage",null).get().addOnSuccessListener {
                val products=it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Success(products))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }

}