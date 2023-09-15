package com.example.ecommerceapp.loginRegister.peresentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.loginRegister.data.model.User
import com.example.ecommerceapp.databinding.FragmentRegisterBinding
import com.example.ecommerceapp.utils.RegisterValidation
import com.example.ecommerceapp.utils.Resource
import com.example.ecommerceapp.loginRegister.peresentation.viewmodels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegisterFragment:Fragment() {
private lateinit var binding:FragmentRegisterBinding
private val viewModel by viewModels<RegisterViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvLoginNow.setOnClickListener {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)

        }
        binding.apply {
            btnRegister.setOnClickListener {
                val user= User(etFirstName.text.toString().trim(),etLastName.text.toString().trim()
                    ,etEmail.text.toString().trim())
                val password=etPassword.text.toString()
               viewModel.createAccountWithUserAndPassword(user,password)
                //findNavController().navigate(R.id.action_registerFragment_to_loginFragment)

            }
        }
        lifecycleScope.launch {
            viewModel.register.collect{
                when(it){
                    is Resource.Loading -> {
                        binding.btnRegister.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.btnRegister.revertAnimation()
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)

                    }
                    is Resource.Error -> {
                        binding.btnRegister.revertAnimation()
                        Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }
        lifecycleScope.launch {
            viewModel.validation.collect{ validation ->
                if(validation.email is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.etEmail.apply {
                            requestFocus()
                            error=validation.email.message
                        }
                    }
                }
                if(validation.password is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.etPassword.apply {
                            requestFocus()
                            error=validation.password.message
                        }
                    }
                }

            }
        }

    }
}