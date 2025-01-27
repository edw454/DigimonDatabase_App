package com.example.tarea_final_moviles.retrofit

import com.example.tarea_final_moviles.retrofit.api.DigimonApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitInstance {

    private const val URL = "https://digimoncard.io/api-public/"
    @Provides
    @Singleton
    fun provideApi(): DigimonApi {
        return Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DigimonApi::class.java)
    }
}