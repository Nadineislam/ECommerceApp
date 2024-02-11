package com.example.ecommerceapp.presentation.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerceapp.data.CartProduct
import com.example.ecommerceapp.databinding.BillingProductsRvItemBinding
import com.example.ecommerceapp.core.extensions.getProductPrice

class BillingProductsAdapter :
    RecyclerView.Adapter<BillingProductsAdapter.BillingProductsViewHolder>() {
    inner class BillingProductsViewHolder(val binding: BillingProductsRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(billingProduct: CartProduct) {
            binding.apply {
                Glide.with(itemView).load(billingProduct.product.images[0]).into(imageCartProduct)
                tvBillingProductQuantity.text = billingProduct.quantity.toString()
                tvProductCartName.text = billingProduct.product.name
                val priceAfterOffer =
                    billingProduct.product.offerPercentage.getProductPrice(billingProduct.product.price)
                tvProductCartPrice.text = "$ ${String.format("%.2f", priceAfterOffer)}"
                imageCartProductColor.setImageDrawable(
                    ColorDrawable(
                        billingProduct.selectedColor ?: Color.TRANSPARENT
                    )
                )
                tvCartProductSize.text = billingProduct.selectedSize ?: "".also {
                    imageCartProductSize.setImageDrawable(
                        ColorDrawable(
                            Color.TRANSPARENT
                        )
                    )
                }
            }
        }
    }

    val diffUtils = object : DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }

    }
    val differList = AsyncListDiffer(this, diffUtils)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingProductsViewHolder {
        return BillingProductsViewHolder(
            BillingProductsRvItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: BillingProductsViewHolder, position: Int) {
        val billingProduct = differList.currentList[position]
        holder.bind(billingProduct)

    }

    override fun getItemCount(): Int {
        return differList.currentList.size
    }

}