package com.example.ecommerceapp.presentation.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.presentation.adapters.AllOrdersAdapter
import com.example.ecommerceapp.databinding.FragmentOrdersBinding
import com.example.ecommerceapp.utils.Resource
import com.example.ecommerceapp.presentation.viewmodel.AllOrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrdersFragment :Fragment(){
    private lateinit var binding: FragmentOrdersBinding
    private val ordersViewModel by viewModels<AllOrdersViewModel> ()
    private val ordersAdapter by lazy { AllOrdersAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentOrdersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpOrdersRv()
        binding.imageCloseOrders.setOnClickListener {
            findNavController().navigateUp()
        }
        lifecycleScope.launch {
            ordersViewModel.allOrders.collectLatest {
                when(it){
                    is Resource.Loading -> {
                        binding.progressbarAllOrders.visibility=View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressbarAllOrders.visibility=View.INVISIBLE
                        ordersAdapter.differList.submitList(it.data)
                        if(it.data.isNullOrEmpty()){
                            binding.tvEmptyOrders.visibility=View.VISIBLE
                        }
                    }
                    is Resource.Error -> {
                        binding.progressbarAllOrders.visibility=View.INVISIBLE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }
        ordersAdapter.onClick={
            val action=OrdersFragmentDirections.actionOrdersFragmentToOrderDetailFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun setUpOrdersRv() {
        binding.rvAllOrders.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter=ordersAdapter
        }
    }
}