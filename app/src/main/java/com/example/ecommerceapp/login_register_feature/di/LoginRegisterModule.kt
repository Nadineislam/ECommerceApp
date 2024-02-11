package com.example.ecommerceapp.login_register_feature.di

import com.example.ecommerceapp.login_register_feature.data.repository.LoginRegisterRepositoryImpl
import com.example.ecommerceapp.login_register_feature.domain.repository.LoginRegisterRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginRegisterModule {
    @Provides
    @Singleton
    fun providesLoginRegisterRepository(
        firebaseAuth: FirebaseAuth,
        db: FirebaseFirestore
    ): LoginRegisterRepository {
        return LoginRegisterRepositoryImpl(firebaseAuth, db)
    }
}