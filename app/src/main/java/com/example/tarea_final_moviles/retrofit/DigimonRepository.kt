package com.example.tarea_final_moviles.retrofit

import com.example.tarea_final_moviles.retrofit.api.DigimonApi
import com.example.tarea_final_moviles.retrofit.model.Cards
import com.example.tarea_final_moviles.retrofit.model.CardsItem
import javax.inject.Inject

class DigimonRepository @Inject constructor(
    private val api: DigimonApi
) {
    suspend fun getDigimon(): List<CardsItem> {
        return api.getDigimon()
    }
}