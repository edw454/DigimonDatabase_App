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
    /*Se encarga de crear la instancia de Retrofit
     * y de configurar el conversor de JSON a objetos Kotlin
     * utilizando la biblioteca Gson.
     * También se encarga de definir la URL base de la API
     * y de crear una instancia de la interfaz DigimonApi
     *que se utilizará para hacer las llamadas a la API.
    */
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