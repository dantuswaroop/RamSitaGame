package com.dantu.findingsita.ui.screens

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
fun Timer(numberSeconds : Int, modifier: Modifier, timedOut : () -> Unit) {
    var rememberTimer :Int by remember {
        mutableStateOf(numberSeconds)
    }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    LaunchedEffect(key1 = scope) {
        while (rememberTimer > 0) {
            delay(1_000)
            rememberTimer -= 1
            if (rememberTimer == 3) {
                MediaPlayer.create(context, R.raw.timeout).start()
            }
            if (rememberTimer == 0) {
                timedOut()
            }
        }
    }

    Row(modifier = modifier.fillMaxWidth()) {
        Text(text = "Seconds Left :: $rememberTimer", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
    }

}


@Composable
fun ShowGameScreen(
    modifier: Modifier,
    gameId: String,
    onPlayerClicked: (playerId: Int, characterId: Int) -> Unit,
    onRoundResult : (Boolean) -> Unit,
    showLeaderBoard : () -> Unit,
    onMessage : (String) -> Unit
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
        val timer =createRef()
        if (finding == ScreenState.REVEALING) {
            Button(onClick = {
                if (players.filter { !it.revealed }.isEmpty()) {
                    finding = ScreenState.FINDING
                } else {
                    onMessage("Make sure that all the players have seen their chits")
                }
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
        } else if (finding == ScreenState.FINDING) {
            Timer(numberSeconds = 30, modifier.constrainAs(timer) {
                bottom.linkTo(parent.bottom, margin = 20.dp)
                absoluteLeft.linkTo(parent.absoluteLeft)
                absoluteRight.linkTo(parent.absoluteRight)
            }) {
                scope.launch(Dispatchers.IO) {
                    viewModel.validateResult(
                        context,
                        players.filter { it.character?.type == CharacterType.FIND }[0].playerId
                    )
                    withContext(Dispatchers.Main) {
                        onRoundResult(false)
                        finding = ScreenState.RESULT
                        delay(5_000)
                        showLeaderBoard()
                    }
                }
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
                                it.revealed = true
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