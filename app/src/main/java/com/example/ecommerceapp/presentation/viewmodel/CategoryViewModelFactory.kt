package com.example.ecommerceapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ecommerceapp.data.Category
import com.example.ecommerceapp.data.repository.ShoppingRepository
import com.google.firebase.firestore.FirebaseFirestore

class CategoryViewModelFactory(
    private val fireStore: FirebaseFirestore,
    private val category: Category,
    private val shoppingRepository: ShoppingRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryViewModel(fireStore, category, shoppingRepository) as T
    }

}