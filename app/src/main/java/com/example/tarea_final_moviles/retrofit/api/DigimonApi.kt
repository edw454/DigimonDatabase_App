package com.example.tarea_final_moviles.retrofit.api

import com.example.tarea_final_moviles.retrofit.model.CardsItem
import retrofit2.http.GET

interface DigimonApi {
    // Define como se realiza la solicitud GET a la API
    @GET("search.php?pack=BT01-03: RELEASE SPECIAL BOOSTER Ver.1.0")
    suspend fun getDigimon(): List<CardsItem>

}