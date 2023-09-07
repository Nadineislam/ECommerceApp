package com.example.ecommerceapp.presentation.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.R
import com.example.ecommerceapp.presentation.adapters.BestProductsAdapter
import com.example.ecommerceapp.databinding.FragmentBaseCategoryBinding
import com.example.ecommerceapp.utils.showBottomNavigationView

open class BaseCategoryFragment:Fragment(R.layout.fragment_base_category) {
    private lateinit var binding:FragmentBaseCategoryBinding
    protected val offerAdapter:BestProductsAdapter by lazy {BestProductsAdapter()}
    protected val bestProductsAdapter: BestProductsAdapter by lazy { BestProductsAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentBaseCategoryBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpOfferRv()
        setUpBestProductsRV()
        bestProductsAdapter.onClick = { product ->
            val bundle = Bundle().apply { putParcelable("product", product) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment, bundle)
        }
        offerAdapter.onClick = { product ->
            val bundle = Bundle().apply { putParcelable("product", product) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment, bundle)
        }
//        binding.offerRv.addOnScrollListener(object :RecyclerView.OnScrollListener(){
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                if(!recyclerView.canScrollHorizontally(1)&&dx!=0){
//                    onOfferProductsPagingRequest()
//                }
//            }
//        })
//        binding.nestedScrollViewBaseCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
//            if (v.getChildAt(0).bottom <= v.height + scrollY) {
//               onBestProductsPagingRequest()
//            }
//
//        })
    }
    open fun onOfferProductsPagingRequest(){}

    open fun onBestProductsPagingRequest(){}

    private fun setUpBestProductsRV() {
        binding.rvBestProducts.apply {
            layoutManager=
               GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
            adapter=bestProductsAdapter}
    }

    private fun setUpOfferRv() {
        binding.offerRv.apply {
            layoutManager=
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            adapter=offerAdapter}

    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
    }
}