package com.example.ecommerceapp.presentation.fragments.shopping

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.ecommerceapp.BuildConfig
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.FragmentProfileBinding
import com.example.ecommerceapp.login_register_feature.peresentation.activity.LoginRegisterActivity
import com.example.ecommerceapp.presentation.viewmodel.ProfileViewModel
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.core.extensions.showBottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel by viewModels<ProfileViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)
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
            val action = ProfileFragmentDirections.actionProfileFragmentToBillingFragment(
                0f,
                emptyArray(), false
            )
            findNavController().navigate(action)
        }
        binding.linearLogOut.setOnClickListener {
            profileViewModel.logOut()
            val intent = Intent(requireActivity(), LoginRegisterActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.tvVersion.text = "Version ${BuildConfig.VERSION_CODE}"
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                profileViewModel.user.collectLatest {
                    when (it) {
                        is Resource.Loading -> {
                            binding.progressbarSettings.visibility = View.VISIBLE
                        }

                        is Resource.Success -> {
                            binding.progressbarSettings.visibility = View.INVISIBLE
                            Glide.with(requireView()).load(it.data!!.imagePath)
                                .error(ColorDrawable(Color.BLACK)).into(binding.imageUser)
                            binding.tvUserName.text = "${it.data.firstName} ${it.data.lastName}"

                        }

                        is Resource.Error -> {
                            binding.progressbarSettings.visibility = View.INVISIBLE
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
    }
}