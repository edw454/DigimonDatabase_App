package com.example.tarea_final_moviles.Main_Structure

import android.graphics.drawable.Icon
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        // Mapa de colores para los títulos
        val titleColor = when (card.color) {
            "Black" -> Color.Black
            "Blue" -> Color.Blue
            "Colorless" -> Color.Gray
            "Green" -> Color.Green
            "Purple" -> Color(0xFF800080) // Hex para púrpura
            "Red" -> Color.Red
            "White" -> Color.Gray // Cambiar a gris si es blanco
            "Yellow" -> Color.Yellow
            else -> Color.Black // Por defecto, negro
        }

        AlertDialog(
            onDismissRequest = {
                    onDismiss()
                },
                title = {
                    Column {
                        Text(
                            text = card.name,
                            fontWeight = FontWeight.Bold,
                            color = titleColor
                        )
                        Row (
                            modifier = Modifier.padding(4.dp)
                        ){
                            infoRow(title = "Id", value = card.id, size = true, color = titleColor)
                            Spacer( modifier = Modifier.padding(4.dp))
                            infoRow(title = "Nivel", value = card.level?.toString() ?: "N/A", size = true, color = titleColor)
                        }
                    }
                },
                text = {
                    Surface {
                        Column (
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp)
                                .verticalScroll(rememberScrollState())
                        ){
                            Row {
                                AsyncImage(
                                    model = imageUrl,
                                    contentDescription = card.name,
                                    contentScale = ContentScale.FillBounds,
                                    modifier = Modifier
                                        .width(100.dp) // Ancho personalizado
                                        .height(150.dp) // Ajustar proporción de la imagen si es necesario
                                )
                                Column(
                                    modifier = Modifier.padding(6.dp)
                                ){
                                    infoRow(title = "Color", value = card.color, color = titleColor)
                                    infoRow(title = "Tipo", value = card.digi_type ?: "N/A", color = titleColor)
                                    infoRow(title = "Coste", value = card.play_cost.toString(), color = titleColor)
                                    infoRow(title = "Color Evolutivo", value = card.evolution_color ?: "N/A", color = titleColor)
                                    infoRow(title = "Coste Evolucion", value = card.evolution_cost?.toString() ?: "N/A", color = titleColor)
                                    infoRow(title = "DP", value = card.dp?.toString() ?: "N/A", color = titleColor)
                                }
                            }
                            Spacer(
                                modifier = Modifier.padding(4.dp)
                            )
                            card.main_effect?.let { Text(text = it) }
                            card.source_effect?.takeIf { it.isNotEmpty() }?.let { Text(text = "Inherited Effect " + it) }

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
    fun infoRow(title: String, value: String, size: Boolean = false, color: Color = Color.Black) {
        Row(
            modifier = Modifier.padding(vertical = 2.dp) // Espaciado entre filas
        ) {
            Text(
                color = color,
                style = if (size) {
                    TextStyle(fontSize = 12.sp) // Tamaño de letra reducido si `size` es true
                } else {
                    TextStyle(fontSize = 16.sp) // Tamaño normal
                },
                text = "$title: ",
                fontWeight = FontWeight.Bold // Negrita para el título
            )
            Text(
                text = value,
                style = if (size) {
                    TextStyle(fontSize = 12.sp) // Tamaño de letra reducido si `size` es true
                } else {
                    TextStyle(fontSize = 16.sp) // Tamaño normal
                }
            )
        }
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

    @Preview(showBackground = true, widthDp = 360, heightDp = 640)
    @Composable
    fun PreviewDigimonCard() {
        val sampleCard = CardsItem(
            alt_effect = "Alternate effect example",
            color = "Red",
            color2 = "Blue",
            digi_type = "Dragon",
            dp = 7000,
            evolution_color = "Yellow",
            evolution_cost = 3,
            evolution_level = 4,
            id = "BT1-001",
            level = 3,
            main_effect = "When attacking, gain +2000 DP.",
            name = "Agumon",
            play_cost = 5,
            rarity = "Rare",
            set_name = listOf("BT1", "Promo"),
            source_effect = "When attacking, gain 1 memory.",
            type = "Rookie"
        )
        DigimonCard(card = sampleCard)
    }

    // Preview para CardList
    @Preview(showBackground = true, widthDp = 360, heightDp = 640)
    @Composable
    fun PreviewCardList() {
        val sampleList = List(20) { index ->
            CardsItem(
                alt_effect = null,
                color = "Blue",
                color2 = if (index % 2 == 0) "Red" else null,
                digi_type = "Beast",
                dp = 5000 + index * 100,
                evolution_color = "Yellow",
                evolution_cost = 2,
                evolution_level = 3,
                id = "BT1-${index + 1}",
                level = 2 + index % 4,
                main_effect = "Main effect $index",
                name = "Digimon $index",
                play_cost = 3 + index,
                rarity = if (index % 3 == 0) "Rare" else "Common",
                set_name = listOf("BT1", "EX1"),
                source_effect = "Source effect $index",
                type = "Champion"
            )
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.padding(4.dp)
        ) {
            items(sampleList) { card ->
                DigimonCard(card)
            }
        }
    }

    @Preview(showBackground = true, widthDp = 360, heightDp = 640)
    @Composable
    fun PreviewAlertDialogDoc() {
        val sampleCard = CardsItem(
            alt_effect = "Alternate effect example",
            color = "Red",
            color2 = "Blue",
            digi_type = "Dragon",
            dp = 7000,
            evolution_color = "Yellow",
            evolution_cost = 3,
            evolution_level = 4,
            id = "BT1-001",
            level = 3,
            main_effect = "When attacking, gain +2000 DP.",
            name = "Agumon",
            play_cost = 5,
            rarity = "Rare",
            set_name = listOf("BT1", "Promo"),
            source_effect = "When attacking, gain 1 memory.",
            type = "Rookie"
        )

        AlertDialogDoc(
            onDismiss = { /* No-op for preview */ },
            card = sampleCard,
            imageUrl = "https://images.digimoncard.io/images/cards/${sampleCard.id}.jpg"
        )
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