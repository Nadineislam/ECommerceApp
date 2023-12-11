package com.example.ecommerceapp.presentation.fragments.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.ecommerceapp.data.Category
import com.example.ecommerceapp.data.repository.ShoppingRepository
import com.example.ecommerceapp.utils.Resource
import com.example.ecommerceapp.presentation.viewmodel.CategoryViewModel
import com.example.ecommerceapp.presentation.viewmodel.CategoryViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class CupboardFragment:BaseCategoryFragment() {
    @Inject
    lateinit var fireStore: FirebaseFirestore
    @Inject
    lateinit var auth: FirebaseAuth
    private val shoppingRepository:ShoppingRepository by lazy { ShoppingRepository(fireStore,auth) }
    private val viewModel by viewModels<CategoryViewModel> {
        CategoryViewModelFactory(fireStore, Category.Cupboard,shoppingRepository)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
            viewModel.offerProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        offerAdapter.differList.submitList(it.data)
                    }

                    is Resource.Error -> {
                        Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG)
                            .show()
                    }

                    else -> Unit
                }
            }
            }
        }
        lifecycleScope.launch {
            viewModel.bestProducts.collectLatest {
                when(it){
                    is Resource.Loading->{}
                    is Resource.Success->{
                        bestProductsAdapter.differList.submitList(it.data)
                    }
                    is Resource.Error->{
                        Snackbar.make(requireView(),it.message.toString(), Snackbar.LENGTH_LONG).show()
                    }
                    else-> Unit
                }

            }
        }
    }
}