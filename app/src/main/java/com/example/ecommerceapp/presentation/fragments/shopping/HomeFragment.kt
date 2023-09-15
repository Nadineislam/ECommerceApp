package com.example.ecommerceapp.presentation.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ecommerceapp.presentation.adapters.HomeViewpagerAdapter
import com.example.ecommerceapp.databinding.FragmentHomeBinding
import com.example.ecommerceapp.presentation.fragments.categories.*
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment :Fragment(){
    lateinit var binding:FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentHomeBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
val categoriesFragment= arrayListOf(
    MainCategoryFragment(),
    TableFragment(),
FurnitureFragment(), CupboardFragment(),ChairFragment(),AccessoryFragment()
    )
        binding.viewPager.isUserInputEnabled=false
        val homeViewpagerAdapter=
            HomeViewpagerAdapter(categoriesFragment,childFragmentManager,lifecycle)
        binding.viewPager.adapter=homeViewpagerAdapter
        TabLayoutMediator(binding.tabLayout,binding.viewPager){tab , position->
            when(position){
                0 -> tab.text ="Main"
                1-> tab.text="Accessory"
                2-> tab.text="Table"
                3->tab.text="Cupboard"
                4-> tab.text="Chair"
                5-> tab.text="Furniture"

            }

        }.attach()
    }

}