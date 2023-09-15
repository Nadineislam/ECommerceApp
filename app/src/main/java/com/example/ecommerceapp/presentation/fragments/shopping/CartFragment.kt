package com.example.ecommerceapp.presentation.fragments.shopping

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.R
import com.example.ecommerceapp.presentation.adapters.CartAdapter
import com.example.ecommerceapp.databinding.FragmentCartBinding
import com.example.ecommerceapp.firebase.FirebaseCommon
import com.example.ecommerceapp.utils.Resource
import com.example.ecommerceapp.utils.VerticalItemDecoration
import com.example.ecommerceapp.presentation.viewmodel.CartViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private val cartAdapter: CartAdapter by lazy { CartAdapter() }
    private val viewModel by activityViewModels<CartViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpCartRv()
        var totalPrice=0f

        cartAdapter.onPlusClick={
           viewModel.changeQuantity(it,FirebaseCommon.QuantityChanges.INCREASE)
        }
        lifecycleScope.launch {
            viewModel.deleteDialog.collectLatest {
                val alertDialog=AlertDialog.Builder(requireContext()).apply {
                    setTitle("Delete item from cart")
                    setMessage("Do you want to delete this item from cart?")
                    setNegativeButton("cancel"){dialog,_ ->
                        dialog.dismiss()
                    }
                    setPositiveButton("Yes"){dialog,_ ->
                        viewModel.deleteCartProduct(it)
                        dialog.dismiss()


                    }
                }
                alertDialog.create()
                alertDialog.show()

            }
        }
        cartAdapter.onMinusClick={
            viewModel.changeQuantity(it,FirebaseCommon.QuantityChanges.DECREASE)
        }
        cartAdapter.onProductClick={
            val bundle=Bundle().apply { putParcelable("product",it.product) }
            findNavController().navigate(R.id.action_cartFragment_to_productDetailsFragment,bundle)
        }
        lifecycleScope.launch {
            viewModel.productPrice.collectLatest {price->
                price?.let {
                    totalPrice=it
                    binding.tvCartTotalPrice.text="$ $price"
                }

            }
        }
        binding.btnCheckout.setOnClickListener {
            val action=CartFragmentDirections.actionCartFragmentToBillingFragment(totalPrice,cartAdapter.differList.currentList.toTypedArray(),
                true)
            findNavController().navigate(action)
        }

        lifecycleScope.launch {
            viewModel.cartProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressBarCart.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBarCart.visibility = View.INVISIBLE
                        if (it.data!!.isEmpty()) {
                            showEmptyLayoutCart()
                            hideOtherViews()
                        } else {
                            hideEmptyLayoutCart()
                            showOtherViews()
                            cartAdapter.differList.submitList(it.data)
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBarCart.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun showOtherViews() {
        binding.apply {
            totalBoxContainer.visibility=View.VISIBLE
            rvCart.visibility=View.VISIBLE
            btnCheckout.visibility=View.VISIBLE
        }
    }

    private fun hideOtherViews() {
        binding.apply {
            totalBoxContainer.visibility=View.GONE
            rvCart.visibility=View.GONE
            btnCheckout.visibility=View.GONE
        }
    }

    private fun hideEmptyLayoutCart() {
        binding.emptyLayoutCart.visibility = View.INVISIBLE
    }

    private fun showEmptyLayoutCart() {
        binding.emptyLayoutCart.visibility = View.VISIBLE
    }

    private fun setUpCartRv() {
        binding.rvCart.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = cartAdapter
            addItemDecoration(VerticalItemDecoration())
        }
    }
}