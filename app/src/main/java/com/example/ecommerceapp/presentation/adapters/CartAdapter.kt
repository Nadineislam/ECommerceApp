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
import com.example.ecommerceapp.databinding.CartProductItemBinding
import com.example.ecommerceapp.core.extensions.getProductPrice

class CartAdapter : RecyclerView.Adapter<CartAdapter.CartProductViewHolder>() {
    inner class CartProductViewHolder(val binding: CartProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cartProduct: CartProduct) {
            binding.apply {
                Glide.with(itemView).load(cartProduct.product.images[0]).into(ivCart)
                tvCartProductName.text = cartProduct.product.name
                tvCartProductPrice.text = cartProduct.product.price.toString()
                tvProductCartQuantity.text = cartProduct.quantity.toString()
                val priceAfterOffer =
                    cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price)
                tvCartProductPrice.text = "$ ${String.format("%.2f", priceAfterOffer)}"
                ivCartProductColor.setImageDrawable(
                    ColorDrawable(
                        cartProduct.selectedColor ?: Color.TRANSPARENT
                    )
                )
                tvCartProductSize.text = cartProduct.selectedSize ?: "".also {
                    ivCartProductSize.setImageDrawable(ColorDrawable(Color.TRANSPARENT))
                }
            }
        }
    }

    val diffUtils = object : DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }


    }
    val differList = AsyncListDiffer(this, diffUtils)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartProductViewHolder {
        return CartProductViewHolder(CartProductItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(
        holder: CartProductViewHolder,
        position: Int
    ) {
        val cartProducts = differList.currentList[position]
        holder.bind(cartProducts)
        holder.itemView.setOnClickListener {
            onProductClick?.invoke(cartProducts)
        }
        holder.binding.ivPlus.setOnClickListener {
            onPlusClick?.invoke(cartProducts)
        }
        holder.binding.ivMinus.setOnClickListener {
            onMinusClick?.invoke(cartProducts)
        }
    }

    override fun getItemCount(): Int {
        return differList.currentList.size
    }

    var onProductClick: ((CartProduct) -> Unit)? = null
    var onPlusClick: ((CartProduct) -> Unit)? = null
    var onMinusClick: ((CartProduct) -> Unit)? = null
}