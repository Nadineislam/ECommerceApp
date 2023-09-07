package com.example.ecommerceapp.presentation.fragments.shopping

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.ecommerceapp.BuildConfig
import com.example.ecommerceapp.R
import com.example.ecommerceapp.presentation.activities.LoginRegisterActivity
import com.example.ecommerceapp.databinding.FragmentProfileBinding
import com.example.ecommerceapp.utils.Resource
import com.example.ecommerceapp.utils.showBottomNavigationView
import com.example.ecommerceapp.presentation.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
@AndroidEntryPoint
class ProfileFragment:Fragment() {
    private lateinit var binding:FragmentProfileBinding
    private val profileViewModel by viewModels<ProfileViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.constraintProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_userAccountFragment)
        }
        binding.linearAllOrders.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_ordersFragment2)
        }
        binding.linearBilling.setOnClickListener {
            val action=ProfileFragmentDirections.actionProfileFragmentToBillingFragment(0f,
                emptyArray(),false)
            findNavController().navigate(action)
        }
        binding.linearLogOut.setOnClickListener {
            profileViewModel.logOut()
            val intent=Intent(requireActivity(),LoginRegisterActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        binding.tvVersion.text="Version ${BuildConfig.VERSION_CODE}"
        lifecycleScope.launchWhenStarted {
            profileViewModel.user.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressbarSettings.visibility=View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressbarSettings.visibility=View.INVISIBLE
                        Glide.with(requireView()).load(it.data!!.imagePath).error(ColorDrawable(Color.BLACK)).into(binding.imageUser)
                        binding.tvUserName.text="${it.data.firstName}  ${it.data.lastName}"

                    }
                    is Resource.Error -> {
                        binding.progressbarSettings.visibility=View.INVISIBLE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
    }
}