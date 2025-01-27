package com.example.tarea_final_moviles.Main_Structure

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.tarea_final_moviles.retrofit.model.CardsItem
import com.example.tarea_final_moviles.retrofit.model.DigimonViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            //val viewModel: DigimonViewModel = ViewModelProvider(this).get(DigimonViewModel::class.java)
            CardList()
        }
    }

    @Composable
    fun CardList(
        viewModel: DigimonViewModel = hiltViewModel()
    ) {
        val _digimonList by viewModel.digimonList.collectAsState()
        Log.d("Cantidad de cartas","${_digimonList.size}")

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.padding(4.dp)
        ) {
           //Colum {
                items(_digimonList) { card ->
                    DigimonCard(card)
                }

           // }
        }
    }


    @Composable
    fun AlertDialogDoc(
        onDismiss: () -> Unit,
        card: CardsItem,
        imageUrl: String
    ) {
        AlertDialog(
            onDismissRequest = {
                    onDismiss()
                },
                title = {
                    Text(text = card.name)
                },
                text = {
                    Surface {
                        Column (
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp)
                                .verticalScroll(rememberScrollState())
                        ){
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = card.name,
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(0.70f) // Ajustar proporción de la imagen si es necesario
                            )
                            card.main_effect?.let { Text(text = it) }

                            Spacer(
                                modifier = Modifier.padding(4.dp)
                            )

                            card.source_effect?.let { Text(text = it) }
                        }
                    }
                },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text("Aceptar")
                }
            },
                dismissButton = {
                    TextButton(
                        onClick = {
                            onDismiss()
                        }
                    )
                    {
                        Text("Salir")
                    }
                }
        )
    }


    @Composable
    fun DigimonCard(
        card: CardsItem,
        modifier: Modifier = Modifier
    ) {
        var showDialog by remember { mutableStateOf(false) }
        val imageUrl = "https://images.digimoncard.io/images/cards/${card.id}.jpg"

        Card(onClick = { showDialog = true },
            shape = MaterialTheme.shapes.medium,
            modifier = modifier.fillMaxWidth()
                .padding(4.dp)) {
                Row {
                    if (showDialog) {
                        AlertDialogDoc(onDismiss = { showDialog = false }, card, imageUrl)
                    }

                    AsyncImage(
                        model = imageUrl,
                        contentDescription = card.name,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(0.70f) // Ajustar proporción de la imagen si es necesario
                    )
                }
        }
    }


    /*@Composable
    fun CardItem(card: CardsItem) {
        val imageUrl = "https://images.digimoncard.io/images/cards/${card.id}.jpg"

        Column {
            Text(text = card.id)
            Text(text = card.name)
            Text(text = card.color)
            card.main_effect?.let { Text(text = it) }
            AsyncImage(
                model = imageUrl,
                contentDescription = card.name,

            )
        }
    }*/

}