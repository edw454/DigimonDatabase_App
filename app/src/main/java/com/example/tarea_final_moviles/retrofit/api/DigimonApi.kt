package com.example.tarea_final_moviles.retrofit.api

import com.example.tarea_final_moviles.retrofit.model.Cards
import com.example.tarea_final_moviles.retrofit.model.CardsItem
import retrofit2.http.GET

interface DigimonApi {
    @GET("search.php?type=digimon&pack=BT01-03: RELEASE SPECIAL BOOSTER Ver.1.0")
    suspend fun getDigimon(): List<CardsItem>

}