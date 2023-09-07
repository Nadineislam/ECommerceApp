package com.example.ecommerceapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ecommerceapp.data.Category
import com.google.firebase.firestore.FirebaseFirestore

class CategoryViewModelFactory(private val fireStore: FirebaseFirestore, private val category: Category):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryViewModel(fireStore,category) as T
    }

}