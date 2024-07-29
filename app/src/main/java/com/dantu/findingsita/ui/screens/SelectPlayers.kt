package com.dantu.findingsita.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.dantu.findingsita.R
import com.dantu.findingsita.data.entities.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

@Serializable
object SelectPlayers


@Composable
fun SelectPlayers(
    modifier: Modifier,
    selectedPlayers : MutableList<Int>,
    onPlayersSelected: (String) -> Unit,
    onMessage: (String) -> Unit
) {
    val selectPlayersViewModel : SelectPlayersViewModel  = hiltViewModel()
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val createGameButton = createRef()
        val playersGrid = createRef()

        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        var allPlayers by remember {
            mutableStateOf<List<Player>>(mutableListOf())
        }


        LaunchedEffect(key1 = scope) {
            launch {
                withContext(Dispatchers.IO) {
                    selectPlayersViewModel.getPlayers()
                    selectPlayersViewModel.allPlayers.collect {
                        allPlayers = it
                    }
                }
            }
        }

        Button(onClick = {
            val selectedPlayers = selectPlayersViewModel.allPlayers.value.filter { it.selected }
            if(selectedPlayers.size > 2) {
                //correct
                scope.launch(Dispatchers.IO) {
                    selectPlayersViewModel.createGameWithPlayers()
                    selectPlayersViewModel.gameId.collect {
                        withContext(Dispatchers.Main) {
                            onPlayersSelected(it)
                        }
                    }
                }
            } else {
                onMessage("Please select atleast 3 players")
            }
        },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .constrainAs(createGameButton) {
                    bottom.linkTo(parent.bottom, margin = 20.dp)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                }) {
            Text(text = "Done")
        }

        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 128.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .constrainAs(playersGrid) {
                    top.linkTo(parent.top, margin = 40.dp)
                    bottom.linkTo(createGameButton.top, margin = 120.dp)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                }) {
            itemsIndexed(items = allPlayers) { index, it ->
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .clickable {
                            scope.launch {
                                selectPlayersViewModel.toggleSelection(index)
                            }
                        }
                        .fillMaxWidth()
                        .background(colors[Math.abs(it.name.hashCode() % colors.size)])
                        .height(128.dp)
                ) {
                    Text(
                        text = it.name, fontSize = 24.sp, fontWeight = FontWeight.Bold
                    )
                    if(it.selected) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_check_24),
                            contentDescription = ""
                        )
                    }
                }
            }
        }
    }
}