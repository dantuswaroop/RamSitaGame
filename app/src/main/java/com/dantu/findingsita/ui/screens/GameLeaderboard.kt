package com.dantu.findingsita.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.dantu.findingsita.R
import com.dantu.findingsita.data.entities.GameStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

@Serializable
data class GameLeaderBoard(val gameId:String)

@Composable
fun GameLeaderBoardScreen(modifier: Modifier = Modifier, gameId : String, onReady : () -> Unit) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val viewModel : GameLeaderboardViewModel = viewModel()

    var gameStatuses by remember {
        mutableStateOf(listOf<GameStatus>())
    }

    ConstraintLayout(modifier.fillMaxSize()) {
        LaunchedEffect(key1 = scope) {
            withContext(Dispatchers.IO) {
                viewModel.getLeaderBoard(context, gameId)
            }
            viewModel.gameStatus.collect {
            gameStatuses = it
            }
        }
        val leaderBoardLazyColumn = createRef()
        val startRoundButton = createRef()
        Button(onClick = onReady,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .constrainAs(startRoundButton) {
                    bottom.linkTo(parent.bottom, margin = 20.dp)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                }) {
            Text(text = "Ready")
        }

        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .constrainAs(leaderBoardLazyColumn) {
                top.linkTo(parent.top, margin = 20.dp)
                bottom.linkTo(startRoundButton.top, margin = 80.dp)
                absoluteLeft.linkTo(parent.absoluteLeft)
                absoluteRight.linkTo(parent.absoluteRight)
            }) {
            items(gameStatuses) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(colors[Math.abs(it.player.name.hashCode()) % colors.size]),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    val painter = if(it.player.profilePic != null) {
                        rememberAsyncImagePainter(model = it.player.profilePic)
                    } else {
                        painterResource(id = R.drawable.profile_place_holder)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(painter = painter, contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxHeight(0.95f)
                                .width(70.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(text = "${it.player.name}", modifier = Modifier.padding(horizontal = 20.dp),
                            fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(text = "${it.score}", modifier = Modifier.padding(horizontal = 20.dp))
                }
                Spacer(modifier = Modifier
                    .height(2.dp)
                    .fillMaxWidth())
            }
        }
    }
}