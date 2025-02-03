package com.example.tarea_final_moviles.retrofit.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tarea_final_moviles.retrofit.DigimonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DigimonViewModel @Inject constructor (
    private val repository: DigimonRepository
) : ViewModel() {
    //Puente entre la interfaz de usuario y los datos
    private val _digimonList = MutableStateFlow(emptyList<CardsItem>())
    val digimonList: StateFlow<List<CardsItem>>
        get() = _digimonList
        init {
       viewModelScope.launch {
           _digimonList.value = repository.getDigimon()
       }
   }
}