package com.dantu.findingsita.ui.screens

import android.R
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.graphics.toColorInt
import com.dantu.findingsita.data.DataBaseHelper
import com.dantu.findingsita.data.entities.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object PlayerList

val colors = listOf<Color>(Color(255, 170, 186),
    Color(255, 223, 186),
    Color(255, 225, 186),
    Color(186, 225, 201),
    Color(186, 225, 255),
    Color(252,176,191),
    Color(254,228,233),
    Color(240,235,210),
    Color(242,205,180),
    Color(212,175,55),
    Color(206,221,249),
    Color(229,225,251),
    Color(218,241,197),
    Color(255,245,197),
    Color(252,231,238)
)

@Composable
fun PlayerListScreen(modifier: Modifier, onAddNewPlayer: (Int) -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        var allPlayers by remember {
            mutableStateOf<List<Player>>(listOf())
        }

        val addNewPlayerButton = createRef()
        val buttonBackground = createRef()
        val playerListLazyColumn = createRef()

        LaunchedEffect(key1 = scope) {
            launch(Dispatchers.IO) {
                allPlayers =
                    DataBaseHelper.getInstance(context).playerDao().getAllPlayers().first()
                Log.i("", allPlayers!!.size.toString())
            }
        }

        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 128.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = modifier
                .fillMaxSize()
                .constrainAs(playerListLazyColumn) {
                    top.linkTo(parent.top, margin = 40.dp)
                    bottom.linkTo(buttonBackground.top, margin = 100.dp)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                }) {
            items(allPlayers) {
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .clickable {
                            onAddNewPlayer(it.id)
                        }
                        .fillMaxWidth()
                        .background(colors[Math.abs(it.name.hashCode() % colors.size)])
                        .height(128.dp)
                ) {
                    Text(
                        text = it.name, fontSize = 24.sp, fontWeight = FontWeight.Bold
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_menu_edit),
                        contentDescription = ""
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .height(100.dp)
                .constrainAs(buttonBackground) {
                    bottom.linkTo(parent.bottom)
                    top.linkTo(addNewPlayerButton.top)
                })
        Button(onClick = { onAddNewPlayer(0) },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .constrainAs(addNewPlayerButton) {
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                    bottom.linkTo(parent.bottom, margin = 20.dp)
                }) {
            Text(text = "Add New Player")
        }



    }

}