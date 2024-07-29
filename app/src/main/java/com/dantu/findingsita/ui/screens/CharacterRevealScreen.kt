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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dantu.findingsita.R
import com.dantu.findingsita.data.entities.CharacterType
import com.dantu.findingsita.data.entities.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable


@Serializable
data class GameScreen(val gameId: String)

private enum class ScreenState {
    REVEALING,
    FINDING,
    RESULT
}


@Composable
fun ShowGameScreen(
    modifier: Modifier,
    gameId: String,
    onPlayerClicked: (playerId: Int, characterId: Int) -> Unit,
    onRoundResult : (Boolean) -> Unit,
    showLeaderBoard : () -> Unit
) {
    val viewModel: RevealCharacterViewModel = viewModel()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var players by remember {
        mutableStateOf(listOf<Player>())
    }

    var finding by remember {
        mutableStateOf(ScreenState.REVEALING)
    }

    ConstraintLayout(modifier.fillMaxSize()) {

        LaunchedEffect(key1 = scope) {
            withContext(Dispatchers.IO) {
                players = viewModel.getPlayersWithCharacters(context, gameId)
            }
        }
        val (startGame, playersGrid) = createRefs()
        if (finding == ScreenState.REVEALING) {
            Button(onClick = {
                finding = ScreenState.FINDING
            },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .constrainAs(startGame) {
                        bottom.linkTo(parent.bottom, margin = 20.dp)
                        absoluteLeft.linkTo(parent.absoluteLeft)
                        absoluteRight.linkTo(parent.absoluteRight)
                    }) {
                Text(text = "Start Finding")
            }
        }

        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 128.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .constrainAs(playersGrid) {
                    top.linkTo(parent.top, margin = 40.dp)
                    bottom.linkTo(startGame.top, margin = 120.dp)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                }) {
            itemsIndexed(items = players) { index, it ->
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .clickable {
                            if (finding == ScreenState.FINDING) {
                                //evaluate and update the score boards
                                scope.launch {
                                    withContext(Dispatchers.IO) {
                                        val success = viewModel.validateResult(context, it.playerId)
                                        withContext(Dispatchers.Main) {
                                            onRoundResult(success)
                                            finding = ScreenState.RESULT
                                            delay(5_000)
                                            showLeaderBoard()
                                        }
                                    }
                                }
                            } else if (finding == ScreenState.REVEALING) {
                                //verifywith pin
                                onPlayerClicked(it.playerId, it.character?.characterId ?: 0)
                            } else if (finding == ScreenState.RESULT) {
                                showLeaderBoard()
                            }
                        }
                        .fillMaxWidth()
                        .background(colors[Math.abs(it.name.hashCode() % colors.size)])
                        .height(128.dp)
                ) {
                    var name = it.name
                    if (finding == ScreenState.FINDING && it.character?.type == CharacterType.FIND) {
                        name += "\n" + it.character?.name
                    } else if (finding == ScreenState.RESULT) {
                        name += "\n" + it.character?.name
                    }
                    Text(
                        text = name, fontSize = 24.sp, fontWeight = FontWeight.Bold
                    )
                    if (it.selected) {
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