package com.example.ecommerceapp.presentation.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.presentation.adapters.ColorsAdapter
import com.example.ecommerceapp.presentation.adapters.SizesAdapter
import com.example.ecommerceapp.presentation.adapters.ViewPager2ImagesAdapter
import com.example.ecommerceapp.data.CartProduct
import com.example.ecommerceapp.databinding.FragmentProductDetailsBinding
import com.example.ecommerceapp.utils.Resource
import com.example.ecommerceapp.utils.hideBottomNavigationView
import com.example.ecommerceapp.presentation.viewmodel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
@AndroidEntryPoint
class ProductDetailsFragment:Fragment() {
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var binding:FragmentProductDetailsBinding
    private val viewPager2Adapter:ViewPager2ImagesAdapter by lazy { ViewPager2ImagesAdapter() }
    private val sizesAdapter:SizesAdapter by lazy { SizesAdapter() }
    private val colorsAdapter: ColorsAdapter by lazy { ColorsAdapter() }
    private var selectedColor:Int?=null
    private var selectedSize:String?=null
    private val viewModel by viewModels<DetailsViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hideBottomNavigationView()
         binding=FragmentProductDetailsBinding.inflate(inflater)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val product=args.product
        sizesAdapter.onItemClick={selectedSize=it}
        colorsAdapter.onItemClick={selectedColor=it}
        binding.btnAddToCart.setOnClickListener{
            viewModel.addUpdateCartProducts(CartProduct( product,1,selectedColor,selectedSize))
        }
        lifecycleScope.launchWhenStarted {
            viewModel.addToCart.collectLatest {
                when(it){
                    is Resource.Loading->{binding.btnAddToCart.startAnimation()}
                    is Resource.Success->{binding.btnAddToCart.revertAnimation()
                        Toast.makeText(requireContext(), "Item added successfully", Toast.LENGTH_SHORT).show()}
                    is Resource.Error->{binding.btnAddToCart.revertAnimation()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()}
                    else->Unit
                }
            }
        }
        setUpColorsRv()
        setUpSizesRv()
        setUpViewPager2Rv()
        binding.apply {
            tvProductDetailsName.text=product.name
            tvProductDetailsDesc.text=product.description
            tvProductDetailsPrice.text="$ ${product.price}"
            if(product.colors.isNullOrEmpty())
                tvProductDetailsColor.visibility=View.INVISIBLE

            if(product.sizes.isNullOrEmpty())
                tvProductDetailsSize.visibility=View.INVISIBLE


        }
        binding.ivClose.setOnClickListener {
            findNavController().navigateUp()
        }
        viewPager2Adapter.differList.submitList(product.images)
        product.colors?.let {
            colorsAdapter.differList.submitList(it)
        }
        product.sizes?.let {
            sizesAdapter.differList.submitList(it)
        }

    }

    private fun setUpColorsRv() {
        binding.rvColors.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter=colorsAdapter
        }
        }
    private fun setUpSizesRv() {
        binding.rvSizes.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter=sizesAdapter
        }
    }
    private fun setUpViewPager2Rv() {
        binding.apply {
            viewPagerProductImages.adapter=viewPager2Adapter}

    }
    }




