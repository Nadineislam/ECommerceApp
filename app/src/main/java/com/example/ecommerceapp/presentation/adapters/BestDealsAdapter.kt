package com.example.ecommerceapp.presentation.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerceapp.data.Product
import com.example.ecommerceapp.databinding.BestDealsRvItemBinding

class BestDealsAdapter:RecyclerView.Adapter<BestDealsAdapter.BestDealsViewHolder>() {
    inner class BestDealsViewHolder(private val binding:BestDealsRvItemBinding): RecyclerView.ViewHolder(binding.root){
       fun bind(product: Product){
           binding.apply {
               Glide.with(itemView).load(product.images[0]).into(binding.imgBestDeal)
               tvDealProductName.text=product.name
               tvOldPrice.text=product.price.toString()
               product.offerPercentage?.let {
                   val remainingPricePercentage=1f-it
                   val priceAfterOffer=remainingPricePercentage*product.price
                   tvNewPrice.text= String.format("%.2f",priceAfterOffer)
               //    "${String.format("%.2f",priceAfterOffer)}"
                   tvOldPrice.paintFlags= Paint.STRIKE_THRU_TEXT_FLAG

               }
           }
       }
    }
    val diffUtils=object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem==newItem
        }

    }
    val differList= AsyncListDiffer(this,diffUtils)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestDealsViewHolder {
        return BestDealsViewHolder(BestDealsRvItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: BestDealsViewHolder, position: Int) {
        val products=differList.currentList[position]
        holder.bind(products)
        holder.itemView.setOnClickListener {
            onClick?.invoke(products)
        }
    }

    override fun getItemCount(): Int {
        return differList.currentList.size
    }

    var onClick: ((Product) -> Unit)? = null
}