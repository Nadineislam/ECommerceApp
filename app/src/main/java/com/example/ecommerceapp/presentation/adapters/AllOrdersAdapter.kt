package com.example.ecommerceapp.presentation.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.Order
import com.example.ecommerceapp.data.OrderStatus
import com.example.ecommerceapp.data.getOrderStatus
import com.example.ecommerceapp.databinding.OrderItemBinding

class AllOrdersAdapter:RecyclerView.Adapter<AllOrdersAdapter.OrdersViewHolder>() {
    inner class OrdersViewHolder( private val binding: OrderItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(order:Order){
            binding.apply {
                tvOrderId.text=order.orderId.toString()
                tvOrderDate.text=order.date
                val resources=itemView.resources
                val colorDrawable=when(getOrderStatus(order.orderStatus)){
                    is OrderStatus.Ordered->{
                        ColorDrawable(resources.getColor(R.color.g_orange_yellow))
                    }
                    is OrderStatus.Shipped->{
                        ColorDrawable(resources.getColor(R.color.g_green))
                    }
                    is OrderStatus.Returned->{
                        ColorDrawable(resources.getColor(R.color.g_red))
                    }
                    is OrderStatus.Delivered->{
                        ColorDrawable(resources.getColor(R.color.g_green))
                    }
                    is OrderStatus.Confirmed->{
                        ColorDrawable(resources.getColor(R.color.g_green))
                    }
                    is OrderStatus.Canceled->{
                        ColorDrawable(resources.getColor(R.color.g_red))
                    }
                }
                imageOrderState.setImageDrawable(colorDrawable)
            }
        }
    }
    val diffUtils=object : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.products==newItem.products
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem==newItem
        }

    }
    val differList= AsyncListDiffer(this,diffUtils)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        return OrdersViewHolder(OrderItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val orders=differList.currentList[position]
        holder.bind(orders)
        holder.itemView.setOnClickListener {
            onClick?.invoke(orders)
        }
    }

    override fun getItemCount(): Int {
        return differList.currentList.size
    }
    var onClick:((Order) -> Unit)?= null
}