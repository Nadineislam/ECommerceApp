package com.example.ecommerceapp.presentation.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.presentation.adapters.BillingProductsAdapter
import com.example.ecommerceapp.data.OrderStatus
import com.example.ecommerceapp.data.getOrderStatus
import com.example.ecommerceapp.databinding.FragmentOrderDetailBinding
import com.example.ecommerceapp.utils.VerticalItemDecoration

class OrderDetailFragment:Fragment() {
    private lateinit var binding:FragmentOrderDetailBinding
    private val billingProductsAdapter by lazy{BillingProductsAdapter()}
    private val args by navArgs<OrderDetailFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentOrderDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val order=args.order
        setUpOrderRv()
        binding.apply {
            tvOrderId.text="Order #${order.orderId}"
            stepView.setSteps(mutableListOf(
                OrderStatus.Ordered.status,
                OrderStatus.Confirmed.status,
                OrderStatus.Shipped.status,
                OrderStatus.Delivered.status

            ))
            val currentOrderStatus=when(getOrderStatus(order.orderStatus)){
                is OrderStatus.Ordered->0
                is OrderStatus.Confirmed->1
                is OrderStatus.Shipped->2
                is OrderStatus.Delivered->3
                else->-1
            }
            stepView.go(currentOrderStatus,false)
            if(currentOrderStatus==3)
                stepView.done(true)
            tvFullName.text=order.address.fullName
            tvAddress.text="${order.address.street} ${order.address.city}"
            tvPhoneNumber.text=order.address.phone
            tvTotalPrice.text="$ ${order.totalPrice}"

        }
        billingProductsAdapter.differList.submitList(order.products)
    }

    private fun setUpOrderRv() {
        binding.rvProducts.apply {
            layoutManager= LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
            adapter=billingProductsAdapter
            addItemDecoration(VerticalItemDecoration())
        }
    }
}