package com.example.ecommerceapp.di

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.ecommerceapp.core.utils.FirebaseCommon
import com.example.ecommerceapp.core.utils.Constants.INTRODUCTION_SHARED_PREFERENCES
import com.example.ecommerceapp.data.repository.CartRepositoryImpl
import com.example.ecommerceapp.data.repository.ShoppingRepositoryImpl
import com.example.ecommerceapp.data.repository.UserSettingsRepositoryImpl
import com.example.ecommerceapp.domain.repository.CartRepository
import com.example.ecommerceapp.domain.repository.ShoppingRepository
import com.example.ecommerceapp.domain.repository.UserSettingsRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore() = Firebase.firestore

    @Provides
    fun provideIntroductionSharedPreferences(application: Application) =
        application.getSharedPreferences(INTRODUCTION_SHARED_PREFERENCES, MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideFirebaseCommon(fireStore: FirebaseFirestore, firebaseAuth: FirebaseAuth) =
        FirebaseCommon(fireStore, firebaseAuth)

    @Provides
    @Singleton
    fun provideStorage() = FirebaseStorage.getInstance().reference

    @Provides
    @Singleton
    fun providesCartRepository(
        fireStore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth,
        fireBaseCommon: FirebaseCommon
    ): CartRepository {
        return CartRepositoryImpl(fireStore, firebaseAuth, fireBaseCommon)
    }

    @Provides
    @Singleton
    fun providesUserSettingsRepository(
        fireStore: FirebaseFirestore,
        auth: FirebaseAuth,
        storage: StorageReference, @ApplicationContext context: Context
    ): UserSettingsRepository {
        return UserSettingsRepositoryImpl(fireStore, auth, storage, context)
    }

    @Provides
    @Singleton
    fun providesShoppingRepository(
        fireStore: FirebaseFirestore,
        auth: FirebaseAuth
    ): ShoppingRepository {
        return ShoppingRepositoryImpl(fireStore, auth)
    }
}