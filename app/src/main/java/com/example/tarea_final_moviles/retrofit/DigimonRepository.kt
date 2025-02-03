package com.example.tarea_final_moviles.retrofit

import com.example.tarea_final_moviles.retrofit.api.DigimonApi
import com.example.tarea_final_moviles.retrofit.model.CardsItem
import javax.inject.Inject

class DigimonRepository @Inject constructor(
    private val api: DigimonApi
) {
    // Función para obtener la lista de Digimon desde la API
    // intermediario entre la vista y el modelo
    suspend fun getDigimon(): List<CardsItem> {
        return api.getDigimon()
    }
}