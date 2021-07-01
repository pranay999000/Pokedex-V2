package com.az.pokedex.ui.view.pokemondetail

import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.az.pokedex.model.remote.ResponsePokemon
import com.az.pokedex.R
import com.az.pokedex.repository.pokemon.PokemonRepository
import com.az.pokedex.utils.Resource
import com.az.pokedex.utils.parsePokemonName
import com.google.accompanist.coil.rememberCoilPainter

@Composable
fun PokemonDetailView(
    pokemonId: Long,
    dominantColor: Int,
    dominantOnColor: Int,
    viewModel: PokemonDetailViewModel = hiltViewModel()
) {
    val pokemon = produceState<Resource<ResponsePokemon>>(
        initialValue = Resource.Loading()
    ){
        value = viewModel.getPokemonInfo(pokemonId)
    }.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(dominantColor).copy(0.5f), Color(dominantColor)),
                )
            )
    ){
        Icon(
            painterResource(id = R.drawable.ic_pokeball),
            contentDescription = null,
            tint = Color(0x77ffffff),
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.TopEnd)
                .offset(40.dp, (-30).dp)
        )
        when(pokemon){
            is Resource.Success -> {
                PokemonDetail(
                    pokemon = pokemon.data!!,
                    background = Color(dominantColor),
                    onBackground = Color(dominantOnColor)
                )
            }
            is Resource.Error -> {
                Text("Error")
            }
            is Resource.Loading -> {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun PokemonDetail(
    pokemon: ResponsePokemon,
    background: Color,
    onBackground: Color,
) {
    Box{
        Column{
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ){
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                ){
                    Text(
                        parsePokemonName(pokemon.name),
                        fontSize = 32.sp,
                        color = onBackground,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    pokemon.types.forEach { type ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .height(30.dp)
                                .width(100.dp)
                                .background(
                                    onBackground.copy(0.2f),
                                    shape = CircleShape
                                )
                        ){
                            Text(
                                type.type.name.replaceFirstChar { it.uppercaseChar() },
                                color = onBackground,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        MaterialTheme.colors.surface,
                        shape = RoundedCornerShape(
                            topStart = 24.dp,
                            topEnd = 24.dp
                        )
                    )
            ){}
        }
        Image(
            painter = rememberCoilPainter(
                pokemon.sprites.other.officialArtwork.frontDefault,
                fadeIn = true,
                fadeInDurationMs = 300,
            ),
            contentDescription = "${pokemon.name} Image",
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.TopCenter)
                .offset(y = 100.dp)
        )
    }
}