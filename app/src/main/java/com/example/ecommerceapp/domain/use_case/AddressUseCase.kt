package com.example.ecommerceapp.domain.use_case

import com.example.ecommerceapp.data.Address
import com.example.ecommerceapp.domain.repository.UserSettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddressUseCase @Inject constructor(private val repository: UserSettingsRepository) {
    suspend operator fun invoke(address: Address) {
        repository.addAddress(address)
    }
}