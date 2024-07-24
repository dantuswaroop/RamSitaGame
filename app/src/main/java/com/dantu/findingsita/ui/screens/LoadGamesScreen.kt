package com.dantu.findingsita.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dantu.findingsita.data.entities.Game
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

@Serializable
object LoadGames

@Composable
fun LoadGamesScreen(modifier: Modifier, onGameClicked : (String) -> Unit) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val viewModel : GamesViewModel = viewModel()
    var games by remember {
        mutableStateOf(listOf<Game>())
    }
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val gamesView = createRef()
        LaunchedEffect(key1 = scope) {
            withContext(Dispatchers.IO) {
                viewModel.loadGames(context)
                viewModel.games.collect {
                    games = it
                }
            }
        }
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .constrainAs(gamesView) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                absoluteRight.linkTo(parent.absoluteRight)
                absoluteLeft.linkTo(parent.absoluteLeft)
            }) {
            items(games) {
                Text(text = "${it.created}", modifier.fillMaxWidth(0.9f).clickable {
                        onGameClicked(it.id)
                },
                    fontSize = 24.sp,)
            }
        }
    }
}