package com.example.ecommerceapp.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.example.ecommerceapp.core.utils.FirebaseCommon
import com.example.ecommerceapp.core.utils.Constants.INTRODUCTION_SHARED_PREFERENCES
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
}