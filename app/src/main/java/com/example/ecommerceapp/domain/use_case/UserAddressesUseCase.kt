package com.example.ecommerceapp.domain.use_case

import com.example.ecommerceapp.data.Address
import com.example.ecommerceapp.domain.repository.ShoppingRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserAddressesUseCase @Inject constructor(private val repository: ShoppingRepository) {
    suspend operator fun invoke(): List<Address> =
        repository.getUserAddresses()
}